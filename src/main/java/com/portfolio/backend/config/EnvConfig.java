package com.portfolio.backend.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {
    
    static {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
        
        // Load environment variables from .env file
        dotenv.entries().forEach(entry -> {
            if (System.getenv(entry.getKey()) == null) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        });
    }
}
