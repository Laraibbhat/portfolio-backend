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
@RequestMapping("/api/core-competencies")
public class CoreCompetencyController {

    private final CoreCompetencyService coreCompetencyService;

    @Autowired
    public CoreCompetencyController(CoreCompetencyService coreCompetencyService) {
        this.coreCompetencyService = coreCompetencyService;
    }

    @GetMapping
    public ResponseEntity<List<CoreCompetencyDTO>> getAllCoreCompetencies() {
        List<CoreCompetencyDTO> coreCompetencies = coreCompetencyService.getAllCoreCompetencies();
        return ResponseEntity.ok(coreCompetencies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoreCompetencyDTO> getCoreCompetencyById(@PathVariable("id") Integer id) {
        CoreCompetencyDTO coreCompetency = coreCompetencyService.getCoreCompetencyById(id);
        return ResponseEntity.ok(coreCompetency);
    }

    @PostMapping
    public ResponseEntity<CoreCompetencyDTO> createCoreCompetency(@Valid @RequestBody CoreCompetencyDTO coreCompetencyDTO) {
        CoreCompetencyDTO createdCoreCompetency = coreCompetencyService.createCoreCompetency(coreCompetencyDTO);
        return new ResponseEntity<>(createdCoreCompetency, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoreCompetencyDTO> updateCoreCompetency(@PathVariable("id") Integer id, @Valid @RequestBody CoreCompetencyDTO coreCompetencyDTO) {
        CoreCompetencyDTO updatedCoreCompetency = coreCompetencyService.updateCoreCompetency(id, coreCompetencyDTO);
        return ResponseEntity.ok(updatedCoreCompetency);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoreCompetency(@PathVariable("id") Integer id) {
        coreCompetencyService.deleteCoreCompetency(id);
        return ResponseEntity.noContent().build();
    }
}