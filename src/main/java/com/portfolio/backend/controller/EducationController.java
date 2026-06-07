package com.portfolio.backend.controller;

import com.portfolio.backend.dto.EducationDTO;
import com.portfolio.backend.service.EducationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/education")
public class EducationController {

    private final EducationService educationService;

    @Autowired
    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @PostMapping
    public ResponseEntity<EducationDTO> createEducation(
            @PathVariable Integer userId,
            @Valid @RequestBody EducationDTO educationDTO) {
        EducationDTO createdEducation = educationService.createEducation(userId, educationDTO);
        return new ResponseEntity<>(createdEducation, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EducationDTO>> getAllEducationByUserId(@PathVariable Integer userId) {
        List<EducationDTO> educationList = educationService.getAllEducationByUserId(userId);
        return ResponseEntity.ok(educationList);
    }

    @GetMapping("/{educationId}")
    public ResponseEntity<EducationDTO> getEducationByIdForUser(
            @PathVariable Integer userId,
            @PathVariable Integer educationId) {
        EducationDTO education = educationService.getEducationByIdForUser(userId, educationId);
        return ResponseEntity.ok(education);
    }

    @PutMapping("/{educationId}")
    public ResponseEntity<EducationDTO> updateEducation(
            @PathVariable Integer userId,
            @PathVariable Integer educationId,
            @Valid @RequestBody EducationDTO educationDTO) {
        EducationDTO updatedEducation = educationService.updateEducation(userId, educationId, educationDTO);
        return ResponseEntity.ok(updatedEducation);
    }

    @DeleteMapping("/{educationId}")
    public ResponseEntity<Void> deleteEducation(
            @PathVariable Integer userId,
            @PathVariable Integer educationId) {
        educationService.deleteEducation(userId, educationId);
        return ResponseEntity.noContent().build();
    }
}