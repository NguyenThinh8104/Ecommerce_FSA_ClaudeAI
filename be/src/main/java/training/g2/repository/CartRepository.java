package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import training.g2.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

}
