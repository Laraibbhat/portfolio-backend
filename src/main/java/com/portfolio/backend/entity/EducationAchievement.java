package com.portfolio.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "education_achievements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationAchievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_id", nullable = false)
    private Education education;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT") // Explicitly define column type
    private String achievement;

    @Column(name = "display_order")
    private Integer displayOrder;
}