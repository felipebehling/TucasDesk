package com.example.Tucasdesk.mappers;

import com.example.Tucasdesk.dtos.AuthenticatedUserDTO;
import com.example.Tucasdesk.model.Perfil;
import com.example.Tucasdesk.model.Usuario;

/**
 * Utility class responsible for mapping {@link Usuario} entities to their corresponding DTOs.
 */
public final class UsuarioMapper {

    private UsuarioMapper() {
        // Utility class
    }

    /**
     * Converts a {@link Usuario} entity into an {@link AuthenticatedUserDTO}.
     *
     * @param usuario the user entity to convert.
     * @return the DTO populated with the user's public information.
     */
    public static AuthenticatedUserDTO toAuthenticatedUserDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        Perfil perfil = usuario.getPerfil();
        String role = perfil != null ? perfil.getNome() : null;

        return new AuthenticatedUserDTO(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getEmail(),
                role
        );
    }
}
