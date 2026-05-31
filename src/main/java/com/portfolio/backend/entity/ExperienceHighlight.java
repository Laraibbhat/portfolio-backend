package com.portfolio.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Experience experience;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT") // Explicitly define column type
    private String highlight;

    @Column(name = "display_order")
    private Integer displayOrder;
}