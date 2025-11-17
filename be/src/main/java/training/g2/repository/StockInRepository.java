package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import training.g2.model.StockIn;

@Repository
public interface StockInRepository extends JpaRepository<StockIn, Long> {
}
