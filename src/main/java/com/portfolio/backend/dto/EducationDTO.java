package com.portfolio.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Set;

@Data
public class EducationDTO {
    private Integer id;
    private Integer userId; // To link with User (legacy)
    // New preferred lookup field: username
    private String username;
    @NotBlank(message = "Degree cannot be empty")
    @Size(max = 100, message = "Degree cannot exceed 100 characters")
    private String degree;
    @NotBlank(message = "School cannot be empty")
    @Size(max = 150, message = "School cannot exceed 150 characters")
    private String school;
    @Size(max = 20, message = "Year cannot exceed 20 characters")
    private String year;
    private String details;
    private Integer displayOrder;
    private Set<EducationAchievementDTO> achievements;
}