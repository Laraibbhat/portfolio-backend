package com.portfolio.backend.repository;

import com.portfolio.backend.entity.Experience;
import com.portfolio.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Integer> {
    List<Experience> findByUser(User user);
    Optional<Experience> findByIdAndUser(Integer id, User user);
}