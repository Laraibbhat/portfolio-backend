package com.portfolio.backend.service;

import com.portfolio.backend.dto.ExperienceHighlightDTO;
import com.portfolio.backend.entity.Experience;
import com.portfolio.backend.entity.ExperienceHighlight;
import com.portfolio.backend.exception.ResourceNotFoundException;
import com.portfolio.backend.mapper.ExperienceHighlightMapper;
import com.portfolio.backend.repository.ExperienceHighlightRepository;
import com.portfolio.backend.repository.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExperienceHighlightService {

    private final ExperienceHighlightRepository experienceHighlightRepository;
    private final ExperienceRepository experienceRepository;
    private final ExperienceHighlightMapper experienceHighlightMapper;

    @Autowired
    public ExperienceHighlightService(ExperienceHighlightRepository experienceHighlightRepository, ExperienceRepository experienceRepository, ExperienceHighlightMapper experienceHighlightMapper) {
        this.experienceHighlightRepository = experienceHighlightRepository;
        this.experienceRepository = experienceRepository;
        this.experienceHighlightMapper = experienceHighlightMapper;
    }

    public List<ExperienceHighlightDTO> getAllExperienceHighlights() {
        return experienceHighlightRepository.findAll().stream()
                .map(experienceHighlightMapper::toDto)
                .collect(Collectors.toList());
    }

    public ExperienceHighlightDTO getExperienceHighlightById(Integer id) {
        ExperienceHighlight experienceHighlight = experienceHighlightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience Highlight not found with id: " + id));
        return experienceHighlightMapper.toDto(experienceHighlight);
    }

    @Transactional
    public ExperienceHighlightDTO createExperienceHighlight(ExperienceHighlightDTO experienceHighlightDTO) {
        Experience experience = experienceRepository.findById(experienceHighlightDTO.getExperienceId())
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found with id: " + experienceHighlightDTO.getExperienceId()));
        ExperienceHighlight experienceHighlight = experienceHighlightMapper.toEntity(experienceHighlightDTO);
        experienceHighlight.setExperience(experience);
        ExperienceHighlight savedExperienceHighlight = experienceHighlightRepository.save(experienceHighlight);
        return experienceHighlightMapper.toDto(savedExperienceHighlight);
    }

    @Transactional
    public ExperienceHighlightDTO updateExperienceHighlight(Integer id, ExperienceHighlightDTO experienceHighlightDTO) {
        ExperienceHighlight existingExperienceHighlight = experienceHighlightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience Highlight not found with id: " + id));

        experienceHighlightMapper.updateEntityFromDto(experienceHighlightDTO, existingExperienceHighlight);
        if (experienceHighlightDTO.getExperienceId() != null && !existingExperienceHighlight.getExperience().getId().equals(experienceHighlightDTO.getExperienceId())) {
            Experience experience = experienceRepository.findById(experienceHighlightDTO.getExperienceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Experience not found with id: " + experienceHighlightDTO.getExperienceId()));
            existingExperienceHighlight.setExperience(experience);
        }
        ExperienceHighlight updatedExperienceHighlight = experienceHighlightRepository.save(existingExperienceHighlight);
        return experienceHighlightMapper.toDto(updatedExperienceHighlight);
    }

    @Transactional
    public void deleteExperienceHighlight(Integer id) {
        if (!experienceHighlightRepository.existsById(id)) {
            throw new ResourceNotFoundException("Experience Highlight not found with id: " + id);
        }
        experienceHighlightRepository.deleteById(id);
    }
}