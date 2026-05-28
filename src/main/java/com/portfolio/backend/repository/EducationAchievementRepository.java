package com.portfolio.backend.repository;

import com.portfolio.backend.entity.EducationAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationAchievementRepository extends JpaRepository<EducationAchievement, Integer> {
}