package com.portfolio.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class UserRequestDTO {
    @NotBlank(message = "Username cannot be empty")
    @Size(max = 50, message = "Username cannot exceed 50 characters")
    private String username;

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Size(max = 20, message = "Phone cannot exceed 20 characters")
    private String phone;

    @Size(max = 100, message = "Location cannot exceed 100 characters")
    private String location;

    private String summary;

    private BigDecimal experienceYears;
    private BigDecimal yearsAsSenior;
    private Integer projectsDelivered;
    private Integer cloudInfraProjects;
    private Integer teamsManagedInfra;
    private Integer performanceOptimization;

    private Set<TechnicalExpertiseDTO> technicalExpertise;
    private Set<ExperienceDTO> experiences;
    private Set<EducationDTO> education;
    private Set<CertificationDTO> certifications;
    private Set<PublicationDTO> publications;
    private Set<AwardDTO> awards;
    private Set<CoreCompetencyDTO> coreCompetencies;

    private String avatarKey;
    private String avatarUrl;
}