package com.portfolio.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "technical_expertise")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnicalExpertise {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;
   
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id", nullable = false)
   private User user;
   
   @Column(nullable = false)
   private String category;
   
   @Column(nullable = false)
   private String skill;
}