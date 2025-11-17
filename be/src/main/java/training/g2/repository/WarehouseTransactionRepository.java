package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import training.g2.model.WarehouseTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface WarehouseTransactionRepository extends JpaRepository<WarehouseTransaction, Long>, JpaSpecificationExecutor<WarehouseTransaction> {

    Page<WarehouseTransaction> findByProductVariantIdOrderByCreatedAtDesc(long variantId, Pageable pageable);
}
