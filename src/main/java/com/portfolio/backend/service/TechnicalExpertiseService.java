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
    public TechnicalExpertiseService(TechnicalExpertiseRepository technicalExpertiseRepository, UserRepository userRepository, TechnicalExpertiseMapper technicalExpertiseMapper) {
        this.technicalExpertiseRepository = technicalExpertiseRepository;
        this.userRepository = userRepository;
        this.technicalExpertiseMapper = technicalExpertiseMapper;
    }

    public List<TechnicalExpertiseDTO> getAllTechnicalExpertise() {
        return technicalExpertiseRepository.findAll().stream()
                .map(technicalExpertiseMapper::toDto)
                .collect(Collectors.toList());
    }

    public TechnicalExpertiseDTO getTechnicalExpertiseById(Integer id) {
        TechnicalExpertise technicalExpertise = technicalExpertiseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technical Expertise not found with id: " + id));
        return technicalExpertiseMapper.toDto(technicalExpertise);
    }

    @Transactional
    public TechnicalExpertiseDTO createTechnicalExpertise(TechnicalExpertiseDTO technicalExpertiseDTO) {
        // Prefer username if provided, fall back to userId for compatibility
        String username = technicalExpertiseDTO.getUsername();
        Integer userId = technicalExpertiseDTO.getUserId();
        User user;
        if (username != null && !username.trim().isEmpty()) {
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        } else if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        } else {
            throw new IllegalArgumentException("Either username or userId must be provided");
        }
        TechnicalExpertise technicalExpertise = technicalExpertiseMapper.toEntity(technicalExpertiseDTO);
        technicalExpertise.setUser(user);
        TechnicalExpertise savedTechnicalExpertise = technicalExpertiseRepository.save(technicalExpertise);
        return technicalExpertiseMapper.toDto(savedTechnicalExpertise);
    }

    @Transactional
    public TechnicalExpertiseDTO updateTechnicalExpertise(Integer id, TechnicalExpertiseDTO technicalExpertiseDTO) {
        TechnicalExpertise existingTechnicalExpertise = technicalExpertiseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technical Expertise not found with id: " + id));

        technicalExpertiseMapper.updateEntityFromDto(technicalExpertiseDTO, existingTechnicalExpertise);
        // If username is provided and different, update relation; otherwise fall back to userId
        if (technicalExpertiseDTO.getUsername() != null && !existingTechnicalExpertise.getUser().getUsername().equals(technicalExpertiseDTO.getUsername())) {
            User user = userRepository.findByUsername(technicalExpertiseDTO.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + technicalExpertiseDTO.getUsername()));
            existingTechnicalExpertise.setUser(user);
        } else if (technicalExpertiseDTO.getUserId() != null && !existingTechnicalExpertise.getUser().getId().equals(technicalExpertiseDTO.getUserId())) {
            User user = userRepository.findById(technicalExpertiseDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + technicalExpertiseDTO.getUserId()));
            existingTechnicalExpertise.setUser(user);
        }
        TechnicalExpertise updatedTechnicalExpertise = technicalExpertiseRepository.save(existingTechnicalExpertise);
        return technicalExpertiseMapper.toDto(updatedTechnicalExpertise);
    }

    @Transactional
    public void deleteTechnicalExpertise(Integer id) {
        if (!technicalExpertiseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Technical Expertise not found with id: " + id);
        }
        technicalExpertiseRepository.deleteById(id);
    }
}