package training.g2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import training.g2.model.Attribute;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    boolean existsByCodeOrName(String code, String name);

    boolean existsByCodeOrNameAndIdNot(String code, String name, long id);

    @Query("""
                SELECT DISTINCT a
                FROM Attribute a
                LEFT JOIN FETCH a.values
                WHERE a.product.id = :productId
                AND a.deleted = false
                ORDER BY a.id ASC
            """)
    List<Attribute> findAllAttributeByProductId(@Param("productId") long productId);

    @Query("""
                SELECT DISTINCT a FROM Attribute a
                JOIN a.product p
                JOIN p.category c
                WHERE c.id = :categoryId
                  AND a.deleted = false
                  AND p.deleted = false
                  AND c.deleted = false
            """)
    List<Attribute> findAllByCategoryId(@Param("categoryId") Long categoryId);

}
