package com.portfolio.backend.service;

import com.portfolio.backend.dto.CoreCompetencyDTO;
import com.portfolio.backend.entity.CoreCompetency;
import com.portfolio.backend.entity.User;
import com.portfolio.backend.exception.ResourceNotFoundException;
import com.portfolio.backend.mapper.CoreCompetencyMapper;
import com.portfolio.backend.repository.CoreCompetencyRepository;
import com.portfolio.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoreCompetencyService {

    private final CoreCompetencyRepository coreCompetencyRepository;
    private final UserRepository userRepository;
    private final CoreCompetencyMapper coreCompetencyMapper;

    @Autowired
    public CoreCompetencyService(CoreCompetencyRepository coreCompetencyRepository,
                                 UserRepository userRepository,
                                 CoreCompetencyMapper coreCompetencyMapper) {
        this.coreCompetencyRepository = coreCompetencyRepository;
        this.userRepository = userRepository;
        this.coreCompetencyMapper = coreCompetencyMapper;
    }

    // Helper method to find user by ID
    private User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    // Helper method to find core competency by ID and user
    private CoreCompetency findCoreCompetencyByIdAndUser(Integer competencyId, User user) {
        return coreCompetencyRepository.findByIdAndUser(competencyId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Core Competency not found with id: " + competencyId + " for user: " + user.getId()));
    }

    @Transactional
    public CoreCompetencyDTO createCoreCompetency(Integer userId, CoreCompetencyDTO coreCompetencyDTO) {
        User user = findUserById(userId);

        CoreCompetency coreCompetency = coreCompetencyMapper.toEntity(coreCompetencyDTO);
        coreCompetency.setUser(user);

        CoreCompetency savedCoreCompetency = coreCompetencyRepository.save(coreCompetency);
        return coreCompetencyMapper.toDto(savedCoreCompetency);
    }

    @Transactional(readOnly = true)
    public List<CoreCompetencyDTO> getAllCoreCompetenciesByUserId(Integer userId) {
        User user = findUserById(userId);
        List<CoreCompetency> competencyList = coreCompetencyRepository.findByUser(user);
        return competencyList.stream()
                .map(coreCompetencyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CoreCompetencyDTO getCoreCompetencyByIdForUser(Integer userId, Integer competencyId) {
        User user = findUserById(userId);
        CoreCompetency coreCompetency = findCoreCompetencyByIdAndUser(competencyId, user);
        return coreCompetencyMapper.toDto(coreCompetency);
    }

    @Transactional
    public CoreCompetencyDTO updateCoreCompetency(Integer userId, Integer competencyId, CoreCompetencyDTO coreCompetencyDTO) {
        User user = findUserById(userId);
        CoreCompetency existingCompetency = findCoreCompetencyByIdAndUser(competencyId, user);

        coreCompetencyMapper.updateEntityFromDto(coreCompetencyDTO, existingCompetency);

        CoreCompetency updatedCompetency = coreCompetencyRepository.save(existingCompetency);
        return coreCompetencyMapper.toDto(updatedCompetency);
    }

    @Transactional
    public void deleteCoreCompetency(Integer userId, Integer competencyId) {
        User user = findUserById(userId);
        CoreCompetency coreCompetency = findCoreCompetencyByIdAndUser(competencyId, user);
        coreCompetencyRepository.delete(coreCompetency);
    }
}