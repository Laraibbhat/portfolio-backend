package com.portfolio.backend.service;

import com.portfolio.backend.dto.PresignedUrlRequest;
import com.portfolio.backend.dto.PresignedUrlResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
public class S3UploadsService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;
    private final long presignedUrlExpiration;
    private final long maxFileSize;

    public S3UploadsService(
            S3Client s3Client,
            @Value("${aws.s3.bucket-name}") String bucketName,
            @Value("${aws.s3.presigned-url-expiration}") long presignedUrlExpiration,
            @Value("${aws.s3.max-file-size}") long maxFileSize,
            @Value("${aws.s3.access-key}") String awsAccessKeyId,
            @Value("${aws.s3.secret-key}") String awsSecretAccessKey,
            @Value("${aws.s3.region}") String region) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.presignedUrlExpiration = presignedUrlExpiration;
        this.maxFileSize = maxFileSize;
        
        // Initialize S3Presigner with explicit credentials and region
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
            awsAccessKeyId,
            awsSecretAccessKey
        );
        
        this.s3Presigner = S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of(region))
                .build();
    }

    public PresignedUrlResponse generatePresignedUploadUrl(PresignedUrlRequest request) {
        validateUploadRequest(request);

        String objectKey = generateObjectKey(request.getUsername(), request.getFilename());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(request.getContentType())
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(presignedUrlExpiration))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        log.info("Generated presigned upload URL for key: {}", objectKey);

        return PresignedUrlResponse.builder()
                .uploadUrl(presignedRequest.url().toString())
                .key(objectKey)
                .contentType(request.getContentType())
                .expirationTimeSeconds(presignedUrlExpiration)
                .build();
    }

    public PresignedUrlResponse generatePresignedDownloadUrl(String objectKey) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(presignedUrlExpiration))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

        log.info("Generated presigned download URL for key: {}", objectKey);

        return PresignedUrlResponse.builder()
                .uploadUrl(presignedRequest.url().toString())
                .key(objectKey)
                .expirationTimeSeconds(presignedUrlExpiration)
                .build();
    }

    private void validateUploadRequest(PresignedUrlRequest request) {
        if (request.getFilename() == null || request.getFilename().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be empty");
        }

        if (request.getContentType() == null || request.getContentType().isEmpty()) {
            throw new IllegalArgumentException("ContentType cannot be empty");
        }

        if (!isValidImageType(request.getContentType())) {
            throw new IllegalArgumentException("Invalid content type. Only image types are allowed");
        }
    }

    private boolean isValidImageType(String contentType) {
        return contentType.startsWith("image/") &&
                (contentType.equals("image/jpeg") ||
                 contentType.equals("image/png") ||
                 contentType.equals("image/gif") ||
                 contentType.equals("image/webp"));
    }

    private String generateObjectKey(String username, String filename) {
        String fileExtension = getFileExtension(filename);
        String uniqueId = UUID.randomUUID().toString();
        return String.format("avatars/%s/%s%s", username, uniqueId, fileExtension);
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex);
        }
        return "";
    }
}
