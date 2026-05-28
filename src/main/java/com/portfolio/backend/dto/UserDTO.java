package com.portfolio.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String name;
    private String title;
    private String email;
    private String phone;
    private String location;
    private String summary;
    private BigDecimal experienceYears;
    private BigDecimal yearsAsSenior;
    private Integer projectsDelivered;
    private Integer cloudInfraProjects;
    private Integer teamsManagedInfra;
    private Integer performanceOptimization;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<TechnicalExpertiseDTO> technicalExpertise;
    private Set<ExperienceDTO> experiences;
    private Set<EducationDTO> education;
    private Set<CertificationDTO> certifications;
    private Set<PublicationDTO> publications;
    private Set<AwardDTO> awards;
    private Set<CoreCompetencyDTO> coreCompetencies;
}