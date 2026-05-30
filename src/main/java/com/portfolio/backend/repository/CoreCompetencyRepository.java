package com.portfolio.backend.repository;

import com.portfolio.backend.entity.CoreCompetency;
import com.portfolio.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoreCompetencyRepository extends JpaRepository<CoreCompetency, Integer> {
    List<CoreCompetency> findByUser(User user);
    Optional<CoreCompetency> findByIdAndUser(Integer id, User user);
}