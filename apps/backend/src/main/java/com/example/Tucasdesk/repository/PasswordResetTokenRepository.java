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

    Optional<PasswordResetToken> findByToken(String token);

    void deleteAllByUsuario(Usuario usuario);

    void deleteAllByExpiresAtBefore(Instant expiration);
}
