package com.portfolio.backend.repository;

import com.portfolio.backend.entity.CoreCompetency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoreCompetencyRepository extends JpaRepository<CoreCompetency, Integer> {
}