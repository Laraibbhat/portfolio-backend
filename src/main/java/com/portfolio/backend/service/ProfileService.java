package com.portfolio.backend.service;

import com.portfolio.backend.dto.*;
import com.portfolio.backend.entity.*;
import com.portfolio.backend.exception.ResourceNotFoundException;
import com.portfolio.backend.mapper.*; // Import all mappers
import com.portfolio.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;
    private final TechnicalExpertiseMapper technicalExpertiseMapper;
    private final ExperienceMapper experienceMapper;
    private final ExperienceHighlightMapper experienceHighlightMapper; // Added
    private final EducationMapper educationMapper;
    private final EducationAchievementMapper educationAchievementMapper; // Added
    private final CertificationMapper certificationMapper;
    private final PublicationMapper publicationMapper;
    private final AwardMapper awardMapper;
    private final CoreCompetencyMapper coreCompetencyMapper;

    private final S3UploadsService s3UploadsService;

    @Autowired
    public ProfileService(UserRepository userRepository,
                          ProfileMapper profileMapper,
                          TechnicalExpertiseMapper technicalExpertiseMapper,
                          ExperienceMapper experienceMapper,
                          ExperienceHighlightMapper experienceHighlightMapper, // Added to constructor
                          EducationMapper educationMapper,
                          EducationAchievementMapper educationAchievementMapper, // Added to constructor
                          CertificationMapper certificationMapper,
                          PublicationMapper publicationMapper,
                          AwardMapper awardMapper,
                          CoreCompetencyMapper coreCompetencyMapper,
                          S3UploadsService s3UploadsService) {
        this.userRepository = userRepository;
        this.profileMapper = profileMapper;
        this.technicalExpertiseMapper = technicalExpertiseMapper;
        this.experienceMapper = experienceMapper;
        this.experienceHighlightMapper = experienceHighlightMapper; // Initialized
        this.educationMapper = educationMapper;
        this.educationAchievementMapper = educationAchievementMapper; // Initialized
        this.certificationMapper = certificationMapper;
        this.publicationMapper = publicationMapper;
        this.awardMapper = awardMapper;
        this.coreCompetencyMapper = coreCompetencyMapper;
        this.s3UploadsService = s3UploadsService;
    }

    @Transactional(readOnly = true)
    public List<ProfileDTO> getAllProfiles() {
        return userRepository.findAllWithDetails().stream()
                .map(user -> {
                    // Force initialization of all collections and nested collections while transaction is active
                    initializeUserCollections(user);
                    return profileMapper.toProfileDTO(user);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProfileDTO getProfileByUsername(String username) {
        User user = userRepository.findByUsernameWithDetails(username)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with username: " + username));
        // Force initialization of all collections and nested collections while transaction is active
        initializeUserCollections(user);
        
        ProfileDTO profileDTO = profileMapper.toProfileDTO(user);
        
        // Add presigned avatar URL if avatar exists
        if (user.getAvatarKey() != null && !user.getAvatarKey().isEmpty()) {
            try {
                PresignedUrlResponse presignedUrl = s3UploadsService.generatePresignedDownloadUrl(user.getAvatarKey());
                profileDTO.setAvatarUrl(presignedUrl.getUploadUrl());
            } catch (Exception e) {
                // Log error but don't fail the profile fetch
                System.err.println("Failed to generate avatar presigned URL: " + e.getMessage());
            }
        }
        
        return profileDTO;
    }

    private void initializeUserCollections(User user) {
        user.getTechnicalExpertise().size();
        user.getExperiences().forEach(exp -> exp.getHighlights().size());
        user.getEducation().forEach(edu -> edu.getAchievements().size());
        user.getCertifications().size();
        user.getPublications().size();
        user.getAwards().size();
        user.getCoreCompetencies().size();
    }

    @Transactional
    public ProfileDTO createProfile(ProfileDTO profileDTO) {
        User user = profileMapper.toUser(profileDTO); // Map basic user fields

        // Handle Technical Expertise
        if (profileDTO.getTechnicalExpertise() != null) {
            Set<TechnicalExpertise> technicalExpertiseSet = new HashSet<>();
            profileDTO.getTechnicalExpertise().forEach((category, skills) -> {
                skills.forEach(skill -> {
                    TechnicalExpertiseDTO teDTO = new TechnicalExpertiseDTO();
                    teDTO.setCategory(category);
                    teDTO.setSkill(skill);
                    TechnicalExpertise technicalExpertise = technicalExpertiseMapper.toEntity(teDTO);
                    technicalExpertise.setUser(user);
                    technicalExpertiseSet.add(technicalExpertise);
                });
            });
            user.setTechnicalExpertise(technicalExpertiseSet);
        }

        // Handle Experiences
        if (profileDTO.getExperiences() != null) {
            Set<Experience> experienceSet = profileDTO.getExperiences().stream()
                    .map(expDTO -> {
                        Experience experience = experienceMapper.toEntity(expDTO);
                        experience.setUser(user);
                        // Handle Experience Highlights
                        if (expDTO.getHighlights() != null) {
                            Set<ExperienceHighlight> highlights = expDTO.getHighlights().stream()
                                    .map(highlightDTO -> {
                                        ExperienceHighlight highlight = experienceHighlightMapper.toEntity(highlightDTO);
                                        highlight.setExperience(experience);
                                        return highlight;
                                    })
                                    .collect(Collectors.toSet());
                            experience.setHighlights(highlights);
                        }
                        return experience;
                    })
                    .collect(Collectors.toSet());
            user.setExperiences(experienceSet);
        }

        // Handle Education
        if (profileDTO.getEducations() != null) {
            Set<Education> educationSet = profileDTO.getEducations().stream()
                    .map(eduDTO -> {
                        Education education = educationMapper.toEntity(eduDTO);
                        education.setUser(user);
                        // Handle Education Achievements
                        if (eduDTO.getAchievements() != null) {
                            Set<EducationAchievement> achievements = eduDTO.getAchievements().stream()
                                    .map(achievementDTO -> {
                                        EducationAchievement achievement = educationAchievementMapper.toEntity(achievementDTO);
                                        achievement.setEducation(education);
                                        return achievement;
                                    })
                                    .collect(Collectors.toSet());
                            education.setAchievements(achievements);
                        }
                        return education;
                    })
                    .collect(Collectors.toSet());
            user.setEducation(educationSet);
        }

        // Handle Certifications
        if (profileDTO.getCertifications() != null) {
            Set<Certification> certificationSet = profileDTO.getCertifications().stream()
                    .map(certDTO -> {
                        Certification certification = certificationMapper.toEntity(certDTO);
                        certification.setUser(user);
                        return certification;
                    })
                    .collect(Collectors.toSet());
            user.setCertifications(certificationSet);
        }

        // Handle Publications
        if (profileDTO.getPublications() != null) {
            Set<Publication> publicationSet = profileDTO.getPublications().stream()
                    .map(pubDTO -> {
                        Publication publication = publicationMapper.toEntity(pubDTO);
                        publication.setUser(user);
                        return publication;
                    })
                    .collect(Collectors.toSet());
            user.setPublications(publicationSet);
        }

        // Handle Awards
        if (profileDTO.getAwards() != null) {
            Set<Award> awardSet = profileDTO.getAwards().stream()
                    .map(awardText -> {
                        AwardDTO awardDTO = new AwardDTO();
                        awardDTO.setAwardText(awardText);
                        Award award = awardMapper.toEntity(awardDTO);
                        award.setUser(user);
                        return award;
                    })
                    .collect(Collectors.toSet());
            user.setAwards(awardSet);
        }

        // Handle Core Competencies
        if (profileDTO.getCoreCompetencies() != null) {
            Set<CoreCompetency> coreCompetencySet = profileDTO.getCoreCompetencies().stream()
                    .map(ccDTO -> {
                        CoreCompetency coreCompetency = coreCompetencyMapper.toEntity(ccDTO);
                        coreCompetency.setUser(user);
                        return coreCompetency;
                    })
                    .collect(Collectors.toSet());
            user.setCoreCompetencies(coreCompetencySet);
        }

        User savedUser = userRepository.save(user); // Save the user and cascade all children
        return profileMapper.toProfileDTO(savedUser);
    }

    @Transactional
    public ProfileDTO updateProfile(String username, ProfileDTO profileDTO) {
        User existingUser = userRepository.findByUsernameWithDetails(username)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with username: " + username));

        profileMapper.updateUserFromProfileDTO(profileDTO, existingUser); // Update basic user fields

        // Update Technical Expertise
        if (profileDTO.getTechnicalExpertise() != null) {
            existingUser.getTechnicalExpertise().clear(); // Clear existing to trigger orphanRemoval
            profileDTO.getTechnicalExpertise().forEach((category, skills) -> {
                skills.forEach(skill -> {
                    TechnicalExpertiseDTO teDTO = new TechnicalExpertiseDTO();
                    teDTO.setCategory(category);
                    teDTO.setSkill(skill);
                    TechnicalExpertise technicalExpertise = technicalExpertiseMapper.toEntity(teDTO);
                    technicalExpertise.setUser(existingUser);
                    existingUser.getTechnicalExpertise().add(technicalExpertise);
                });
            });
        }

        // Update Experiences
        if (profileDTO.getExperiences() != null) {
            existingUser.getExperiences().clear();
            profileDTO.getExperiences().forEach(expDTO -> {
                Experience experience = experienceMapper.toEntity(expDTO);
                experience.setUser(existingUser);
                // Update Experience Highlights
                if (expDTO.getHighlights() != null) {
                    Set<ExperienceHighlight> highlights = expDTO.getHighlights().stream()
                            .map(highlightDTO -> {
                                ExperienceHighlight highlight = experienceHighlightMapper.toEntity(highlightDTO);
                                highlight.setExperience(experience);
                                return highlight;
                            })
                            .collect(Collectors.toSet());
                    experience.setHighlights(highlights);
                }
                existingUser.getExperiences().add(experience);
            });
        }

        // Update Education
        if (profileDTO.getEducations() != null) {
            existingUser.getEducation().clear();
            profileDTO.getEducations().forEach(eduDTO -> {
                Education education = educationMapper.toEntity(eduDTO);
                education.setUser(existingUser);
                // Update Education Achievements
                if (eduDTO.getAchievements() != null) {
                    Set<EducationAchievement> achievements = eduDTO.getAchievements().stream()
                            .map(achievementDTO -> {
                                EducationAchievement achievement = educationAchievementMapper.toEntity(achievementDTO);
                                achievement.setEducation(education);
                                return achievement;
                            })
                            .collect(Collectors.toSet());
                    education.setAchievements(achievements);
                }
                existingUser.getEducation().add(education);
            });
        }

        // Update Certifications
        if (profileDTO.getCertifications() != null) {
            existingUser.getCertifications().clear();
            profileDTO.getCertifications().forEach(certDTO -> {
                Certification certification = certificationMapper.toEntity(certDTO);
                certification.setUser(existingUser);
                existingUser.getCertifications().add(certification);
            });
        }

        // Update Publications
        if (profileDTO.getPublications() != null) {
            existingUser.getPublications().clear();
            profileDTO.getPublications().forEach(pubDTO -> {
                Publication publication = publicationMapper.toEntity(pubDTO);
                publication.setUser(existingUser);
                existingUser.getPublications().add(publication);
            });
        }

        // Update Awards
        if (profileDTO.getAwards() != null) {
            existingUser.getAwards().clear();
            profileDTO.getAwards().forEach(awardText -> {
                AwardDTO awardDTO = new AwardDTO();
                awardDTO.setAwardText(awardText);
                Award award = awardMapper.toEntity(awardDTO);
                award.setUser(existingUser);
                existingUser.getAwards().add(award);
            });
        }

        // Update Core Competencies
        if (profileDTO.getCoreCompetencies() != null) {
            existingUser.getCoreCompetencies().clear();
            profileDTO.getCoreCompetencies().forEach(ccDTO -> {
                CoreCompetency coreCompetency = coreCompetencyMapper.toEntity(ccDTO);
                coreCompetency.setUser(existingUser);
                existingUser.getCoreCompetencies().add(coreCompetency);
            });
        }

        User updatedUser = userRepository.save(existingUser); // Save the user and cascade all changes
        return profileMapper.toProfileDTO(updatedUser);
    }

    @Transactional
    public void deleteProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with username: " + username));
        userRepository.delete(user);
    }
}