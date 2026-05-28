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
@RequestMapping("/api/certifications")
public class CertificationController {

    private final CertificationService certificationService;

    @Autowired
    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @GetMapping
    public ResponseEntity<List<CertificationDTO>> getAllCertifications() {
        List<CertificationDTO> certifications = certificationService.getAllCertifications();
        return ResponseEntity.ok(certifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificationDTO> getCertificationById(@PathVariable("id") Integer id) {
        CertificationDTO certification = certificationService.getCertificationById(id);
        return ResponseEntity.ok(certification);
    }

    @PostMapping
    public ResponseEntity<CertificationDTO> createCertification(@Valid @RequestBody CertificationDTO certificationDTO) {
        CertificationDTO createdCertification = certificationService.createCertification(certificationDTO);
        return new ResponseEntity<>(createdCertification, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CertificationDTO> updateCertification(@PathVariable("id") Integer id, @Valid @RequestBody CertificationDTO certificationDTO) {
        CertificationDTO updatedCertification = certificationService.updateCertification(id, certificationDTO);
        return ResponseEntity.ok(updatedCertification);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertification(@PathVariable("id") Integer id) {
        certificationService.deleteCertification(id);
        return ResponseEntity.noContent().build();
    }
}