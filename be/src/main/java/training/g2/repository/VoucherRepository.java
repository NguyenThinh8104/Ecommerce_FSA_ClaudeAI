package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import training.g2.model.Voucher;
import training.g2.model.enums.VoucherStatusEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    boolean existsByCode(String code);

    Optional<Voucher> findByCode(String code);

    List<Voucher> findByStatus(VoucherStatusEnum status);

    List<Voucher> findByEndDateBeforeAndStatusNot(LocalDateTime now, VoucherStatusEnum status);
}
