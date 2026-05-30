package com.portfolio.backend.controller;

import com.portfolio.backend.dto.ExperienceDTO;
import com.portfolio.backend.service.ExperienceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/experiences")
public class ExperienceController {

    private final ExperienceService experienceService;

    @Autowired
    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @PostMapping
    public ResponseEntity<ExperienceDTO> createExperience(
            @PathVariable Integer userId,
            @Valid @RequestBody ExperienceDTO experienceDTO) {
        ExperienceDTO createdExperience = experienceService.createExperience(userId, experienceDTO);
        return new ResponseEntity<>(createdExperience, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExperienceDTO>> getAllExperienceByUserId(@PathVariable Integer userId) {
        List<ExperienceDTO> experienceList = experienceService.getAllExperienceByUserId(userId);
        return ResponseEntity.ok(experienceList);
    }

    @GetMapping("/{experienceId}")
    public ResponseEntity<ExperienceDTO> getExperienceByIdForUser(
            @PathVariable Integer userId,
            @PathVariable Integer experienceId) {
        ExperienceDTO experience = experienceService.getExperienceByIdForUser(userId, experienceId);
        return ResponseEntity.ok(experience);
    }

    @PutMapping("/{experienceId}")
    public ResponseEntity<ExperienceDTO> updateExperience(
            @PathVariable Integer userId,
            @PathVariable Integer experienceId,
            @Valid @RequestBody ExperienceDTO experienceDTO) {
        ExperienceDTO updatedExperience = experienceService.updateExperience(userId, experienceId, experienceDTO);
        return ResponseEntity.ok(updatedExperience);
    }

    @DeleteMapping("/{experienceId}")
    public ResponseEntity<Void> deleteExperience(
            @PathVariable Integer userId,
            @PathVariable Integer experienceId) {
        experienceService.deleteExperience(userId, experienceId);
        return ResponseEntity.noContent().build();
    }
}