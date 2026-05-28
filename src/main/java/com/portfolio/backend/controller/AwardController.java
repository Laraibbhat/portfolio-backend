package com.portfolio.backend.controller;

import com.portfolio.backend.dto.AwardDTO;
import com.portfolio.backend.service.AwardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/awards")
public class AwardController {

    private final AwardService awardService;

    @Autowired
    public AwardController(AwardService awardService) {
        this.awardService = awardService;
    }

    @GetMapping
    public ResponseEntity<List<AwardDTO>> getAllAwards() {
        List<AwardDTO> awards = awardService.getAllAwards();
        return ResponseEntity.ok(awards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AwardDTO> getAwardById(@PathVariable("id") Integer id) {
        AwardDTO award = awardService.getAwardById(id);
        return ResponseEntity.ok(award);
    }

    @PostMapping
    public ResponseEntity<AwardDTO> createAward(@Valid @RequestBody AwardDTO awardDTO) {
        AwardDTO createdAward = awardService.createAward(awardDTO);
        return new ResponseEntity<>(createdAward, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AwardDTO> updateAward(@PathVariable("id") Integer id, @Valid @RequestBody AwardDTO awardDTO) {
        AwardDTO updatedAward = awardService.updateAward(id, awardDTO);
        return ResponseEntity.ok(updatedAward);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAward(@PathVariable("id") Integer id) {
        awardService.deleteAward(id);
        return ResponseEntity.noContent().build();
    }
}