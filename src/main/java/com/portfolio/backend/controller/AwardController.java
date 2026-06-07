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
@RequestMapping("/api/users/{userId}/awards")
public class AwardController {

    private final AwardService awardService;

    @Autowired
    public AwardController(AwardService awardService) {
        this.awardService = awardService;
    }

    @PostMapping
    public ResponseEntity<AwardDTO> createAward(
            @PathVariable Integer userId,
            @Valid @RequestBody AwardDTO awardDTO) {
        AwardDTO createdAward = awardService.createAward(userId, awardDTO);
        return new ResponseEntity<>(createdAward, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AwardDTO>> getAllAwardsByUserId(@PathVariable Integer userId) {
        List<AwardDTO> awardList = awardService.getAllAwardsByUserId(userId);
        return ResponseEntity.ok(awardList);
    }

    @GetMapping("/{awardId}")
    public ResponseEntity<AwardDTO> getAwardByIdForUser(
            @PathVariable Integer userId,
            @PathVariable Integer awardId) {
        AwardDTO award = awardService.getAwardByIdForUser(userId, awardId);
        return ResponseEntity.ok(award);
    }

    @PutMapping("/{awardId}")
    public ResponseEntity<AwardDTO> updateAward(
            @PathVariable Integer userId,
            @PathVariable Integer awardId,
            @Valid @RequestBody AwardDTO awardDTO) {
        AwardDTO updatedAward = awardService.updateAward(userId, awardId, awardDTO);
        return ResponseEntity.ok(updatedAward);
    }

    @DeleteMapping("/{awardId}")
    public ResponseEntity<Void> deleteAward(
            @PathVariable Integer userId,
            @PathVariable Integer awardId) {
        awardService.deleteAward(userId, awardId);
        return ResponseEntity.noContent().build();
    }
}