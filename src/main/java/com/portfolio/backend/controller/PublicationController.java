package com.portfolio.backend.controller;

import com.portfolio.backend.dto.PublicationDTO;
import com.portfolio.backend.service.PublicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/publications")
public class PublicationController {

    private final PublicationService publicationService;

    @Autowired
    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @PostMapping
    public ResponseEntity<PublicationDTO> createPublication(
            @PathVariable Integer userId,
            @Valid @RequestBody PublicationDTO publicationDTO) {
        PublicationDTO createdPublication = publicationService.createPublication(userId, publicationDTO);
        return new ResponseEntity<>(createdPublication, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PublicationDTO>> getAllPublicationsByUserId(@PathVariable Integer userId) {
        List<PublicationDTO> publicationList = publicationService.getAllPublicationsByUserId(userId);
        return ResponseEntity.ok(publicationList);
    }

    @GetMapping("/{publicationId}")
    public ResponseEntity<PublicationDTO> getPublicationByIdForUser(
            @PathVariable Integer userId,
            @PathVariable Integer publicationId) {
        PublicationDTO publication = publicationService.getPublicationByIdForUser(userId, publicationId);
        return ResponseEntity.ok(publication);
    }

    @PutMapping("/{publicationId}")
    public ResponseEntity<PublicationDTO> updatePublication(
            @PathVariable Integer userId,
            @PathVariable Integer publicationId,
            @Valid @RequestBody PublicationDTO publicationDTO) {
        PublicationDTO updatedPublication = publicationService.updatePublication(userId, publicationId, publicationDTO);
        return ResponseEntity.ok(updatedPublication);
    }

    @DeleteMapping("/{publicationId}")
    public ResponseEntity<Void> deletePublication(
            @PathVariable Integer userId,
            @PathVariable Integer publicationId) {
        publicationService.deletePublication(userId, publicationId);
        return ResponseEntity.noContent().build();
    }
}