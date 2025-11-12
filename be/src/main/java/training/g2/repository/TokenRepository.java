package training.g2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import training.g2.model.Token;
import training.g2.model.User;
import training.g2.model.enums.TokenTypeEnum;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    boolean existsByToken(String token);

    void deleteByUser(User user);

    void deleteByUserAndType(User user, TokenTypeEnum type);

    Optional<Token> findFirstByUserAndTypeOrderByCreatedAtDesc(User user, TokenTypeEnum type);

    @Query("SELECT t.user FROM Token t WHERE t.token = :token")
    Optional<User> findUserByToken(@Param("token") String token);

}
