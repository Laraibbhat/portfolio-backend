package com.portfolio.backend.service;

import com.portfolio.backend.dto.CertificationDTO;
import com.portfolio.backend.entity.Certification;
import com.portfolio.backend.entity.User;
import com.portfolio.backend.exception.ResourceNotFoundException;
import com.portfolio.backend.mapper.CertificationMapper;
import com.portfolio.backend.repository.CertificationRepository;
import com.portfolio.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final UserRepository userRepository;
    private final CertificationMapper certificationMapper;

    @Autowired
    public CertificationService(CertificationRepository certificationRepository, UserRepository userRepository, CertificationMapper certificationMapper) {
        this.certificationRepository = certificationRepository;
        this.userRepository = userRepository;
        this.certificationMapper = certificationMapper;
    }

    public List<CertificationDTO> getAllCertifications() {
        return certificationRepository.findAll().stream()
                .map(certificationMapper::toDto)
                .collect(Collectors.toList());
    }

    public CertificationDTO getCertificationById(Integer id) {
        Certification certification = certificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found with id: " + id));
        return certificationMapper.toDto(certification);
    }

    @Transactional
    public CertificationDTO createCertification(CertificationDTO certificationDTO) {
        String username = certificationDTO.getUsername();
        Integer userId = certificationDTO.getUserId();
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
        Certification certification = certificationMapper.toEntity(certificationDTO);
        certification.setUser(user);
        Certification savedCertification = certificationRepository.save(certification);
        return certificationMapper.toDto(savedCertification);
    }

    @Transactional
    public CertificationDTO updateCertification(Integer id, CertificationDTO certificationDTO) {
        Certification existingCertification = certificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found with id: " + id));

        certificationMapper.updateEntityFromDto(certificationDTO, existingCertification);
        if (certificationDTO.getUsername() != null && !existingCertification.getUser().getUsername().equals(certificationDTO.getUsername())) {
            User user = userRepository.findByUsername(certificationDTO.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + certificationDTO.getUsername()));
            existingCertification.setUser(user);
        } else if (certificationDTO.getUserId() != null && !existingCertification.getUser().getId().equals(certificationDTO.getUserId())) {
            User user = userRepository.findById(certificationDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + certificationDTO.getUserId()));
            existingCertification.setUser(user);
        }
        Certification updatedCertification = certificationRepository.save(existingCertification);
        return certificationMapper.toDto(updatedCertification);
    }

    @Transactional
    public void deleteCertification(Integer id) {
        if (!certificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Certification not found with id: " + id);
        }
        certificationRepository.deleteById(id);
    }
}