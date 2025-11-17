package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training.g2.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
