package training.g2.service;

import java.util.Optional;

import training.g2.model.Token;
import training.g2.model.User;
import training.g2.model.enums.TokenTypeEnum;

public interface TokenService {
    String generatePasswordUpdateToken(User user);

    User validateToken(String token);

    void deleteToken(String token);

    /** Lưu refresh token mới cho user */
    Token saveNewToken(User user, String token, TokenTypeEnum type);

    Optional<Token> getByToken(String token);

    void deleteByToken(User user);

    User getUserByToken(String token);

    /** Thay (rotate) token cũ của user bằng token mới */
    Token rotateRefreshToken(User user, String newRefreshToken);
}
