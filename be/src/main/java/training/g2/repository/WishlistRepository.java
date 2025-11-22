package training.g2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import training.g2.model.ProductVariant;
import training.g2.model.User;
import training.g2.model.Wishlist;

import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long>, JpaSpecificationExecutor<Wishlist> {

    Page<Wishlist> findByUser(User user, Pageable pageable);

    Optional<Wishlist> findByUserAndProductVariant(User user, ProductVariant productVariant);

    boolean existsByUserAndProductVariant(User user, ProductVariant productVariant);

    void deleteByUserAndProductVariant(User user, ProductVariant productVariant);
}
