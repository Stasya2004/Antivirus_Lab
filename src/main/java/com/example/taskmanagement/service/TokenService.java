package com.example.taskmanagement.service;

import com.example.taskmanagement.model.SessionStatus;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.model.UserSession;
import com.example.taskmanagement.repository.UserSessionRepository;
import com.example.taskmanagement.security.JwtTokenProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TokenService {

    private final UserSessionRepository sessionRepository;
    private final JwtTokenProvider tokenProvider;

    public TokenService(UserSessionRepository sessionRepository, JwtTokenProvider tokenProvider) {
        this.sessionRepository = sessionRepository;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public TokenPair createTokenPair(UserDetails userDetails, User user) {
        // Временно создаём сессию без sessionId в токене
        String tempRefreshToken = tokenProvider.generateRefreshToken(userDetails, null);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusSeconds(tokenProvider.getRefreshExpirationMs() / 1000);

        UserSession session = new UserSession(user, tempRefreshToken, now, expiresAt);
        session = sessionRepository.save(session);

        // Генерируем финальный refresh-токен с реальным sessionId
        String refreshToken = tokenProvider.generateRefreshToken(userDetails, session.getId());
        session.setRefreshToken(refreshToken);
        sessionRepository.save(session);

        String accessToken = tokenProvider.generateAccessToken(userDetails, user.getId(), user.getRole().name());

        return new TokenPair(accessToken, refreshToken);
    }

    @Transactional
    public TokenPair refreshTokens(String refreshToken) {
        if (!tokenProvider.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        UserSession session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new RuntimeException("Session is not active");
        }

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            session.setStatus(SessionStatus.EXPIRED);
            sessionRepository.save(session);
            throw new RuntimeException("Refresh token expired");
        }

        User user = session.getUser();
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();

        // Отзываем старую сессию
        session.setStatus(SessionStatus.REVOKED);
        sessionRepository.save(session);

        // Создаём новую пару токенов
        return createTokenPair(userDetails, user);
    }

    @Transactional
    public void revokeSession(String refreshToken) {
        sessionRepository.findByRefreshToken(refreshToken)
                .ifPresent(session -> {
                    session.setStatus(SessionStatus.REVOKED);
                    sessionRepository.save(session);
                });
    }

    public static class TokenPair {
        private final String accessToken;
        private final String refreshToken;

        public TokenPair(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        public String getAccessToken() { return accessToken; }
        public String getRefreshToken() { return refreshToken; }
    }
}