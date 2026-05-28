package com.portfolio.backend.service;

import com.portfolio.backend.dto.EducationAchievementDTO;
import com.portfolio.backend.entity.Education;
import com.portfolio.backend.entity.EducationAchievement;
import com.portfolio.backend.exception.ResourceNotFoundException;
import com.portfolio.backend.mapper.EducationAchievementMapper;
import com.portfolio.backend.repository.EducationAchievementRepository;
import com.portfolio.backend.repository.EducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EducationAchievementService {

    private final EducationAchievementRepository educationAchievementRepository;
    private final EducationRepository educationRepository;
    private final EducationAchievementMapper educationAchievementMapper;

    @Autowired
    public EducationAchievementService(EducationAchievementRepository educationAchievementRepository, EducationRepository educationRepository, EducationAchievementMapper educationAchievementMapper) {
        this.educationAchievementRepository = educationAchievementRepository;
        this.educationRepository = educationRepository;
        this.educationAchievementMapper = educationAchievementMapper;
    }

    public List<EducationAchievementDTO> getAllEducationAchievements() {
        return educationAchievementRepository.findAll().stream()
                .map(educationAchievementMapper::toDto)
                .collect(Collectors.toList());
    }

    public EducationAchievementDTO getEducationAchievementById(Integer id) {
        EducationAchievement educationAchievement = educationAchievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education Achievement not found with id: " + id));
        return educationAchievementMapper.toDto(educationAchievement);
    }

    @Transactional
    public EducationAchievementDTO createEducationAchievement(EducationAchievementDTO educationAchievementDTO) {
        Education education = educationRepository.findById(educationAchievementDTO.getEducationId())
                .orElseThrow(() -> new ResourceNotFoundException("Education not found with id: " + educationAchievementDTO.getEducationId()));
        EducationAchievement educationAchievement = educationAchievementMapper.toEntity(educationAchievementDTO);
        educationAchievement.setEducation(education);
        EducationAchievement savedEducationAchievement = educationAchievementRepository.save(educationAchievement);
        return educationAchievementMapper.toDto(savedEducationAchievement);
    }

    @Transactional
    public EducationAchievementDTO updateEducationAchievement(Integer id, EducationAchievementDTO educationAchievementDTO) {
        EducationAchievement existingEducationAchievement = educationAchievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education Achievement not found with id: " + id));

        educationAchievementMapper.updateEntityFromDto(educationAchievementDTO, existingEducationAchievement);
        if (educationAchievementDTO.getEducationId() != null && !existingEducationAchievement.getEducation().getId().equals(educationAchievementDTO.getEducationId())) {
            Education education = educationRepository.findById(educationAchievementDTO.getEducationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Education not found with id: " + educationAchievementDTO.getEducationId()));
            existingEducationAchievement.setEducation(education);
        }
        EducationAchievement updatedEducationAchievement = educationAchievementRepository.save(existingEducationAchievement);
        return educationAchievementMapper.toDto(updatedEducationAchievement);
    }

    @Transactional
    public void deleteEducationAchievement(Integer id) {
        if (!educationAchievementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Education Achievement not found with id: " + id);
        }
        educationAchievementRepository.deleteById(id);
    }
}