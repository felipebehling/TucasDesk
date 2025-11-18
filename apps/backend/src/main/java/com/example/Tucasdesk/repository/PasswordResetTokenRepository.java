package com.example.Tucasdesk.repository;

import com.example.Tucasdesk.model.PasswordResetToken;
import com.example.Tucasdesk.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

/**
 * Repository used for persisting password reset tokens.
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    /**
     * Finds a password reset token by its token string.
     *
     * @param token The token string to search for.
     * @return An {@link Optional} containing the token if found, or empty otherwise.
     */
    Optional<PasswordResetToken> findByToken(String token);

    /**
     * Deletes all password reset tokens associated with a specific user.
     *
     * @param usuario The user whose tokens should be deleted.
     */
    void deleteAllByUsuario(Usuario usuario);

    /**
     * Deletes all password reset tokens that expired before a given timestamp.
     *
     * @param expiration The expiration timestamp.
     */
    void deleteAllByExpiresAtBefore(Instant expiration);
}
