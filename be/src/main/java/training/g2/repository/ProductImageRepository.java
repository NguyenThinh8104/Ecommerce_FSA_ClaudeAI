package training.g2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import training.g2.model.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    @Query("""
            select i from ProductImage i
            where i.product.id = :productId
            """)
    List<ProductImage> findAllProductImage(@Param("productId") long productId);
}
