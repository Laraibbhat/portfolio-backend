package com.portfolio.backend.repository;

import com.portfolio.backend.entity.Education;
import com.portfolio.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EducationRepository extends JpaRepository<Education, Integer> {
    List<Education> findByUser(User user);
    Optional<Education> findByIdAndUser(Integer id, User user);
}