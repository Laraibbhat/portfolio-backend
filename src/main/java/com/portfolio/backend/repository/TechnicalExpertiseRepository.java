package com.portfolio.backend.repository;

import com.portfolio.backend.entity.TechnicalExpertise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnicalExpertiseRepository extends JpaRepository<TechnicalExpertise, Integer> {
}