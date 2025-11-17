package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training.g2.model.Inventory;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductVariantId(Long id);
}
