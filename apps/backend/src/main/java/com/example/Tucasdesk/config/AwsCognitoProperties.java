package com.example.Tucasdesk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * Configuration properties used to integrate the application with AWS Cognito.
 */
@ConfigurationProperties(prefix = "aws.cognito")
public class AwsCognitoProperties {

    /**
     * The AWS region where the Cognito User Pool is provisioned.
     */
    private String region;

    /**
     * The ID of the Cognito User Pool.
     */
    private String userPoolId;

    /**
     * The ID of the App Client configured in the User Pool.
     */
    private String appClientId;

    /**
     * The issuer URI of the User Pool, used for token validation.
     * If not provided, it is derived from the region and user pool ID.
     */
    private String issuerUri;

    /**
     * The JWK Set URI, used to fetch public keys for token signature validation.
     * If not provided, it is derived from the issuer URI.
     */
    private String jwkSetUri;

    /**
     * The JWT claim that contains user profile information (e.g., user groups).
     * Defaults to "cognito:groups".
     */
    private String profileClaim = "cognito:groups";

    /**
     * The domain associated with the Cognito Hosted UI.
     */
    private String domain;

    /**
     * @return The AWS region.
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region The AWS region.
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return The User Pool ID.
     */
    public String getUserPoolId() {
        return userPoolId;
    }

    /**
     * @param userPoolId The User Pool ID.
     */
    public void setUserPoolId(String userPoolId) {
        this.userPoolId = userPoolId;
    }

    /**
     * @return The App Client ID.
     */
    public String getAppClientId() {
        return appClientId;
    }

    /**
     * @param appClientId The App Client ID.
     */
    public void setAppClientId(String appClientId) {
        this.appClientId = appClientId;
    }

    /**
     * Returns the issuer URI. If not explicitly set, it constructs the URI
     * from the region and user pool ID.
     *
     * @return The issuer URI for the Cognito User Pool.
     */
    public String getIssuerUri() {
        if (StringUtils.hasText(issuerUri)) {
            return issuerUri;
        }
        if (StringUtils.hasText(region) && StringUtils.hasText(userPoolId)) {
            return String.format("https://cognito-idp.%s.amazonaws.com/%s", region, userPoolId);
        }
        return null;
    }

    /**
     * @param issuerUri The issuer URI.
     */
    public void setIssuerUri(String issuerUri) {
        this.issuerUri = issuerUri;
    }

    /**
     * Returns the JWK Set URI. If not explicitly set, it derives the URI
     * from the issuer URI.
     *
     * @return The JWK Set URI for the Cognito User Pool.
     */
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

    /**
     * @param jwkSetUri The JWK Set URI.
     */
    public void setJwkSetUri(String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
    }

    /**
     * @return The profile claim name.
     */
    public String getProfileClaim() {
        return profileClaim;
    }

    /**
     * @param profileClaim The profile claim name.
     */
    public void setProfileClaim(String profileClaim) {
        this.profileClaim = profileClaim;
    }

    /**
     * @return The Cognito domain.
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain The Cognito domain.
     */
    public void setDomain(String domain) {
        this.domain = domain;
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
