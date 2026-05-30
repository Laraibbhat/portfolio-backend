package com.portfolio.backend.controller;

import com.portfolio.backend.dto.CertificationDTO;
import com.portfolio.backend.service.CertificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/certifications")
public class CertificationController {

    private final CertificationService certificationService;

    @Autowired
    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @PostMapping
    public ResponseEntity<CertificationDTO> createCertification(
            @PathVariable Integer userId,
            @Valid @RequestBody CertificationDTO certificationDTO) {
        CertificationDTO createdCertification = certificationService.createCertification(userId, certificationDTO);
        return new ResponseEntity<>(createdCertification, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CertificationDTO>> getAllCertificationsByUserId(@PathVariable Integer userId) {
        List<CertificationDTO> certificationList = certificationService.getAllCertificationsByUserId(userId);
        return ResponseEntity.ok(certificationList);
    }

    @GetMapping("/{certificationId}")
    public ResponseEntity<CertificationDTO> getCertificationByIdForUser(
            @PathVariable Integer userId,
            @PathVariable Integer certificationId) {
        CertificationDTO certification = certificationService.getCertificationByIdForUser(userId, certificationId);
        return ResponseEntity.ok(certification);
    }

    @PutMapping("/{certificationId}")
    public ResponseEntity<CertificationDTO> updateCertification(
            @PathVariable Integer userId,
            @PathVariable Integer certificationId,
            @Valid @RequestBody CertificationDTO certificationDTO) {
        CertificationDTO updatedCertification = certificationService.updateCertification(userId, certificationId, certificationDTO);
        return ResponseEntity.ok(updatedCertification);
    }

    @DeleteMapping("/{certificationId}")
    public ResponseEntity<Void> deleteCertification(
            @PathVariable Integer userId,
            @PathVariable Integer certificationId) {
        certificationService.deleteCertification(userId, certificationId);
        return ResponseEntity.noContent().build();
    }
}