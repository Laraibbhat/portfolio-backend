package com.portfolio.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "experience_highlights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperienceHighlight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experience_id", nullable = false)
    private Experience experience;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT") // Explicitly define column type
    private String highlight;

    @Column(name = "display_order")
    private Integer displayOrder;
}