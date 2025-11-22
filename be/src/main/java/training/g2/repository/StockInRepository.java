package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training.g2.model.StockIn;

public interface StockInRepository extends JpaRepository<StockIn, Long> {
}
