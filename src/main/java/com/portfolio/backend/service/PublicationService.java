package com.portfolio.backend.service;

import com.portfolio.backend.dto.PublicationDTO;
import com.portfolio.backend.entity.Publication;
import com.portfolio.backend.entity.User;
import com.portfolio.backend.exception.ResourceNotFoundException;
import com.portfolio.backend.mapper.PublicationMapper;
import com.portfolio.backend.repository.PublicationRepository;
import com.portfolio.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicationService {

    private final PublicationRepository publicationRepository;
    private final UserRepository userRepository;
    private final PublicationMapper publicationMapper;

    @Autowired
    public PublicationService(PublicationRepository publicationRepository,
                              UserRepository userRepository,
                              PublicationMapper publicationMapper) {
        this.publicationRepository = publicationRepository;
        this.userRepository = userRepository;
        this.publicationMapper = publicationMapper;
    }

    // Helper method to find user by ID
    private User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    // Helper method to find publication by ID and user
    private Publication findPublicationByIdAndUser(Integer publicationId, User user) {
        return publicationRepository.findByIdAndUser(publicationId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found with id: " + publicationId + " for user: " + user.getId()));
    }

    @Transactional
    public PublicationDTO createPublication(Integer userId, PublicationDTO publicationDTO) {
        User user = findUserById(userId);

        Publication publication = publicationMapper.toEntity(publicationDTO);
        publication.setUser(user);

        Publication savedPublication = publicationRepository.save(publication);
        return publicationMapper.toDto(savedPublication);
    }

    @Transactional(readOnly = true)
    public List<PublicationDTO> getAllPublicationsByUserId(Integer userId) {
        User user = findUserById(userId);
        List<Publication> publicationList = publicationRepository.findByUser(user);
        return publicationList.stream()
                .map(publicationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PublicationDTO getPublicationByIdForUser(Integer userId, Integer publicationId) {
        User user = findUserById(userId);
        Publication publication = findPublicationByIdAndUser(publicationId, user);
        return publicationMapper.toDto(publication);
    }

    @Transactional
    public PublicationDTO updatePublication(Integer userId, Integer publicationId, PublicationDTO publicationDTO) {
        User user = findUserById(userId);
        Publication existingPublication = findPublicationByIdAndUser(publicationId, user);

        publicationMapper.updateEntityFromDto(publicationDTO, existingPublication);

        Publication updatedPublication = publicationRepository.save(existingPublication);
        return publicationMapper.toDto(updatedPublication);
    }

    @Transactional
    public void deletePublication(Integer userId, Integer publicationId) {
        User user = findUserById(userId);
        Publication publication = findPublicationByIdAndUser(publicationId, user);
        publicationRepository.delete(publication);
    }
}