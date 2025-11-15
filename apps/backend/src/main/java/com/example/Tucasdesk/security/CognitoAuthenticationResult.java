package com.example.Tucasdesk.security;

/**
 * Value object that represents the tokens returned by AWS Cognito after a successful authentication.
 */
public class CognitoAuthenticationResult {

    private final String idToken;
    private final String accessToken;
    private final String refreshToken;
    private final String username;

    public CognitoAuthenticationResult(String idToken, String accessToken, String refreshToken, String username) {
        this.idToken = idToken;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
    }

    public CognitoAuthenticationResult(String idToken, String accessToken, String refreshToken) {
        this(idToken, accessToken, refreshToken, null);
    }

    public String getIdToken() {
        return idToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getUsername() {
        return username;
    }
}
