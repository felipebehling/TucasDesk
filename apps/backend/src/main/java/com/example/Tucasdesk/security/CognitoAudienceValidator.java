package com.example.Tucasdesk.security;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;

/**
 * Validates if the Cognito JWT contains the expected audience/client identifier.
 */
public class CognitoAudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final String expectedClientId;

    public CognitoAudienceValidator(String expectedClientId) {
        this.expectedClientId = expectedClientId;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        if (!StringUtils.hasText(expectedClientId)) {
            return OAuth2TokenValidatorResult.success();
        }

        Object audClaim = token.getClaims().get("aud");
        if (audClaim instanceof String) {
            String aud = (String) audClaim;
            if (expectedClientId.equals(aud)) {
                return OAuth2TokenValidatorResult.success();
            }
        }
        if (audClaim instanceof Collection<?>) {
            Collection<?> audience = (Collection<?>) audClaim;
            if (audience.stream().anyMatch(expectedClientId::equals)) {
                return OAuth2TokenValidatorResult.success();
            }
        }

        Object clientId = token.getClaims().get("client_id");
        if (Objects.equals(expectedClientId, clientId)) {
            return OAuth2TokenValidatorResult.success();
        }

        OAuth2Error error = new OAuth2Error("invalid_token", "Token audience does not match the configured Cognito client id.",
                null);
        return OAuth2TokenValidatorResult.failure(error);
    }
}
