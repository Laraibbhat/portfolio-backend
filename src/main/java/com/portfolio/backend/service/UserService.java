package com.portfolio.backend.service;

import com.portfolio.backend.dto.UserDTO;
import com.portfolio.backend.dto.UserRequestDTO;
import com.portfolio.backend.entity.User;
import com.portfolio.backend.exception.ResourceNotFoundException;
import com.portfolio.backend.mapper.UserMapper;
import com.portfolio.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAllWithDetails().stream()
                .map(user -> {
                    // Force initialization of all collections while transaction is active
                    user.getTechnicalExpertise().size();
                    user.getExperiences().size();
                    user.getEducation().size();
                    user.getCertifications().size();
                    user.getPublications().size();
                    user.getAwards().size();
                    user.getCoreCompetencies().size();
                    return userMapper.toDto(user);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Integer id) {
        User user = userRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        // Force initialization of all collections while transaction is active
        user.getTechnicalExpertise().size();
        user.getExperiences().size();
        user.getEducation().size();
        user.getCertifications().size();
        user.getPublications().size();
        user.getAwards().size();
        user.getCoreCompetencies().size();
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsernameWithDetails(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        // Force initialization of all collections while transaction is active
        user.getTechnicalExpertise().size();
        user.getExperiences().size();
        user.getEducation().size();
        user.getCertifications().size();
        user.getPublications().size();
        user.getAwards().size();
        user.getCoreCompetencies().size();
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDTO createUser(UserRequestDTO userRequestDTO) {
        User user = userMapper.toEntity(userRequestDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Transactional
    public UserDTO updateUserByUsername(String username, UserRequestDTO userRequestDTO) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        userMapper.updateEntityFromDto(userRequestDTO, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    @Transactional
    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        userRepository.delete(user);
    }
}