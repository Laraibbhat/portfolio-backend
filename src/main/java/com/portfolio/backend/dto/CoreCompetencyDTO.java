package com.portfolio.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CoreCompetencyDTO {
    private Integer id;
    private Integer userId; // To link with User (legacy)
    // New preferred lookup field: username
    private String username;
    @NotBlank(message = "Skill cannot be empty")
    @Size(max = 100, message = "Skill cannot exceed 100 characters")
    private String skill;
    @Size(max = 20, message = "Level cannot exceed 20 characters")
    private String level;
    private Integer displayOrder;
}