package com.portfolio.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String title;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String location;

    @Lob
    @Column(columnDefinition = "LONGTEXT") // Explicitly define column type to match database
    private String summary;

    @Column(name = "experience_years", precision = 3, scale = 1)
    private BigDecimal experienceYears;

    @Column(name = "years_as_senior", precision = 3, scale = 1)
    private BigDecimal yearsAsSenior;

    @Column(name = "projects_delivered")
    private Integer projectsDelivered;

    @Column(name = "cloud_infra_projects")
    private Integer cloudInfraProjects;

    @Column(name = "teams_managed_infra")
    private Integer teamsManagedInfra;

    @Column(name = "performance_optimization")
    private Integer performanceOptimization;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Relationships - Using EAGER fetch to load all related data when User is fetched
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<TechnicalExpertise> technicalExpertise;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Experience> experiences;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Education> education;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Certification> certifications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Publication> publications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Award> awards;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CoreCompetency> coreCompetencies;
}