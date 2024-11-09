package dev.fiinn.profile_service.controller;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "profile.url")
public class ApiURI {
    private String path;
}
