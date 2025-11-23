package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import training.g2.model.CartDetail;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {

    @Modifying
    @Transactional
    @Query("""
            DELETE FROM CartDetail c
            WHERE c.cart.user.id = :userId
              AND c.productVariant.id = :variantId
            """)
    void deleteByUserIdAndVariantId(@Param("userId") Long userId,
            @Param("variantId") Long variantId);

}
