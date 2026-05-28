package com.portfolio.backend.service;

import com.portfolio.backend.dto.EducationDTO;
import com.portfolio.backend.entity.Education;
import com.portfolio.backend.entity.User;
import com.portfolio.backend.exception.ResourceNotFoundException;
import com.portfolio.backend.mapper.EducationMapper;
import com.portfolio.backend.repository.EducationRepository;
import com.portfolio.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EducationService {

    private final EducationRepository educationRepository;
    private final UserRepository userRepository;
    private final EducationMapper educationMapper;

    @Autowired
    public EducationService(EducationRepository educationRepository, UserRepository userRepository, EducationMapper educationMapper) {
        this.educationRepository = educationRepository;
        this.userRepository = userRepository;
        this.educationMapper = educationMapper;
    }

    public List<EducationDTO> getAllEducation() {
        return educationRepository.findAll().stream()
                .map(educationMapper::toDto)
                .collect(Collectors.toList());
    }

    public EducationDTO getEducationById(Integer id) {
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found with id: " + id));
        return educationMapper.toDto(education);
    }

    @Transactional
    public EducationDTO createEducation(EducationDTO educationDTO) {
        String username = educationDTO.getUsername();
        Integer userId = educationDTO.getUserId();
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
        Education education = educationMapper.toEntity(educationDTO);
        education.setUser(user);
        Education savedEducation = educationRepository.save(education);
        return educationMapper.toDto(savedEducation);
    }

    @Transactional
    public EducationDTO updateEducation(Integer id, EducationDTO educationDTO) {
        Education existingEducation = educationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found with id: " + id));

        educationMapper.updateEntityFromDto(educationDTO, existingEducation);
        if (educationDTO.getUsername() != null && !existingEducation.getUser().getUsername().equals(educationDTO.getUsername())) {
            User user = userRepository.findByUsername(educationDTO.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + educationDTO.getUsername()));
            existingEducation.setUser(user);
        } else if (educationDTO.getUserId() != null && !existingEducation.getUser().getId().equals(educationDTO.getUserId())) {
            User user = userRepository.findById(educationDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + educationDTO.getUserId()));
            existingEducation.setUser(user);
        }
        Education updatedEducation = educationRepository.save(existingEducation);
        return educationMapper.toDto(updatedEducation);
    }

    @Transactional
    public void deleteEducation(Integer id) {
        if (!educationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Education not found with id: " + id);
        }
        educationRepository.deleteById(id);
    }
}