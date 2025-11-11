package com.example.Tucasdesk.security;

import java.text.Normalizer;
import java.util.Locale;

import org.springframework.util.StringUtils;

/**
 * Utility methods for converting domain specific profile names into Spring Security authorities.
 */
public final class AuthorityUtils {

    private AuthorityUtils() {
    }

    /**
     * Builds a {@code ROLE_*} authority string based on the provided profile name.
     *
     * @param profileName the persisted profile name.
     * @return the authority string, defaulting to {@code ROLE_USUARIO} when the profile name is empty.
     */
    public static String createRoleAuthority(String profileName) {
        if (!StringUtils.hasText(profileName)) {
            return "ROLE_USUARIO";
        }

        String normalized = Normalizer.normalize(profileName, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^\\p{Alnum}]+", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_", "")
                .replaceAll("_$", "");

        if (!StringUtils.hasText(normalized)) {
            return "ROLE_USUARIO";
        }

        return "ROLE_" + normalized.toUpperCase(Locale.ROOT);
    }
}
