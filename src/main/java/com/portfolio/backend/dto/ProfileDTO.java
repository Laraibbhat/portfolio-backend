package com.portfolio.backend.dto;

import lombok.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {
   private String username;
   private String name;
   private String title;
   private String email;
   private String phone;
   private String location;
   private String avatarKey;
   private String avatarUrl;
   private String summary;
   
   private Double experienceYears;
   private Double yearsAsSenior;
   private Integer projectsDelivered;
   private Integer cloudInfraProjects;
   private Integer teamsManagedInfra;
   private Integer performanceOptimization;
   
   private Map<String, List<String>> technicalExpertise;
   private List<ExperienceDTO> experiences;
   private List<EducationDTO> educations;
   private List<CertificationDTO> certifications;
   private List<PublicationDTO> publications;
   private List<String> awards;
   private List<CoreCompetencyDTO> coreCompetencies; // Corrected from CompetencyDTO
}