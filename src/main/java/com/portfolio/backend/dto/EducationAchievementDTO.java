package com.portfolio.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EducationAchievementDTO {
    private Integer id;
    private Integer educationId; // To link with Education
    @NotBlank(message = "Achievement cannot be empty")
    private String achievement;
    private Integer displayOrder;
}