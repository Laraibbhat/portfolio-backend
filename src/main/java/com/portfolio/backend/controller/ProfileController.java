package com.portfolio.backend.controller;

import com.portfolio.backend.dto.ProfileDTO;
import com.portfolio.backend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/profiles")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = {"http://localhost:3000", "https://www.kashify.shop"})
public class ProfileController {
   
   @Autowired
   private ProfileService profileService;
   
   @GetMapping
   public ResponseEntity<List<ProfileDTO>> getAllProfiles() {
	  List<ProfileDTO> profiles = profileService.getAllProfiles();
	  return ResponseEntity.ok(profiles);
   }
   
    @GetMapping("/{username}")
    public ResponseEntity<ProfileDTO> getProfileByUsername(@PathVariable("username") String username) {
	  ProfileDTO profile = profileService.getProfileByUsername(username);
	  return ResponseEntity.ok(profile);
   }
   
   @PostMapping
   public ResponseEntity<ProfileDTO> createProfile(@RequestBody ProfileDTO profileDTO) {
	  ProfileDTO createdProfile = profileService.createProfile(profileDTO);
	  return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
   }
   
    @PutMapping("/{username}")
    public ResponseEntity<ProfileDTO> updateProfile(
           @PathVariable("username") String username,
           @RequestBody ProfileDTO profileDTO) {
	  ProfileDTO updatedProfile = profileService.updateProfile(username, profileDTO);
	  return ResponseEntity.ok(updatedProfile);
   }
   
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteProfile(@PathVariable("username") String username) {
	  profileService.deleteProfile(username);
	  return ResponseEntity.noContent().build();
   }
}