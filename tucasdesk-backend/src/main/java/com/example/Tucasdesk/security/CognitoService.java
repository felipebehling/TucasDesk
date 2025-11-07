package com.example.Tucasdesk.security;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.Tucasdesk.config.AwsCognitoProperties;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminSetUserPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InvalidPasswordException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.MessageActionType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.NotAuthorizedException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserNotFoundException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UsernameExistsException;

/**
 * Wraps Cognito API interactions used by the application.
 */
@Service
public class CognitoService {

    private static final Logger log = LoggerFactory.getLogger(CognitoService.class);

    private final CognitoIdentityProviderClient cognitoClient;
    private final AwsCognitoProperties properties;

    public CognitoService(CognitoIdentityProviderClient cognitoClient, AwsCognitoProperties properties) {
        this.cognitoClient = cognitoClient;
        this.properties = properties;
    }

    public CognitoAuthenticationResult authenticate(String username, String password) {
        try {
            AdminInitiateAuthResponse response = cognitoClient.adminInitiateAuth(AdminInitiateAuthRequest.builder()
                    .userPoolId(properties.getUserPoolId())
                    .clientId(properties.getAppClientId())
                    .authFlow("ADMIN_USER_PASSWORD_AUTH")
                    .authParameters(Map.of("USERNAME", username, "PASSWORD", password))
                    .build());
            AuthenticationResultType result = response.authenticationResult();
            if (result == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos.");
            }
            return new CognitoAuthenticationResult(result.idToken(), result.accessToken(), result.refreshToken());
        } catch (NotAuthorizedException | UserNotFoundException ex) {
            log.warn("event=cognito_authenticate status=unauthorized username={}", username);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos.");
        } catch (CognitoIdentityProviderException ex) {
            log.error("event=cognito_authenticate status=error username={} message=\"{}\"", username, ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Não foi possível autenticar no serviço Cognito.");
        }
    }

    public void registerUser(String email, String password) {
        try {
            AdminCreateUserResponse createResponse = cognitoClient.adminCreateUser(AdminCreateUserRequest.builder()
                    .userPoolId(properties.getUserPoolId())
                    .username(email)
                    .userAttributes(
                            AttributeType.builder().name("email").value(email).build(),
                            AttributeType.builder().name("email_verified").value("true").build())
                    .messageAction(MessageActionType.SUPPRESS)
                    .build());
            log.info("event=cognito_register status=created username={}", createResponse.user().username());
            cognitoClient.adminSetUserPassword(AdminSetUserPasswordRequest.builder()
                    .userPoolId(properties.getUserPoolId())
                    .username(email)
                    .password(password)
                    .permanent(true)
                    .build());
        } catch (UsernameExistsException ex) {
            log.warn("event=cognito_register status=conflict username={}", email);
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe um usuário cadastrado com este e-mail no Cognito.");
        } catch (InvalidPasswordException ex) {
            log.warn("event=cognito_register status=invalid_password username={} message=\"{}\"", email, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A senha não atende aos critérios configurados no Cognito.");
        } catch (CognitoIdentityProviderException ex) {
            log.error("event=cognito_register status=error username={} message=\"{}\"", email, ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Não foi possível registrar o usuário no serviço Cognito.");
        }
    }

    public void updatePassword(String email, String newPassword) {
        try {
            cognitoClient.adminSetUserPassword(AdminSetUserPasswordRequest.builder()
                    .userPoolId(properties.getUserPoolId())
                    .username(email)
                    .password(newPassword)
                    .permanent(true)
                    .build());
        } catch (InvalidPasswordException ex) {
            log.warn("event=cognito_update_password status=invalid_password username={} message=\"{}\"", email,
                    ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A nova senha não atende aos critérios configurados no Cognito.");
        } catch (UserNotFoundException ex) {
            log.warn("event=cognito_update_password status=user_not_found username={}", email);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado no Cognito.");
        } catch (CognitoIdentityProviderException ex) {
            log.error("event=cognito_update_password status=error username={} message=\"{}\"", email, ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Não foi possível atualizar a senha no serviço Cognito.");
        }
    }
}
