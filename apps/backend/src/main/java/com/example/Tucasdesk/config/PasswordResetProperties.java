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

    /**
     * @return The base URL of the frontend password reset page.
     */
    public String getFrontendUrl() {
        return frontendUrl;
    }

    /**
     * @param frontendUrl The base URL of the frontend password reset page.
     */
    public void setFrontendUrl(String frontendUrl) {
        this.frontendUrl = frontendUrl;
    }

    /**
     * @return The Time-to-Live (TTL) for password reset tokens.
     */
    public Duration getTokenTtl() {
        return tokenTtl;
    }

    /**
     * @param tokenTtl The Time-to-Live (TTL) for password reset tokens.
     */
    public void setTokenTtl(Duration tokenTtl) {
        this.tokenTtl = tokenTtl;
    }

    /**
     * @return The email subject for password reset emails.
     */
    public String getEmailSubject() {
        return emailSubject;
    }

    /**
     * @param emailSubject The email subject for password reset emails.
     */
    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }
}
