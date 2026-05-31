package com.portfolio.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "core_competencies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoreCompetency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private User user;

    @Column(nullable = false, length = 100)
    private String skill;

    @Column(length = 20)
    private String level;

    @Column(name = "display_order")
    private Integer displayOrder;
}