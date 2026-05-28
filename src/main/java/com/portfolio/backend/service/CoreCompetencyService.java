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
    public CoreCompetencyService(CoreCompetencyRepository coreCompetencyRepository, UserRepository userRepository, CoreCompetencyMapper coreCompetencyMapper) {
        this.coreCompetencyRepository = coreCompetencyRepository;
        this.userRepository = userRepository;
        this.coreCompetencyMapper = coreCompetencyMapper;
    }

    public List<CoreCompetencyDTO> getAllCoreCompetencies() {
        return coreCompetencyRepository.findAll().stream()
                .map(coreCompetencyMapper::toDto)
                .collect(Collectors.toList());
    }

    public CoreCompetencyDTO getCoreCompetencyById(Integer id) {
        CoreCompetency coreCompetency = coreCompetencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Core Competency not found with id: " + id));
        return coreCompetencyMapper.toDto(coreCompetency);
    }

    @Transactional
    public CoreCompetencyDTO createCoreCompetency(CoreCompetencyDTO coreCompetencyDTO) {
        String username = coreCompetencyDTO.getUsername();
        Integer userId = coreCompetencyDTO.getUserId();
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
        CoreCompetency coreCompetency = coreCompetencyMapper.toEntity(coreCompetencyDTO);
        coreCompetency.setUser(user);
        CoreCompetency savedCoreCompetency = coreCompetencyRepository.save(coreCompetency);
        return coreCompetencyMapper.toDto(savedCoreCompetency);
    }

    @Transactional
    public CoreCompetencyDTO updateCoreCompetency(Integer id, CoreCompetencyDTO coreCompetencyDTO) {
        CoreCompetency existingCoreCompetency = coreCompetencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Core Competency not found with id: " + id));

        coreCompetencyMapper.updateEntityFromDto(coreCompetencyDTO, existingCoreCompetency);
        if (coreCompetencyDTO.getUsername() != null && !existingCoreCompetency.getUser().getUsername().equals(coreCompetencyDTO.getUsername())) {
            User user = userRepository.findByUsername(coreCompetencyDTO.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + coreCompetencyDTO.getUsername()));
            existingCoreCompetency.setUser(user);
        } else if (coreCompetencyDTO.getUserId() != null && !existingCoreCompetency.getUser().getId().equals(coreCompetencyDTO.getUserId())) {
            User user = userRepository.findById(coreCompetencyDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + coreCompetencyDTO.getUserId()));
            existingCoreCompetency.setUser(user);
        }
        CoreCompetency updatedCoreCompetency = coreCompetencyRepository.save(existingCoreCompetency);
        return coreCompetencyMapper.toDto(updatedCoreCompetency);
    }

    @Transactional
    public void deleteCoreCompetency(Integer id) {
        if (!coreCompetencyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Core Competency not found with id: " + id);
        }
        coreCompetencyRepository.deleteById(id);
    }
}