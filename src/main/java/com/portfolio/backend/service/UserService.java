package com.portfolio.backend.service;

import com.portfolio.backend.dto.UserDTO;
import com.portfolio.backend.dto.UserRequestDTO;
import com.portfolio.backend.dto.PresignedUrlResponse;
import com.portfolio.backend.entity.*;
import com.portfolio.backend.exception.ResourceNotFoundException;
import com.portfolio.backend.mapper.*;
import com.portfolio.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TechnicalExpertiseMapper technicalExpertiseMapper;
    private final ExperienceMapper experienceMapper;
    private final ExperienceHighlightMapper experienceHighlightMapper;
    private final EducationMapper educationMapper;
    private final EducationAchievementMapper educationAchievementMapper;
    private final CertificationMapper certificationMapper;
    private final PublicationMapper publicationMapper;
    private final AwardMapper awardMapper;
    private final CoreCompetencyMapper coreCompetencyMapper;
    private final S3UploadsService s3UploadsService;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper,
                       TechnicalExpertiseMapper technicalExpertiseMapper,
                       ExperienceMapper experienceMapper,
                       ExperienceHighlightMapper experienceHighlightMapper,
                       EducationMapper educationMapper,
                       EducationAchievementMapper educationAchievementMapper,
                       CertificationMapper certificationMapper,
                       PublicationMapper publicationMapper,
                       AwardMapper awardMapper,
                       CoreCompetencyMapper coreCompetencyMapper,
                       S3UploadsService s3UploadsService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.technicalExpertiseMapper = technicalExpertiseMapper;
        this.experienceMapper = experienceMapper;
        this.experienceHighlightMapper = experienceHighlightMapper;
        this.educationMapper = educationMapper;
        this.educationAchievementMapper = educationAchievementMapper;
        this.certificationMapper = certificationMapper;
        this.publicationMapper = publicationMapper;
        this.awardMapper = awardMapper;
        this.coreCompetencyMapper = coreCompetencyMapper;
        this.s3UploadsService = s3UploadsService;
    }

    // Helper method to populate avatarUrl from a DTO's avatarKey. This version performs S3 network I/O
    // and should be called outside of a DB transaction to avoid holding locks during network calls.
    private UserDTO populateAvatarUrl(UserDTO userDTO) {
        String avatarKey = userDTO.getAvatarKey();
        if (avatarKey != null && !avatarKey.isEmpty()) {
            try {
                PresignedUrlResponse presignedUrlResponse = s3UploadsService.generatePresignedDownloadUrl(avatarKey);
                userDTO.setAvatarUrl(presignedUrlResponse.getUploadUrl()); // Assuming getUploadUrl() returns the download URL
            } catch (ResourceNotFoundException e) {
                // Avatar key exists but S3 object not found - leave avatarUrl null
                System.err.println("Avatar S3 object not found for key: " + avatarKey);
            } catch (Exception e) {
                System.err.println("Error generating presigned URL for avatar key: " + avatarKey + " - " + e.getMessage());
            }
        }
        return userDTO;
    }

    // Non-transactional public method: fetches data in a short transactional helper and then
    // performs S3 calls outside the transaction to prevent holding DB locks while doing network I/O.
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = fetchAllUsersWithDetailsTransactional();
        users.forEach(this::populateAvatarUrl);
        return users;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> fetchAllUsersWithDetailsTransactional() {
        return userRepository.findAllWithDetails().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Integer id) {
        UserDTO userDTO = fetchUserByIdWithDetailsTransactional(id);
        return populateAvatarUrl(userDTO);
    }

    @Transactional(readOnly = true)
    public UserDTO fetchUserByIdWithDetailsTransactional(Integer id) {
        User user = userRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    public UserDTO getUserByUsername(String username) {
        UserDTO userDTO = fetchUserByUsernameWithDetailsTransactional(username);
        return populateAvatarUrl(userDTO);
    }

    @Transactional(readOnly = true)
    public UserDTO fetchUserByUsernameWithDetailsTransactional(String username) {
        User user = userRepository.findByUsernameWithDetails(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDTO createUser(UserRequestDTO userRequestDTO) {
        // Map basic user fields
        User user = userMapper.toEntity(userRequestDTO);
        
        // Map and set nested collections with user reference
        if (userRequestDTO.getTechnicalExpertise() != null) {
            user.setTechnicalExpertise(userRequestDTO.getTechnicalExpertise().stream()
                    .map(dto -> {
                        TechnicalExpertise entity = technicalExpertiseMapper.toEntity(dto);
                        entity.setUser(user);
                        return entity;
                    })
                    .collect(Collectors.toSet()));
        }
        
        if (userRequestDTO.getExperiences() != null) {
            user.setExperiences(userRequestDTO.getExperiences().stream()
                    .map(dto -> {
                        Experience entity = experienceMapper.toEntity(dto);
                        entity.setUser(user);
                        // Map nested highlights
                        if (dto.getHighlights() != null) {
                            entity.setHighlights(dto.getHighlights().stream()
                                    .map(highlightDto -> {
                                        ExperienceHighlight highlight = experienceHighlightMapper.toEntity(highlightDto);
                                        highlight.setExperience(entity);
                                        return highlight;
                                    })
                                    .collect(Collectors.toSet()));
                        }
                        return entity;
                    })
                    .collect(Collectors.toSet()));
        }
        
        if (userRequestDTO.getEducation() != null) {
            user.setEducation(userRequestDTO.getEducation().stream()
                    .map(dto -> {
                        Education entity = educationMapper.toEntity(dto);
                        entity.setUser(user);
                        // Map nested achievements
                        if (dto.getAchievements() != null) {
                            entity.setAchievements(dto.getAchievements().stream()
                                    .map(achievementDto -> {
                                        EducationAchievement achievement = educationAchievementMapper.toEntity(achievementDto);
                                        achievement.setEducation(entity);
                                        return achievement;
                                    })
                                    .collect(Collectors.toSet()));
                        }
                        return entity;
                    })
                    .collect(Collectors.toSet()));
        }
        
        if (userRequestDTO.getCertifications() != null) {
            user.setCertifications(userRequestDTO.getCertifications().stream()
                    .map(dto -> {
                        Certification entity = certificationMapper.toEntity(dto);
                        entity.setUser(user);
                        return entity;
                    })
                    .collect(Collectors.toSet()));
        }
        
        if (userRequestDTO.getPublications() != null) {
            user.setPublications(userRequestDTO.getPublications().stream()
                    .map(dto -> {
                        Publication entity = publicationMapper.toEntity(dto);
                        entity.setUser(user);
                        return entity;
                    })
                    .collect(Collectors.toSet()));
        }
        
        if (userRequestDTO.getAwards() != null) {
            user.setAwards(userRequestDTO.getAwards().stream()
                    .map(dto -> {
                        Award entity = awardMapper.toEntity(dto);
                        entity.setUser(user);
                        return entity;
                    })
                    .collect(Collectors.toSet()));
        }
        
        if (userRequestDTO.getCoreCompetencies() != null) {
            user.setCoreCompetencies(userRequestDTO.getCoreCompetencies().stream()
                    .map(dto -> {
                        CoreCompetency entity = coreCompetencyMapper.toEntity(dto);
                        entity.setUser(user);
                        return entity;
                    })
                    .collect(Collectors.toSet()));
        }
        
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Transactional
    public UserDTO updateUserByUsername(String username, UserRequestDTO userRequestDTO) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        userMapper.updateEntityFromDto(userRequestDTO, existingUser);
        
        // Update nested collections - clear and rebuild
        if (userRequestDTO.getTechnicalExpertise() != null) {
            existingUser.getTechnicalExpertise().clear();
            existingUser.setTechnicalExpertise(userRequestDTO.getTechnicalExpertise().stream()
                    .map(dto -> {
                        TechnicalExpertise entity = technicalExpertiseMapper.toEntity(dto);
                        entity.setUser(existingUser);
                        return entity;
                    })
                    .collect(Collectors.toSet()));
        }
        
        if (userRequestDTO.getExperiences() != null) {
            existingUser.getExperiences().clear();
            existingUser.setExperiences(userRequestDTO.getExperiences().stream()
                    .map(dto -> {
                        Experience entity = experienceMapper.toEntity(dto);
                        entity.setUser(existingUser);
                        // Map nested highlights
                        if (dto.getHighlights() != null) {
                            entity.setHighlights(dto.getHighlights().stream()
                                    .map(highlightDto -> {
                                        ExperienceHighlight highlight = experienceHighlightMapper.toEntity(highlightDto);
                                        highlight.setExperience(entity);
                                        return highlight;
                                    })
                                    .collect(Collectors.toSet()));
                        }
                        return entity;
                    })
                    .collect(Collectors.toSet()));
        }
        
        if (userRequestDTO.getEducation() != null) {
            existingUser.getEducation().clear();
            existingUser.setEducation(userRequestDTO.getEducation().stream()
                    .map(dto -> {
                        Education entity = educationMapper.toEntity(dto);
                        entity.setUser(existingUser);
                        // Map nested achievements
                        if (dto.getAchievements() != null) {
                            entity.setAchievements(dto.getAchievements().stream()
                                    .map(achievementDto -> {
                                        EducationAchievement achievement = educationAchievementMapper.toEntity(achievementDto);
                                        achievement.setEducation(entity);
                                        return achievement;
                                    })
                                    .collect(Collectors.toSet()));
                        }
                        return entity;
                    })
                    .collect(Collectors.toSet()));
        }
        
        if (userRequestDTO.getCertifications() != null) {
            existingUser.getCertifications().clear();
            existingUser.setCertifications(userRequestDTO.getCertifications().stream()
                    .map(dto -> {
                        Certification entity = certificationMapper.toEntity(dto);
                        entity.setUser(existingUser);
                        return entity;
                    })
                    .collect(Collectors.toSet()));
        }
        
        if (userRequestDTO.getPublications() != null) {
            existingUser.getPublications().clear();
            existingUser.setPublications(userRequestDTO.getPublications().stream()
                    .map(dto -> {
                        Publication entity = publicationMapper.toEntity(dto);
                        entity.setUser(existingUser);
                        return entity;
                    })
                    .collect(Collectors.toSet()));
        }
        
        if (userRequestDTO.getAwards() != null) {
            existingUser.getAwards().clear();
            existingUser.setAwards(userRequestDTO.getAwards().stream()
                    .map(dto -> {
                        Award entity = awardMapper.toEntity(dto);
                        entity.setUser(existingUser);
                        return entity;
                    })
                    .collect(Collectors.toSet()));
        }
        
        if (userRequestDTO.getCoreCompetencies() != null) {
            existingUser.getCoreCompetencies().clear();
            existingUser.setCoreCompetencies(userRequestDTO.getCoreCompetencies().stream()
                    .map(dto -> {
                        CoreCompetency entity = coreCompetencyMapper.toEntity(dto);
                        entity.setUser(existingUser);
                        return entity;
                    })
                    .collect(Collectors.toSet()));
        }
        
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    @Transactional
    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        userRepository.delete(user);
    }

    @Transactional
    public void updateUserAvatar(String username, String avatarKey) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        user.setAvatarKey(avatarKey);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public PresignedUrlResponse getAvatarPresignedUrl(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
        if (user.getAvatarKey() == null || user.getAvatarKey().isEmpty()) {
            throw new ResourceNotFoundException("No avatar found for user: " + username);
        }
        
        return s3UploadsService.generatePresignedDownloadUrl(user.getAvatarKey());
    }
}