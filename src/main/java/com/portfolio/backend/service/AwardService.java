package com.portfolio.backend.service;

import com.portfolio.backend.dto.AwardDTO;
import com.portfolio.backend.entity.Award;
import com.portfolio.backend.entity.User;
import com.portfolio.backend.exception.ResourceNotFoundException;
import com.portfolio.backend.mapper.AwardMapper;
import com.portfolio.backend.repository.AwardRepository;
import com.portfolio.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AwardService {

    private final AwardRepository awardRepository;
    private final UserRepository userRepository;
    private final AwardMapper awardMapper;

    @Autowired
    public AwardService(AwardRepository awardRepository, UserRepository userRepository, AwardMapper awardMapper) {
        this.awardRepository = awardRepository;
        this.userRepository = userRepository;
        this.awardMapper = awardMapper;
    }

    public List<AwardDTO> getAllAwards() {
        return awardRepository.findAll().stream()
                .map(awardMapper::toDto)
                .collect(Collectors.toList());
    }

    public AwardDTO getAwardById(Integer id) {
        Award award = awardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Award not found with id: " + id));
        return awardMapper.toDto(award);
    }

    @Transactional
    public AwardDTO createAward(AwardDTO awardDTO) {
        String username = awardDTO.getUsername();
        Integer userId = awardDTO.getUserId();
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
        Award award = awardMapper.toEntity(awardDTO);
        award.setUser(user);
        Award savedAward = awardRepository.save(award);
        return awardMapper.toDto(savedAward);
    }

    @Transactional
    public AwardDTO updateAward(Integer id, AwardDTO awardDTO) {
        Award existingAward = awardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Award not found with id: " + id));

        awardMapper.updateEntityFromDto(awardDTO, existingAward);
        if (awardDTO.getUsername() != null && !existingAward.getUser().getUsername().equals(awardDTO.getUsername())) {
            User user = userRepository.findByUsername(awardDTO.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + awardDTO.getUsername()));
            existingAward.setUser(user);
        } else if (awardDTO.getUserId() != null && !existingAward.getUser().getId().equals(awardDTO.getUserId())) {
            User user = userRepository.findById(awardDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + awardDTO.getUserId()));
            existingAward.setUser(user);
        }
        Award updatedAward = awardRepository.save(existingAward);
        return awardMapper.toDto(updatedAward);
    }

    @Transactional
    public void deleteAward(Integer id) {
        if (!awardRepository.existsById(id)) {
            throw new ResourceNotFoundException("Award not found with id: " + id);
        }
        awardRepository.deleteById(id);
    }
}