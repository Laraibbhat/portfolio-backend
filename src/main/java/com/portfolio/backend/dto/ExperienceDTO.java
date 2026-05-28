package com.portfolio.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Set;

@Data
public class ExperienceDTO {
    private Integer id;
    private Integer userId; // To link with User (legacy)
    // New preferred lookup field: username
    private String username;
    @NotBlank(message = "Title cannot be empty")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;
    @NotBlank(message = "Company cannot be empty")
    @Size(max = 100, message = "Company cannot exceed 100 characters")
    private String company;
    @Size(max = 50, message = "Period cannot exceed 50 characters")
    private String period;
    @Size(max = 200, message = "Project focus cannot exceed 200 characters")
    private String projectFocus;
    private Integer displayOrder;
    private Set<ExperienceHighlightDTO> highlights;
}