package com.example.Tucasdesk.dtos;

import com.example.Tucasdesk.model.Perfil;
import com.example.Tucasdesk.model.Usuario;

/**
 * Data Transfer Object (DTO) for sending responses after a login attempt.
 * It includes a JWT, information about the authenticated user and a status message.
 */
public class LoginResponseDTO {
    /**
     * The JWT generated upon successful authentication.
     */
    private final String token;
    /**
     * Information about the authenticated user.
     */
    private final UsuarioInfo usuario;
    /**
     * A message indicating the result of the login attempt (e.g., "Login successful").
     */
    private final String message;

    /**
     * Constructs a new LoginResponseDTO.
     *
     * @param token    The authentication token.
     * @param usuario  The authenticated user.
     * @param message  A response message.
     */
    public LoginResponseDTO(String token, Usuario usuario, String message) {
        this.token = token;
        this.usuario = usuario != null ? new UsuarioInfo(usuario) : null;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public UsuarioInfo getUsuario() {
        return usuario;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Simplified representation of the authenticated user returned after login.
     */
    public static class UsuarioInfo {
        private final Integer id;
        private final String nome;
        private final String email;
        private final String role;

        public UsuarioInfo(Usuario usuario) {
            this.id = usuario.getIdUsuario();
            this.nome = usuario.getNome();
            this.email = usuario.getEmail();
            this.role = extrairRole(usuario.getPerfil());
        }

        public Integer getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }

        private static String extrairRole(Perfil perfil) {
            return perfil != null ? perfil.getNome() : null;
        }
    }
}
