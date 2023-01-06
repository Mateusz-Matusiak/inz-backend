package com.example.backend.mail.activation.token;

import com.example.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivationTokenService {

    private final ActivationTokenRepository tokenRepository;
    private final UserRepository userRepository;

    public String createActivationToken(String email) {
        final String token = generateToken();

        userRepository.findByEmail(email).map(userEntity -> tokenRepository.save(
                new ActivationTokenEntity(token, LocalDateTime.now().plusDays(1), userEntity)));
        return token;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Transactional
    public void deleteOldTokensForUser(String email, String token) {
        tokenRepository.deleteAllByUser(email, token);
    }

    @Scheduled(fixedRateString = "PT1H")
    @Transactional
    public void deleteExpiredTokens() {
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}
