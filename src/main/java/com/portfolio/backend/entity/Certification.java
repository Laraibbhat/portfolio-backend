package com.portfolio.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "certifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private User user;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 100)
    private String issuer;

    @Column(name = "certification_date", length = 20)
    private String certificationDate;

    @Lob
    @Column(columnDefinition = "TEXT") // Explicitly define column type
    private String description;

    @Column(name = "display_order")
    private Integer displayOrder;
}