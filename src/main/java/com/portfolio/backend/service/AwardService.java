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
    public AwardService(AwardRepository awardRepository,
                        UserRepository userRepository,
                        AwardMapper awardMapper) {
        this.awardRepository = awardRepository;
        this.userRepository = userRepository;
        this.awardMapper = awardMapper;
    }

    // Helper method to find user by ID
    private User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    // Helper method to find award by ID and user
    private Award findAwardByIdAndUser(Integer awardId, User user) {
        return awardRepository.findByIdAndUser(awardId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Award not found with id: " + awardId + " for user: " + user.getId()));
    }

    @Transactional
    public AwardDTO createAward(Integer userId, AwardDTO awardDTO) {
        User user = findUserById(userId);

        Award award = awardMapper.toEntity(awardDTO);
        award.setUser(user);

        Award savedAward = awardRepository.save(award);
        return awardMapper.toDto(savedAward);
    }

    @Transactional(readOnly = true)
    public List<AwardDTO> getAllAwardsByUserId(Integer userId) {
        User user = findUserById(userId);
        List<Award> awardList = awardRepository.findByUser(user);
        return awardList.stream()
                .map(awardMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AwardDTO getAwardByIdForUser(Integer userId, Integer awardId) {
        User user = findUserById(userId);
        Award award = findAwardByIdAndUser(awardId, user);
        return awardMapper.toDto(award);
    }

    @Transactional
    public AwardDTO updateAward(Integer userId, Integer awardId, AwardDTO awardDTO) {
        User user = findUserById(userId);
        Award existingAward = findAwardByIdAndUser(awardId, user);

        awardMapper.updateEntityFromDto(awardDTO, existingAward);

        Award updatedAward = awardRepository.save(existingAward);
        return awardMapper.toDto(updatedAward);
    }

    @Transactional
    public void deleteAward(Integer userId, Integer awardId) {
        User user = findUserById(userId);
        Award award = findAwardByIdAndUser(awardId, user);
        awardRepository.delete(award);
    }
}