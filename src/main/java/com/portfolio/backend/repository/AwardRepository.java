package com.portfolio.backend.repository;

import com.portfolio.backend.entity.Award;
import com.portfolio.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AwardRepository extends JpaRepository<Award, Integer> {
    List<Award> findByUser(User user);
    Optional<Award> findByIdAndUser(Integer id, User user);
}