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
@RequestMapping("/api/technical-expertise")
public class TechnicalExpertiseController {

    private final TechnicalExpertiseService technicalExpertiseService;

    @Autowired
    public TechnicalExpertiseController(TechnicalExpertiseService technicalExpertiseService) {
        this.technicalExpertiseService = technicalExpertiseService;
    }

    @GetMapping
    public ResponseEntity<List<TechnicalExpertiseDTO>> getAllTechnicalExpertise() {
        List<TechnicalExpertiseDTO> technicalExpertise = technicalExpertiseService.getAllTechnicalExpertise();
        return ResponseEntity.ok(technicalExpertise);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnicalExpertiseDTO> getTechnicalExpertiseById(@PathVariable("id") Integer id) {
        TechnicalExpertiseDTO technicalExpertise = technicalExpertiseService.getTechnicalExpertiseById(id);
        return ResponseEntity.ok(technicalExpertise);
    }

    @PostMapping
    public ResponseEntity<TechnicalExpertiseDTO> createTechnicalExpertise(@Valid @RequestBody TechnicalExpertiseDTO technicalExpertiseDTO) {
        TechnicalExpertiseDTO createdTechnicalExpertise = technicalExpertiseService.createTechnicalExpertise(technicalExpertiseDTO);
        return new ResponseEntity<>(createdTechnicalExpertise, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TechnicalExpertiseDTO> updateTechnicalExpertise(@PathVariable("id") Integer id, @Valid @RequestBody TechnicalExpertiseDTO technicalExpertiseDTO) {
        TechnicalExpertiseDTO updatedTechnicalExpertise = technicalExpertiseService.updateTechnicalExpertise(id, technicalExpertiseDTO);
        return ResponseEntity.ok(updatedTechnicalExpertise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnicalExpertise(@PathVariable("id") Integer id) {
        technicalExpertiseService.deleteTechnicalExpertise(id);
        return ResponseEntity.noContent().build();
    }
}