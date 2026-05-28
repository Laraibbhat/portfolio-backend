package com.portfolio.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "publications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 150)
    private String journal;

    @Column(name = "publication_date", length = 20)
    private String publicationDate;

    @Lob
    @Column(columnDefinition = "TEXT") // Explicitly define column type
    private String description;

    @Column(name = "display_order")
    private Integer displayOrder;
}