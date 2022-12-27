package com.example.backend.mail.activation.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ActivationTokenRepository extends JpaRepository<ActivationTokenEntity, String> {


    @Modifying
    @Query("DELETE FROM ActivationTokenEntity token WHERE token.user.email = ?1 AND NOT token.token = ?2")
    void deleteAllByUser(String user, String newToken);

    @Modifying
    @Query("DELETE FROM ActivationTokenEntity where expireTime < ?1")
    void deleteExpiredTokens(LocalDateTime dateTime);
}
