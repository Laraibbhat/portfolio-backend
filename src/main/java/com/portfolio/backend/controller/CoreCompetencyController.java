package com.portfolio.backend.controller;

import com.portfolio.backend.dto.CoreCompetencyDTO;
import com.portfolio.backend.service.CoreCompetencyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/core-competencies")
public class CoreCompetencyController {

    private final CoreCompetencyService coreCompetencyService;

    @Autowired
    public CoreCompetencyController(CoreCompetencyService coreCompetencyService) {
        this.coreCompetencyService = coreCompetencyService;
    }

    @PostMapping
    public ResponseEntity<CoreCompetencyDTO> createCoreCompetency(
            @PathVariable Integer userId,
            @Valid @RequestBody CoreCompetencyDTO coreCompetencyDTO) {
        CoreCompetencyDTO createdCompetency = coreCompetencyService.createCoreCompetency(userId, coreCompetencyDTO);
        return new ResponseEntity<>(createdCompetency, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CoreCompetencyDTO>> getAllCoreCompetenciesByUserId(@PathVariable Integer userId) {
        List<CoreCompetencyDTO> competencyList = coreCompetencyService.getAllCoreCompetenciesByUserId(userId);
        return ResponseEntity.ok(competencyList);
    }

    @GetMapping("/{competencyId}")
    public ResponseEntity<CoreCompetencyDTO> getCoreCompetencyByIdForUser(
            @PathVariable Integer userId,
            @PathVariable Integer competencyId) {
        CoreCompetencyDTO competency = coreCompetencyService.getCoreCompetencyByIdForUser(userId, competencyId);
        return ResponseEntity.ok(competency);
    }

    @PutMapping("/{competencyId}")
    public ResponseEntity<CoreCompetencyDTO> updateCoreCompetency(
            @PathVariable Integer userId,
            @PathVariable Integer competencyId,
            @Valid @RequestBody CoreCompetencyDTO coreCompetencyDTO) {
        CoreCompetencyDTO updatedCompetency = coreCompetencyService.updateCoreCompetency(userId, competencyId, coreCompetencyDTO);
        return ResponseEntity.ok(updatedCompetency);
    }

    @DeleteMapping("/{competencyId}")
    public ResponseEntity<Void> deleteCoreCompetency(
            @PathVariable Integer userId,
            @PathVariable Integer competencyId) {
        coreCompetencyService.deleteCoreCompetency(userId, competencyId);
        return ResponseEntity.noContent().build();
    }
}