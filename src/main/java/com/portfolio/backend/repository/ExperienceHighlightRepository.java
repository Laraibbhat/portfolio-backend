package com.portfolio.backend.repository;

import com.portfolio.backend.entity.ExperienceHighlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceHighlightRepository extends JpaRepository<ExperienceHighlight, Integer> {
}