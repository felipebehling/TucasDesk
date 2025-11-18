package com.example.Tucasdesk.security;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.example.Tucasdesk.config.AwsCognitoProperties;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminSetUserPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserResponse;
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

    /**
     * Authenticates a user with the given credentials against the Cognito User Pool.
     *
     * @param username The user's username (email).
     * @param password The user's password.
     * @return A {@link CognitoAuthenticationResult} containing the authentication tokens.
     * @throws ResponseStatusException if authentication fails.
     */
    public CognitoAuthenticationResult authenticate(String username, String password) {
        try {
            AdminInitiateAuthResponse response = cognitoClient.adminInitiateAuth(AdminInitiateAuthRequest.builder()
                    .userPoolId(properties.getUserPoolId())
                    .clientId(properties.getAppClientId())
                    .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                    .authParameters(Map.of("USERNAME", username, "PASSWORD", password))
                    .build());
            AuthenticationResultType result = response.authenticationResult();
            if (result == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos.");
            }
            return new CognitoAuthenticationResult(result.idToken(), result.accessToken(), result.refreshToken(), username);
        } catch (NotAuthorizedException | UserNotFoundException ex) {
            log.warn("event=cognito_authenticate status=unauthorized username={}", username);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos.");
        } catch (CognitoIdentityProviderException ex) {
            log.error("event=cognito_authenticate status=error username={} message=\"{}\"", username, ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Não foi possível autenticar no serviço Cognito.");
        }
    }

    /**
     * Refreshes the authentication tokens using a refresh token.
     *
     * @param refreshToken The refresh token to use.
     * @return A new {@link CognitoAuthenticationResult} with refreshed tokens.
     * @throws ResponseStatusException if the refresh token is invalid or expired.
     */
    public CognitoAuthenticationResult refreshToken(String refreshToken) {
        if (!StringUtils.hasText(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um refresh token válido.");
        }

        try {
            AdminInitiateAuthResponse response = cognitoClient.adminInitiateAuth(AdminInitiateAuthRequest.builder()
                    .userPoolId(properties.getUserPoolId())
                    .clientId(properties.getAppClientId())
                    .authFlow(AuthFlowType.REFRESH_TOKEN_AUTH)
                    .authParameters(Map.of("REFRESH_TOKEN", refreshToken))
                    .build());

            AuthenticationResultType result = response.authenticationResult();
            if (result == null || !StringUtils.hasText(result.idToken())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido.");
            }

            String returnedRefreshToken = StringUtils.hasText(result.refreshToken())
                    ? result.refreshToken()
                    : refreshToken;
            String username = resolveUsernameFromAccessToken(result.accessToken());

            return new CognitoAuthenticationResult(result.idToken(), result.accessToken(), returnedRefreshToken, username);
        } catch (NotAuthorizedException ex) {
            log.warn("event=cognito_refresh status=unauthorized message=\"{}\"", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido.");
        } catch (CognitoIdentityProviderException ex) {
            log.error("event=cognito_refresh status=error message=\"{}\"", ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Não foi possível renovar o token no serviço Cognito.");
        }
    }

    private String resolveUsernameFromAccessToken(String accessToken) {
        if (!StringUtils.hasText(accessToken)) {
            return null;
        }

        try {
            GetUserResponse userResponse = cognitoClient.getUser(GetUserRequest.builder()
                    .accessToken(accessToken)
                    .build());

            if (userResponse == null) {
                return null;
            }

            if (userResponse.userAttributes() == null || userResponse.userAttributes().isEmpty()) {
                return StringUtils.hasText(userResponse.username()) ? userResponse.username() : null;
            }

            Optional<String> email = userResponse.userAttributes().stream()
                    .filter(attribute -> "email".equals(attribute.name()))
                    .map(AttributeType::value)
                    .filter(StringUtils::hasText)
                    .findFirst();

            return email.orElseGet(() -> StringUtils.hasText(userResponse.username()) ? userResponse.username() : null);
        } catch (CognitoIdentityProviderException ex) {
            log.warn("event=cognito_resolve_user status=error message=\"{}\"", ex.getMessage());
            return null;
        }
    }

    /**
     * Registers a new user in the Cognito User Pool.
     *
     * @param email    The user's email, which will also be their username.
     * @param password The user's password.
     * @throws ResponseStatusException if the user already exists or if registration fails.
     */
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
            log.debug("event=cognito_register status=password_set username={}", email);
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

    /**
     * Updates the password for a user in the Cognito User Pool.
     *
     * @param email       The email of the user whose password is to be updated.
     * @param newPassword The new password for the user.
     * @throws ResponseStatusException if the user is not found or the password update fails.
     */
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

    /**
     * Adds a user to a specified group in the Cognito User Pool.
     *
     * @param username  The username of the user to add to the group.
     * @param groupName The name of the group to add the user to.
     * @throws ResponseStatusException if the user is not found or the group assignment fails.
     */
    public void addUserToGroup(String username, String groupName) {
        if (!org.springframework.util.StringUtils.hasText(groupName)) {
            return;
        }
        try {
            cognitoClient.adminAddUserToGroup(AdminAddUserToGroupRequest.builder()
                    .userPoolId(properties.getUserPoolId())
                    .username(username)
                    .groupName(groupName)
                    .build());
            log.info("event=cognito_assign_group status=success username={} group={}", username, groupName);
        } catch (UserNotFoundException ex) {
            log.warn("event=cognito_assign_group status=user_not_found username={} group={} message=\"{}\"", username,
                    groupName, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Usuário não encontrado no serviço Cognito para atribuição de grupo.");
        } catch (CognitoIdentityProviderException ex) {
            log.error("event=cognito_assign_group status=error username={} group={} message=\"{}\"", username,
                    groupName, ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Não foi possível atualizar o grupo do usuário no serviço Cognito.");
        }
    }
}
