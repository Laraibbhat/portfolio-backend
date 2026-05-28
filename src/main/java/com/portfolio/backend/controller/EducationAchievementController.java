package com.portfolio.backend.controller;

import com.portfolio.backend.dto.EducationAchievementDTO;
import com.portfolio.backend.service.EducationAchievementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/education-achievements")
public class EducationAchievementController {

    private final EducationAchievementService educationAchievementService;

    @Autowired
    public EducationAchievementController(EducationAchievementService educationAchievementService) {
        this.educationAchievementService = educationAchievementService;
    }

    @GetMapping
    public ResponseEntity<List<EducationAchievementDTO>> getAllEducationAchievements() {
        List<EducationAchievementDTO> educationAchievements = educationAchievementService.getAllEducationAchievements();
        return ResponseEntity.ok(educationAchievements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EducationAchievementDTO> getEducationAchievementById(@PathVariable("id") Integer id) {
        EducationAchievementDTO educationAchievement = educationAchievementService.getEducationAchievementById(id);
        return ResponseEntity.ok(educationAchievement);
    }

    @PostMapping
    public ResponseEntity<EducationAchievementDTO> createEducationAchievement(@Valid @RequestBody EducationAchievementDTO educationAchievementDTO) {
        EducationAchievementDTO createdEducationAchievement = educationAchievementService.createEducationAchievement(educationAchievementDTO);
        return new ResponseEntity<>(createdEducationAchievement, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationAchievementDTO> updateEducationAchievement(@PathVariable("id") Integer id, @Valid @RequestBody EducationAchievementDTO educationAchievementDTO) {
        EducationAchievementDTO updatedEducationAchievement = educationAchievementService.updateEducationAchievement(id, educationAchievementDTO);
        return ResponseEntity.ok(updatedEducationAchievement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducationAchievement(@PathVariable("id") Integer id) {
        educationAchievementService.deleteEducationAchievement(id);
        return ResponseEntity.noContent().build();
    }
}