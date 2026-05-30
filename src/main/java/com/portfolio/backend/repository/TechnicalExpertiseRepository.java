package com.portfolio.backend.repository;

import com.portfolio.backend.entity.TechnicalExpertise;
import com.portfolio.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TechnicalExpertiseRepository extends JpaRepository<TechnicalExpertise, Integer> {
    List<TechnicalExpertise> findByUser(User user);
    Optional<TechnicalExpertise> findByIdAndUser(Integer id, User user);
}