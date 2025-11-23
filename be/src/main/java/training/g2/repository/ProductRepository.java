package training.g2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import training.g2.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    boolean existsByNameAndDeletedFalse(String name);

    boolean existsByCodeAndDeletedFalse(String code);

    @Query("""
            Select p from Product p
            join p.category c
            where p.deleted = false
             and  c.deleted = false
             and p.category.id = :cateId
            """)
    List<Product> findAllProductByCategory(@Param("cateId") long cateId);

    long countByDeletedFalse();
}
