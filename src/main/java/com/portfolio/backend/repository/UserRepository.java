package com.portfolio.backend.repository;

import com.portfolio.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.technicalExpertise
            LEFT JOIN FETCH u.experiences
            LEFT JOIN FETCH u.education
            LEFT JOIN FETCH u.certifications
            LEFT JOIN FETCH u.publications
            LEFT JOIN FETCH u.awards
            LEFT JOIN FETCH u.coreCompetencies
            WHERE u.username = :username
            """)
    Optional<User> findByUsernameWithDetails(@Param("username") String username);

    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.technicalExpertise
            LEFT JOIN FETCH u.experiences
            LEFT JOIN FETCH u.education
            LEFT JOIN FETCH u.certifications
            LEFT JOIN FETCH u.publications
            LEFT JOIN FETCH u.awards
            LEFT JOIN FETCH u.coreCompetencies
            WHERE u.id = :id
            """)
    Optional<User> findByIdWithDetails(@Param("id") Integer id);

    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.technicalExpertise
            LEFT JOIN FETCH u.experiences
            LEFT JOIN FETCH u.education
            LEFT JOIN FETCH u.certifications
            LEFT JOIN FETCH u.publications
            LEFT JOIN FETCH u.awards
            LEFT JOIN FETCH u.coreCompetencies
            """)
    List<User> findAllWithDetails();
}