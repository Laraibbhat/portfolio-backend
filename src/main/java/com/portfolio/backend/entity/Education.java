package com.portfolio.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "education")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String degree;

    @Column(nullable = false, length = 150)
    private String school;

    @Column(length = 20)
    private String year;

    @Lob
    @Column(columnDefinition = "TEXT") // Explicitly define column type
    private String details;

    @Column(name = "display_order")
    private Integer displayOrder;

    @OneToMany(mappedBy = "education", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EducationAchievement> achievements;
}