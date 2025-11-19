package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import training.g2.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    boolean existsByNameAndDeletedFalse(String name);

    boolean existsByCodeAndDeletedFalse(String code);

    long countByDeletedFalse();
}
