package com.portfolio.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TechnicalExpertiseDTO {
    private Integer id;
    private Integer userId; // To link with User (legacy)
    // New preferred lookup field: username
    private String username;
    @NotBlank(message = "Category cannot be empty")
    @Size(max = 50, message = "Category cannot exceed 50 characters")
    private String category;
    @NotBlank(message = "Skill cannot be empty")
    @Size(max = 100, message = "Skill cannot exceed 100 characters")
    private String skill;
}