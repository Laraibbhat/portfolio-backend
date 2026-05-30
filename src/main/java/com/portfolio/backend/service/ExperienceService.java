package com.portfolio.backend.service;

import com.portfolio.backend.dto.ExperienceDTO;
import com.portfolio.backend.dto.ExperienceHighlightDTO;
import com.portfolio.backend.entity.Experience;
import com.portfolio.backend.entity.ExperienceHighlight;
import com.portfolio.backend.entity.User;
import com.portfolio.backend.exception.ResourceNotFoundException;
import com.portfolio.backend.mapper.ExperienceMapper;
import com.portfolio.backend.mapper.ExperienceHighlightMapper;
import com.portfolio.backend.repository.ExperienceRepository;
import com.portfolio.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final UserRepository userRepository;
    private final ExperienceMapper experienceMapper;
    private final ExperienceHighlightMapper highlightMapper;

    @Autowired
    public ExperienceService(ExperienceRepository experienceRepository, UserRepository userRepository,
                             ExperienceMapper experienceMapper, ExperienceHighlightMapper highlightMapper) {
        this.experienceRepository = experienceRepository;
        this.userRepository = userRepository;
        this.experienceMapper = experienceMapper;
        this.highlightMapper = highlightMapper;
    }

    // Helper method to find user by ID
    private User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    // Helper method to find experience by ID and user
    private Experience findExperienceByIdAndUser(Integer experienceId, User user) {
        return experienceRepository.findByIdAndUser(experienceId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found with id: " + experienceId + " for user: " + user.getId()));
    }

    @Transactional
    public ExperienceDTO createExperience(Integer userId, ExperienceDTO experienceDTO) {
        User user = findUserById(userId);

        Experience experience = experienceMapper.toEntity(experienceDTO);
        experience.setUser(user);

        // Handle highlights
        if (experienceDTO.getHighlights() != null && !experienceDTO.getHighlights().isEmpty()) {
            Set<ExperienceHighlight> highlights = experienceDTO.getHighlights().stream()
                    .map(dto -> {
                        ExperienceHighlight highlight = highlightMapper.toEntity(dto);
                        highlight.setExperience(experience); // Set parent experience
                        return highlight;
                    })
                    .collect(Collectors.toSet());
            experience.setHighlights(highlights);
        }

        Experience savedExperience = experienceRepository.save(experience);
        return experienceMapper.toDto(savedExperience);
    }

    @Transactional(readOnly = true)
    public List<ExperienceDTO> getAllExperienceByUserId(Integer userId) {
        User user = findUserById(userId);
        List<Experience> experienceList = experienceRepository.findByUser(user);
        return experienceList.stream()
                .map(experienceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ExperienceDTO getExperienceByIdForUser(Integer userId, Integer experienceId) {
        User user = findUserById(userId);
        Experience experience = findExperienceByIdAndUser(experienceId, user);
        return experienceMapper.toDto(experience);
    }

    @Transactional
    public ExperienceDTO updateExperience(Integer userId, Integer experienceId, ExperienceDTO experienceDTO) {
        User user = findUserById(userId);
        Experience existingExperience = findExperienceByIdAndUser(experienceId, user);

        experienceMapper.updateEntityFromDto(experienceDTO, existingExperience);

        // Handle highlights update
        if (experienceDTO.getHighlights() != null) {
            Set<ExperienceHighlight> currentHighlights = existingExperience.getHighlights();
            Set<ExperienceHighlightDTO> incomingHighlightDTOs = experienceDTO.getHighlights();

            // 1. Highlights to remove
            Set<ExperienceHighlight> highlightsToRemove = currentHighlights.stream()
                .filter(current -> incomingHighlightDTOs.stream().noneMatch(incoming -> incoming.getId() != null && incoming.getId().equals(current.getId())))
                .collect(Collectors.toSet());
            currentHighlights.removeAll(highlightsToRemove);

            // 2. Highlights to add or update
            for (ExperienceHighlightDTO incomingDto : incomingHighlightDTOs) {
                if (incomingDto.getId() == null) { // New highlight
                    ExperienceHighlight newHighlight = highlightMapper.toEntity(incomingDto);
                    newHighlight.setExperience(existingExperience);
                    currentHighlights.add(newHighlight);
                } else { // Existing highlight, find and update
                    currentHighlights.stream()
                        .filter(current -> current.getId().equals(incomingDto.getId()))
                        .findFirst()
                        .ifPresent(existingHighlight -> highlightMapper.updateEntityFromDto(incomingDto, existingHighlight));
                }
            }
        } else {
            // If incoming highlights are null, clear all existing highlights
            existingExperience.getHighlights().clear();
        }

        Experience updatedExperience = experienceRepository.save(existingExperience);
        return experienceMapper.toDto(updatedExperience);
    }

    @Transactional
    public void deleteExperience(Integer userId, Integer experienceId) {
        User user = findUserById(userId);
        Experience experience = findExperienceByIdAndUser(experienceId, user);
        experienceRepository.delete(experience);
    }
}