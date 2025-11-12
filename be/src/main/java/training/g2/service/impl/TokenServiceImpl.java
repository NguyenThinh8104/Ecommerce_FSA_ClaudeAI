package training.g2.service.impl;

import static training.g2.constant.Constants.UserExceptionInformation.USER_NOT_FOUND;

import java.util.Optional;

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
}
