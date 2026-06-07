package com.portfolio.backend.controller;

import com.portfolio.backend.dto.TechnicalExpertiseDTO;
import com.portfolio.backend.service.TechnicalExpertiseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/technical-expertise")
public class TechnicalExpertiseController {

    private final TechnicalExpertiseService technicalExpertiseService;

    @Autowired
    public TechnicalExpertiseController(TechnicalExpertiseService technicalExpertiseService) {
        this.technicalExpertiseService = technicalExpertiseService;
    }

    @PostMapping
    public ResponseEntity<TechnicalExpertiseDTO> createTechnicalExpertise(
            @PathVariable Integer userId,
            @Valid @RequestBody TechnicalExpertiseDTO technicalExpertiseDTO) {
        TechnicalExpertiseDTO createdExpertise = technicalExpertiseService.createTechnicalExpertise(userId, technicalExpertiseDTO);
        return new ResponseEntity<>(createdExpertise, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TechnicalExpertiseDTO>> getAllTechnicalExpertiseByUserId(@PathVariable Integer userId) {
        List<TechnicalExpertiseDTO> expertiseList = technicalExpertiseService.getAllTechnicalExpertiseByUserId(userId);
        return ResponseEntity.ok(expertiseList);
    }

    @GetMapping("/{expertiseId}")
    public ResponseEntity<TechnicalExpertiseDTO> getTechnicalExpertiseByIdForUser(
            @PathVariable Integer userId,
            @PathVariable Integer expertiseId) {
        TechnicalExpertiseDTO expertise = technicalExpertiseService.getTechnicalExpertiseByIdForUser(userId, expertiseId);
        return ResponseEntity.ok(expertise);
    }

    @PutMapping("/{expertiseId}")
    public ResponseEntity<TechnicalExpertiseDTO> updateTechnicalExpertise(
            @PathVariable Integer userId,
            @PathVariable Integer expertiseId,
            @Valid @RequestBody TechnicalExpertiseDTO technicalExpertiseDTO) {
        TechnicalExpertiseDTO updatedExpertise = technicalExpertiseService.updateTechnicalExpertise(userId, expertiseId, technicalExpertiseDTO);
        return ResponseEntity.ok(updatedExpertise);
    }

    @DeleteMapping("/{expertiseId}")
    public ResponseEntity<Void> deleteTechnicalExpertise(
            @PathVariable Integer userId,
            @PathVariable Integer expertiseId) {
        technicalExpertiseService.deleteTechnicalExpertise(userId, expertiseId);
        return ResponseEntity.noContent().build();
    }
}