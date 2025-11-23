package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training.g2.model.StockIn;
import training.g2.model.StockInItem;

import java.util.List;

public interface StockInItemRepository extends JpaRepository<StockInItem, Long> {
}
