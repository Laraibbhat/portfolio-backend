package com.portfolio.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresignedUrlRequest {
    private String filename;
    private String contentType;
    private String username;
}
