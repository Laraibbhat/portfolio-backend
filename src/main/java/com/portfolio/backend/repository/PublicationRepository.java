package com.portfolio.backend.repository;

import com.portfolio.backend.entity.Publication;
import com.portfolio.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Integer> {
    List<Publication> findByUser(User user);
    Optional<Publication> findByIdAndUser(Integer id, User user);
}