package com.portfolio.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AwardDTO {
    private Integer id;
    private Integer userId; // To link with User (legacy)
    // New preferred lookup field: username
    private String username;
    @NotBlank(message = "Award text cannot be empty")
    private String awardText;
    private Integer displayOrder;
}