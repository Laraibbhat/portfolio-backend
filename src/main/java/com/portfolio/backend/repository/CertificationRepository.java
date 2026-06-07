package com.portfolio.backend.repository;

import com.portfolio.backend.entity.Certification;
import com.portfolio.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Integer> {
    List<Certification> findByUser(User user);
    Optional<Certification> findByIdAndUser(Integer id, User user);
}