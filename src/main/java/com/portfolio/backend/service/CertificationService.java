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
    public CertificationService(CertificationRepository certificationRepository,
                                UserRepository userRepository,
                                CertificationMapper certificationMapper) {
        this.certificationRepository = certificationRepository;
        this.userRepository = userRepository;
        this.certificationMapper = certificationMapper;
    }

    // Helper method to find user by ID
    private User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    // Helper method to find certification by ID and user
    private Certification findCertificationByIdAndUser(Integer certificationId, User user) {
        return certificationRepository.findByIdAndUser(certificationId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found with id: " + certificationId + " for user: " + user.getId()));
    }

    @Transactional
    public CertificationDTO createCertification(Integer userId, CertificationDTO certificationDTO) {
        User user = findUserById(userId);

        Certification certification = certificationMapper.toEntity(certificationDTO);
        certification.setUser(user);

        Certification savedCertification = certificationRepository.save(certification);
        return certificationMapper.toDto(savedCertification);
    }

    @Transactional(readOnly = true)
    public List<CertificationDTO> getAllCertificationsByUserId(Integer userId) {
        User user = findUserById(userId);
        List<Certification> certificationList = certificationRepository.findByUser(user);
        return certificationList.stream()
                .map(certificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CertificationDTO getCertificationByIdForUser(Integer userId, Integer certificationId) {
        User user = findUserById(userId);
        Certification certification = findCertificationByIdAndUser(certificationId, user);
        return certificationMapper.toDto(certification);
    }

    @Transactional
    public CertificationDTO updateCertification(Integer userId, Integer certificationId, CertificationDTO certificationDTO) {
        User user = findUserById(userId);
        Certification existingCertification = findCertificationByIdAndUser(certificationId, user);

        certificationMapper.updateEntityFromDto(certificationDTO, existingCertification);

        Certification updatedCertification = certificationRepository.save(existingCertification);
        return certificationMapper.toDto(updatedCertification);
    }

    @Transactional
    public void deleteCertification(Integer userId, Integer certificationId) {
        User user = findUserById(userId);
        Certification certification = findCertificationByIdAndUser(certificationId, user);
        certificationRepository.delete(certification);
    }
}