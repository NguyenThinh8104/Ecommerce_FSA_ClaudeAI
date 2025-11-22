package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training.g2.model.StockInItem;

public interface StockInItemRepository extends JpaRepository<StockInItem, Integer> {
}
