package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import training.g2.model.ProductImage;

@Repository
public interface VariantImageRepository extends JpaRepository<ProductImage, Long> {

}
