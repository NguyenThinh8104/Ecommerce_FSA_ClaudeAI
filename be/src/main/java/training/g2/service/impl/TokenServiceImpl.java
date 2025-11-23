package training.g2.service.impl;

import static training.g2.constant.Constants.UserExceptionInformation.USER_NOT_FOUND;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import training.g2.exception.common.BusinessException;
import training.g2.model.Token;
import training.g2.model.User;
import training.g2.model.enums.TokenTypeEnum;
import training.g2.repository.TokenRepository;
import training.g2.service.TokenService;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    public String generatePasswordUpdateToken(User user) {

        String token = UUID.randomUUID().toString();

        Token t = new Token();
        t.setToken(token);
        t.setUser(user);
        t.setType(TokenTypeEnum.UPDATE_PASSWORD_TOKEN);
        t.setExpiredAt(Instant.now().plusSeconds(10 * 60));

        tokenRepository.save(t);
        return token;
    }

    public User validateToken(String token) {
        Token t = tokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException("Token không hợp lệ"));

        if (t.getExpiredAt().isBefore(Instant.now())) {
            throw new BusinessException("Token đã hết hạn");
        }

        return t.getUser();
    }

    // Code cũ của tôi đang chạy đc ko ai đụng tới thích thì viết cái mới

    @Override
    public Token saveNewToken(User user, String refreshToken, TokenTypeEnum type) {
        tokenRepository.deleteByUserAndType(user, type);
        Token entity = new Token();
        entity.setUser(user);
        entity.setToken(refreshToken);
        entity.setType(type);

        return tokenRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Token> getByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void deleteByToken(User user) {
        tokenRepository.deleteByUser(user);
    }

    @Override
    public Token rotateRefreshToken(User user, String newRefreshToken) {
        tokenRepository.deleteByUserAndType(user, TokenTypeEnum.REFRESH_TOKEN);
        Token entity = new Token();
        entity.setUser(user);
        entity.setToken(newRefreshToken);
        entity.setType(TokenTypeEnum.REFRESH_TOKEN);
        return tokenRepository.save(entity);
    }

    @Override
    public User getUserByToken(String token) {
        User user = tokenRepository.findUserByToken(token).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        return user;
    }

    @Override
    public void deleteToken(String token) {
        tokenRepository.deleteByToken(token);
    }
}
