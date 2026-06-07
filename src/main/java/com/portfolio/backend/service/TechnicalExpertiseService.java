package com.portfolio.backend.service;

import com.portfolio.backend.dto.TechnicalExpertiseDTO;
import com.portfolio.backend.entity.TechnicalExpertise;
import com.portfolio.backend.entity.User;
import com.portfolio.backend.exception.ResourceNotFoundException;
import com.portfolio.backend.mapper.TechnicalExpertiseMapper;
import com.portfolio.backend.repository.TechnicalExpertiseRepository;
import com.portfolio.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechnicalExpertiseService {

    private final TechnicalExpertiseRepository technicalExpertiseRepository;
    private final UserRepository userRepository;
    private final TechnicalExpertiseMapper technicalExpertiseMapper;

    @Autowired
    public TechnicalExpertiseService(TechnicalExpertiseRepository technicalExpertiseRepository,
                                     UserRepository userRepository,
                                     TechnicalExpertiseMapper technicalExpertiseMapper) {
        this.technicalExpertiseRepository = technicalExpertiseRepository;
        this.userRepository = userRepository;
        this.technicalExpertiseMapper = technicalExpertiseMapper;
    }

    // Helper method to find user by ID
    private User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    // Helper method to find technical expertise by ID and user
    private TechnicalExpertise findTechnicalExpertiseByIdAndUser(Integer expertiseId, User user) {
        return technicalExpertiseRepository.findByIdAndUser(expertiseId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Technical Expertise not found with id: " + expertiseId + " for user: " + user.getId()));
    }

    @Transactional
    public TechnicalExpertiseDTO createTechnicalExpertise(Integer userId, TechnicalExpertiseDTO technicalExpertiseDTO) {
        User user = findUserById(userId);

        TechnicalExpertise technicalExpertise = technicalExpertiseMapper.toEntity(technicalExpertiseDTO);
        technicalExpertise.setUser(user);

        TechnicalExpertise savedTechnicalExpertise = technicalExpertiseRepository.save(technicalExpertise);
        return technicalExpertiseMapper.toDto(savedTechnicalExpertise);
    }

    @Transactional(readOnly = true)
    public List<TechnicalExpertiseDTO> getAllTechnicalExpertiseByUserId(Integer userId) {
        User user = findUserById(userId);
        List<TechnicalExpertise> expertiseList = technicalExpertiseRepository.findByUser(user);
        return expertiseList.stream()
                .map(technicalExpertiseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TechnicalExpertiseDTO getTechnicalExpertiseByIdForUser(Integer userId, Integer expertiseId) {
        User user = findUserById(userId);
        TechnicalExpertise technicalExpertise = findTechnicalExpertiseByIdAndUser(expertiseId, user);
        return technicalExpertiseMapper.toDto(technicalExpertise);
    }

    @Transactional
    public TechnicalExpertiseDTO updateTechnicalExpertise(Integer userId, Integer expertiseId, TechnicalExpertiseDTO technicalExpertiseDTO) {
        User user = findUserById(userId);
        TechnicalExpertise existingExpertise = findTechnicalExpertiseByIdAndUser(expertiseId, user);

        technicalExpertiseMapper.updateEntityFromDto(technicalExpertiseDTO, existingExpertise);

        TechnicalExpertise updatedExpertise = technicalExpertiseRepository.save(existingExpertise);
        return technicalExpertiseMapper.toDto(updatedExpertise);
    }

    @Transactional
    public void deleteTechnicalExpertise(Integer userId, Integer expertiseId) {
        User user = findUserById(userId);
        TechnicalExpertise technicalExpertise = findTechnicalExpertiseByIdAndUser(expertiseId, user);
        technicalExpertiseRepository.delete(technicalExpertise);
    }
}