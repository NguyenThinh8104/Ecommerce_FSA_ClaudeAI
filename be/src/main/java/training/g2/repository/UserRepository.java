package training.g2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import training.g2.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmailAndDeletedFalse(String email);

    Optional<User> findByIdNot(Long id);

    boolean existsByEmailAndDeletedFalse(String email);

    boolean existsByPhoneAndDeletedFalse(String phone);

    Optional<User> findUserById(Long id);

    long countByDeletedFalse();

}