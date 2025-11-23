package training.g2.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import training.g2.model.ProductVariant;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
  @Query("""
      select pv from ProductVariant pv
      where pv.product.id=:productId
      and pv.deleted = false
      and pv.product.deleted = false
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

  @Query("""
        select v
        from ProductVariant v
        join v.product p
        join p.category c
        where p.deleted = false
          and c.deleted = false
          and v.deleted = false
      """)
  Page<ProductVariant> findByDeletedFalseAndProduct_DeletedFalse(Pageable pageable);

  @Query("""
          SELECT v FROM ProductVariant v
          JOIN v.product p
          JOIN p.category c
          WHERE v.deleted = false
            AND p.deleted = false
            AND c.deleted = false
            AND c.id = :categoryId
            AND (
                  (:r0_5 = false AND :r5_15 = false AND :r15Plus = false)
               OR (:r0_5 = true    AND v.price >= 0       AND v.price <= 5000000)
               OR (:r5_15 = true   AND v.price >  5000000 AND v.price <= 15000000)
               OR (:r15Plus = true AND v.price >  15000000)
            )
            AND (
                  :attributeValues IS NULL
               OR EXISTS (
                      SELECT 1 FROM v.values val
                      WHERE val.value IN :attributeValues
                  )
            )
      """)
  Page<ProductVariant> findByCategoryWithMultiPriceAndAttributes(
      @Param("categoryId") Long categoryId,
      @Param("r0_5") boolean r0_5,
      @Param("r5_15") boolean r5_15,
      @Param("r15Plus") boolean r15Plus,
      @Param("attributeValues") List<String> attributeValues,
      Pageable pageable);

}
