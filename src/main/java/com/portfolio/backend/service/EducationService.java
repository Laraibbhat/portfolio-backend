package com.portfolio.backend.service;

import com.portfolio.backend.dto.EducationDTO;
import com.portfolio.backend.dto.EducationAchievementDTO;
import com.portfolio.backend.entity.Education;
import com.portfolio.backend.entity.EducationAchievement;
import com.portfolio.backend.entity.User;
import com.portfolio.backend.exception.ResourceNotFoundException;
import com.portfolio.backend.mapper.EducationMapper;
import com.portfolio.backend.mapper.EducationAchievementMapper;
import com.portfolio.backend.repository.EducationRepository;
import com.portfolio.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EducationService {

    private final EducationRepository educationRepository;
    private final UserRepository userRepository;
    private final EducationMapper educationMapper;
    private final EducationAchievementMapper achievementMapper;

    @Autowired
    public EducationService(EducationRepository educationRepository, UserRepository userRepository,
                            EducationMapper educationMapper, EducationAchievementMapper achievementMapper) {
        this.educationRepository = educationRepository;
        this.userRepository = userRepository;
        this.educationMapper = educationMapper;
        this.achievementMapper = achievementMapper;
    }

    // Helper method to find user by ID
    private User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    // Helper method to find education by ID and user
    private Education findEducationByIdAndUser(Integer educationId, User user) {
        return educationRepository.findByIdAndUser(educationId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found with id: " + educationId + " for user: " + user.getId()));
    }

    @Transactional
    public EducationDTO createEducation(Integer userId, EducationDTO educationDTO) {
        User user = findUserById(userId);

        Education education = educationMapper.toEntity(educationDTO);
        education.setUser(user);

        // Handle achievements
        if (educationDTO.getAchievements() != null && !educationDTO.getAchievements().isEmpty()) {
            Set<EducationAchievement> achievements = educationDTO.getAchievements().stream()
                    .map(dto -> {
                        EducationAchievement achievement = achievementMapper.toEntity(dto);
                        achievement.setEducation(education); // Set parent education
                        return achievement;
                    })
                    .collect(Collectors.toSet());
            education.setAchievements(achievements);
        }

        Education savedEducation = educationRepository.save(education);
        return educationMapper.toDto(savedEducation);
    }

    @Transactional(readOnly = true)
    public List<EducationDTO> getAllEducationByUserId(Integer userId) {
        User user = findUserById(userId);
        List<Education> educationList = educationRepository.findByUser(user);
        return educationList.stream()
                .map(educationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EducationDTO getEducationByIdForUser(Integer userId, Integer educationId) {
        User user = findUserById(userId);
        Education education = findEducationByIdAndUser(educationId, user);
        return educationMapper.toDto(education);
    }

    @Transactional
    public EducationDTO updateEducation(Integer userId, Integer educationId, EducationDTO educationDTO) {
        User user = findUserById(userId);
        Education existingEducation = findEducationByIdAndUser(educationId, user);

        educationMapper.updateEntityFromDto(educationDTO, existingEducation);

        // Handle achievements update
        if (educationDTO.getAchievements() != null) {
            // Current achievements linked to the existingEducation
            Set<EducationAchievement> currentAchievements = existingEducation.getAchievements();
            // Incoming achievements from DTO
            Set<EducationAchievementDTO> incomingAchievementDTOs = educationDTO.getAchievements();

            // 1. Achievements to remove (exist in current but not in incoming DTOs)
            Set<EducationAchievement> achievementsToRemove = currentAchievements.stream()
                .filter(current -> incomingAchievementDTOs.stream().noneMatch(incoming -> incoming.getId() != null && incoming.getId().equals(current.getId())))
                .collect(Collectors.toSet());
            currentAchievements.removeAll(achievementsToRemove); // Remove from the collection

            // 2. Achievements to add or update
            for (EducationAchievementDTO incomingDto : incomingAchievementDTOs) {
                if (incomingDto.getId() == null) { // New achievement
                    EducationAchievement newAchievement = achievementMapper.toEntity(incomingDto);
                    newAchievement.setEducation(existingEducation);
                    currentAchievements.add(newAchievement);
                } else { // Existing achievement, find and update
                    currentAchievements.stream()
                        .filter(current -> current.getId().equals(incomingDto.getId()))
                        .findFirst()
                        .ifPresent(existingAchievement -> achievementMapper.updateEntityFromDto(incomingDto, existingAchievement));
                }
            }
        } else {
            // If incoming achievements are null, clear all existing achievements
            existingEducation.getAchievements().clear();
        }


        Education updatedEducation = educationRepository.save(existingEducation);
        return educationMapper.toDto(updatedEducation);
    }

    @Transactional
    public void deleteEducation(Integer userId, Integer educationId) {
        User user = findUserById(userId);
        Education education = findEducationByIdAndUser(educationId, user);
        educationRepository.delete(education);
    }
}