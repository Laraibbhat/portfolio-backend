package com.portfolio.backend.controller;

import com.portfolio.backend.dto.PresignedUrlRequest;
import com.portfolio.backend.dto.PresignedUrlResponse;
import com.portfolio.backend.service.S3UploadsService;
import com.portfolio.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/uploads")
@CrossOrigin(origins = "http://localhost:3000")
public class UploadController {

    @Autowired
    private S3UploadsService s3UploadsService;

    @Autowired
    private UserService userService;

    @PostMapping("/presign")
    public ResponseEntity<PresignedUrlResponse> generatePresignedUrl(
            @RequestBody PresignedUrlRequest request) {
        try {
            PresignedUrlResponse response = s3UploadsService.generatePresignedUploadUrl(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/associate/{username}")
    public ResponseEntity<Void> associateAvatarWithUser(
            @PathVariable String username,
            @RequestParam String avatarKey) {
        try {
            userService.updateUserAvatar(username, avatarKey);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/download/{username}")
    public ResponseEntity<PresignedUrlResponse> getPresignedDownloadUrl(
            @PathVariable String username) {
        try {
            PresignedUrlResponse response = userService.getAvatarPresignedUrl(username);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
