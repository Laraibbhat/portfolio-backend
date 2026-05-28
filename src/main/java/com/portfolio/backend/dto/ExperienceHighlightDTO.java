package com.portfolio.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ExperienceHighlightDTO {
    private Integer id;
    private Integer experienceId; // To link with Experience
    @NotBlank(message = "Highlight cannot be empty")
    private String highlight;
    private Integer displayOrder;
}