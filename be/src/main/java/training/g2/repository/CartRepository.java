package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training.g2.model.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(long userId);
}
