package com.portfolio.backend.controller;

import com.portfolio.backend.dto.UserDTO;
import com.portfolio.backend.dto.UserRequestDTO;
import com.portfolio.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from your frontend origin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Integer id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable("username") String username) {
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserDTO createdUser = userService.createUser(userRequestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/username/{username}")
    public ResponseEntity<UserDTO> updateUserByUsername(@PathVariable("username") String username, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserDTO updatedUser = userService.updateUserByUsername(username, userRequestDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/username/{username}")
    public ResponseEntity<Void> deleteUserByUsername(@PathVariable("username") String username) {
        userService.deleteUserByUsername(username);
        return ResponseEntity.noContent().build();
    }
}