package com.example.Tucasdesk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Configuration properties for the password reset flow.
 */
@Component
@ConfigurationProperties(prefix = "app.password-reset")
public class PasswordResetProperties {

    /**
     * Base URL of the frontend page that handles password reset.
     */
    private String frontendUrl = "http://localhost:5173/redefinir-senha";

    /**
     * Expiration time for password reset tokens.
     */
    private Duration tokenTtl = Duration.ofMinutes(30);

    /**
     * Subject used when sending password reset e-mails.
     */
    private String emailSubject = "Redefinição de senha - TucasDesk";

    public String getFrontendUrl() {
        return frontendUrl;
    }

    public void setFrontendUrl(String frontendUrl) {
        this.frontendUrl = frontendUrl;
    }

    public Duration getTokenTtl() {
        return tokenTtl;
    }

    public void setTokenTtl(Duration tokenTtl) {
        this.tokenTtl = tokenTtl;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }
}
