package com.portfolio.backend.controller;

import com.portfolio.backend.dto.ExperienceHighlightDTO;
import com.portfolio.backend.service.ExperienceHighlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experience-highlights")
public class ExperienceHighlightController {

    private final ExperienceHighlightService experienceHighlightService;

    @Autowired
    public ExperienceHighlightController(ExperienceHighlightService experienceHighlightService) {
        this.experienceHighlightService = experienceHighlightService;
    }

    @GetMapping
    public ResponseEntity<List<ExperienceHighlightDTO>> getAllExperienceHighlights() {
        List<ExperienceHighlightDTO> experienceHighlights = experienceHighlightService.getAllExperienceHighlights();
        return ResponseEntity.ok(experienceHighlights);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceHighlightDTO> getExperienceHighlightById(@PathVariable("id") Integer id) {
        ExperienceHighlightDTO experienceHighlight = experienceHighlightService.getExperienceHighlightById(id);
        return ResponseEntity.ok(experienceHighlight);
    }

    @PostMapping
    public ResponseEntity<ExperienceHighlightDTO> createExperienceHighlight(@Valid @RequestBody ExperienceHighlightDTO experienceHighlightDTO) {
        ExperienceHighlightDTO createdExperienceHighlight = experienceHighlightService.createExperienceHighlight(experienceHighlightDTO);
        return new ResponseEntity<>(createdExperienceHighlight, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExperienceHighlightDTO> updateExperienceHighlight(@PathVariable("id") Integer id, @Valid @RequestBody ExperienceHighlightDTO experienceHighlightDTO) {
        ExperienceHighlightDTO updatedExperienceHighlight = experienceHighlightService.updateExperienceHighlight(id, experienceHighlightDTO);
        return ResponseEntity.ok(updatedExperienceHighlight);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperienceHighlight(@PathVariable("id") Integer id) {
        experienceHighlightService.deleteExperienceHighlight(id);
        return ResponseEntity.noContent().build();
    }
}