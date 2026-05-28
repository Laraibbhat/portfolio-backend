package com.portfolio.backend.service;

import com.portfolio.backend.dto.ExperienceDTO;
import com.portfolio.backend.entity.Experience;
import com.portfolio.backend.entity.User;
import com.portfolio.backend.exception.ResourceNotFoundException;
import com.portfolio.backend.mapper.ExperienceMapper;
import com.portfolio.backend.repository.ExperienceRepository;
import com.portfolio.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final UserRepository userRepository;
    private final ExperienceMapper experienceMapper;

    @Autowired
    public ExperienceService(ExperienceRepository experienceRepository, UserRepository userRepository, ExperienceMapper experienceMapper) {
        this.experienceRepository = experienceRepository;
        this.userRepository = userRepository;
        this.experienceMapper = experienceMapper;
    }

    public List<ExperienceDTO> getAllExperiences() {
        return experienceRepository.findAll().stream()
                .map(experienceMapper::toDto)
                .collect(Collectors.toList());
    }

    public ExperienceDTO getExperienceById(Integer id) {
        Experience experience = experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found with id: " + id));
        return experienceMapper.toDto(experience);
    }

    @Transactional
    public ExperienceDTO createExperience(ExperienceDTO experienceDTO) {
        String username = experienceDTO.getUsername();
        Integer userId = experienceDTO.getUserId();
        User user;
        if (username != null && !username.trim().isEmpty()) {
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        } else if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        } else {
            throw new IllegalArgumentException("Either username or userId must be provided");
        }
        Experience experience = experienceMapper.toEntity(experienceDTO);
        experience.setUser(user);
        Experience savedExperience = experienceRepository.save(experience);
        return experienceMapper.toDto(savedExperience);
    }

    @Transactional
    public ExperienceDTO updateExperience(Integer id, ExperienceDTO experienceDTO) {
        Experience existingExperience = experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found with id: " + id));

        experienceMapper.updateEntityFromDto(experienceDTO, existingExperience);
        if (experienceDTO.getUsername() != null && !existingExperience.getUser().getUsername().equals(experienceDTO.getUsername())) {
            User user = userRepository.findByUsername(experienceDTO.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + experienceDTO.getUsername()));
            existingExperience.setUser(user);
        } else if (experienceDTO.getUserId() != null && !existingExperience.getUser().getId().equals(experienceDTO.getUserId())) {
            User user = userRepository.findById(experienceDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + experienceDTO.getUserId()));
            existingExperience.setUser(user);
        }
        Experience updatedExperience = experienceRepository.save(existingExperience);
        return experienceMapper.toDto(updatedExperience);
    }

    @Transactional
    public void deleteExperience(Integer id) {
        if (!experienceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Experience not found with id: " + id);
        }
        experienceRepository.deleteById(id);
    }
}