package training.g2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import training.g2.model.ProductVariant;


public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
        @Query("""
                        select pv from ProductVariant pv
                        where pv.product.id=:productId
                        and pv.deleted = false
                        """)
        Page<ProductVariant> findAllVariantsWithProductId(@Param("productId") long productId, Pageable pageable);

        @Query("""
                          select v.sku
                          from ProductVariant v
                          where v.product.id = :productId
                            and v.sku in :skus
                            and v.deleted = false
                        """)
        List<String> findSkusByProductAndSkuIn(@Param("productId") long productId,
                        @Param("skus") List<String> skus);

    @Override
    Optional<ProductVariant> findById(Long aLong);
}
