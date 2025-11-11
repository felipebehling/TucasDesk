package com.example.Tucasdesk.mappers;

import com.example.Tucasdesk.dtos.AuthenticatedUserDTO;
import com.example.Tucasdesk.dtos.UsuarioResponseDTO;
import com.example.Tucasdesk.model.Perfil;
import com.example.Tucasdesk.model.Usuario;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Nullable
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

    /**
     * Converts a {@link Usuario} entity into a {@link UsuarioResponseDTO}.
     *
     * @param usuario the user entity to convert.
     * @return the DTO populated with non-sensitive user information.
     */
    @Nullable
    public static UsuarioResponseDTO toUsuarioResponseDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.getDataCriacao(),
                usuario.getAtivo()
        );
    }

    /**
     * Converts a collection of {@link Usuario} entities into a list of {@link UsuarioResponseDTO}s.
     *
     * @param usuarios the list of user entities to convert.
     * @return a list of DTOs populated with non-sensitive user information.
     */
    public static List<UsuarioResponseDTO> toUsuarioResponseDTOList(List<Usuario> usuarios) {
        if (usuarios == null) {
            return Collections.emptyList();
        }

        return usuarios.stream()
                .map(UsuarioMapper::toUsuarioResponseDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
