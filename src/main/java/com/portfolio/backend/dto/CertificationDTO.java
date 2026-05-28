package com.portfolio.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CertificationDTO {
    private Integer id;
    private Integer userId; // To link with User (legacy)
    // New preferred lookup field: username
    private String username;
    @NotBlank(message = "Name cannot be empty")
    @Size(max = 150, message = "Name cannot exceed 150 characters")
    private String name;
    @Size(max = 100, message = "Issuer cannot exceed 100 characters")
    private String issuer;
    @Size(max = 20, message = "Certification date cannot exceed 20 characters")
    private String certificationDate;
    private String description;
    private Integer displayOrder;
}