package com.example.Tucasdesk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * Configuration properties used to integrate the application with AWS Cognito.
 */
@ConfigurationProperties(prefix = "aws.cognito")
public class AwsCognitoProperties {

    private String region;
    private String userPoolId;
    private String appClientId;
    private String issuerUri;
    private String jwkSetUri;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getUserPoolId() {
        return userPoolId;
    }

    public void setUserPoolId(String userPoolId) {
        this.userPoolId = userPoolId;
    }

    public String getAppClientId() {
        return appClientId;
    }

    public void setAppClientId(String appClientId) {
        this.appClientId = appClientId;
    }

    public String getIssuerUri() {
        if (StringUtils.hasText(issuerUri)) {
            return issuerUri;
        }
        if (StringUtils.hasText(region) && StringUtils.hasText(userPoolId)) {
            return String.format("https://cognito-idp.%s.amazonaws.com/%s", region, userPoolId);
        }
        return null;
    }

    public void setIssuerUri(String issuerUri) {
        this.issuerUri = issuerUri;
    }

    public String getJwkSetUri() {
        if (StringUtils.hasText(jwkSetUri)) {
            return jwkSetUri;
        }
        String issuer = getIssuerUri();
        if (StringUtils.hasText(issuer)) {
            return issuer + "/.well-known/jwks.json";
        }
        return null;
    }

    public void setJwkSetUri(String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
    }

    /**
     * Indicates whether enough information was provided to enable Cognito based
     * authentication.
     *
     * @return {@code true} when the mandatory properties are present.
     */
    public boolean isAuthenticationEnabled() {
        return StringUtils.hasText(appClientId) && StringUtils.hasText(getJwkSetUri());
    }
}
