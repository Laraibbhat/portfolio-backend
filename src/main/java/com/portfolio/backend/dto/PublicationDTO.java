package com.portfolio.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PublicationDTO {
    private Integer id;
    private Integer userId; // To link with User (legacy)
    // New preferred lookup field: username
    private String username;
    @NotBlank(message = "Title cannot be empty")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;
    @Size(max = 150, message = "Journal cannot exceed 150 characters")
    private String journal;
    @Size(max = 20, message = "Publication date cannot exceed 20 characters")
    private String publicationDate;
    private String description;
    private Integer displayOrder;
}