package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training.g2.model.Stock;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProductVariantId(Long productVariantId);
}
