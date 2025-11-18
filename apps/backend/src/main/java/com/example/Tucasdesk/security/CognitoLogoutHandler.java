package com.example.Tucasdesk.security;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.Tucasdesk.config.AwsCognitoProperties;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

/**
 * Logout success handler that redirects users to the Cognito hosted UI logout
 * endpoint.
 */
@Component
public class CognitoLogoutHandler extends SimpleUrlLogoutSuccessHandler {

    private final AwsCognitoProperties cognitoProperties;
    private final String loginPageUrl;

    public CognitoLogoutHandler(AwsCognitoProperties cognitoProperties,
            @Value("${app.frontend.login-url:http://localhost:5173/login}") String loginPageUrl) {
        this.cognitoProperties = cognitoProperties;
        this.loginPageUrl = loginPageUrl;
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
        String domain = cognitoProperties.getDomain();
        String clientId = cognitoProperties.getAppClientId();

        if (!StringUtils.hasText(domain) || !StringUtils.hasText(clientId)) {
            return loginPageUrl;
        }

        return UriComponentsBuilder
                .fromUri(URI.create(domain + "/logout"))
                .queryParam("client_id", clientId)
                .queryParam("logout_uri", loginPageUrl)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUriString();
    }
}
