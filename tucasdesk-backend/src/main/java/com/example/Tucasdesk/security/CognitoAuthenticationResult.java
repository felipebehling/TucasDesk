package com.example.Tucasdesk.security;

/**
 * Value object that represents the tokens returned by AWS Cognito after a successful authentication.
 */
public record CognitoAuthenticationResult(String idToken, String accessToken, String refreshToken) {
}
