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
@RequestMapping("/api/education")
public class EducationController {

    private final EducationService educationService;

    @Autowired
    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @GetMapping
    public ResponseEntity<List<EducationDTO>> getAllEducation() {
        List<EducationDTO> education = educationService.getAllEducation();
        return ResponseEntity.ok(education);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EducationDTO> getEducationById(@PathVariable("id") Integer id) {
        EducationDTO education = educationService.getEducationById(id);
        return ResponseEntity.ok(education);
    }

    @PostMapping
    public ResponseEntity<EducationDTO> createEducation(@Valid @RequestBody EducationDTO educationDTO) {
        EducationDTO createdEducation = educationService.createEducation(educationDTO);
        return new ResponseEntity<>(createdEducation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationDTO> updateEducation(@PathVariable("id") Integer id, @Valid @RequestBody EducationDTO educationDTO) {
        EducationDTO updatedEducation = educationService.updateEducation(id, educationDTO);
        return ResponseEntity.ok(updatedEducation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducation(@PathVariable("id") Integer id) {
        educationService.deleteEducation(id);
        return ResponseEntity.noContent().build();
    }
}