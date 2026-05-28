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
    public PublicationService(PublicationRepository publicationRepository, UserRepository userRepository, PublicationMapper publicationMapper) {
        this.publicationRepository = publicationRepository;
        this.userRepository = userRepository;
        this.publicationMapper = publicationMapper;
    }

    public List<PublicationDTO> getAllPublications() {
        return publicationRepository.findAll().stream()
                .map(publicationMapper::toDto)
                .collect(Collectors.toList());
    }

    public PublicationDTO getPublicationById(Integer id) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found with id: " + id));
        return publicationMapper.toDto(publication);
    }

    @Transactional
    public PublicationDTO createPublication(PublicationDTO publicationDTO) {
        String username = publicationDTO.getUsername();
        Integer userId = publicationDTO.getUserId();
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
        Publication publication = publicationMapper.toEntity(publicationDTO);
        publication.setUser(user);
        Publication savedPublication = publicationRepository.save(publication);
        return publicationMapper.toDto(savedPublication);
    }

    @Transactional
    public PublicationDTO updatePublication(Integer id, PublicationDTO publicationDTO) {
        Publication existingPublication = publicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found with id: " + id));

        publicationMapper.updateEntityFromDto(publicationDTO, existingPublication);
        if (publicationDTO.getUsername() != null && !existingPublication.getUser().getUsername().equals(publicationDTO.getUsername())) {
            User user = userRepository.findByUsername(publicationDTO.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + publicationDTO.getUsername()));
            existingPublication.setUser(user);
        } else if (publicationDTO.getUserId() != null && !existingPublication.getUser().getId().equals(publicationDTO.getUserId())) {
            User user = userRepository.findById(publicationDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + publicationDTO.getUserId()));
            existingPublication.setUser(user);
        }
        Publication updatedPublication = publicationRepository.save(existingPublication);
        return publicationMapper.toDto(updatedPublication);
    }

    @Transactional
    public void deletePublication(Integer id) {
        if (!publicationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Publication not found with id: " + id);
        }
        publicationRepository.deleteById(id);
    }
}