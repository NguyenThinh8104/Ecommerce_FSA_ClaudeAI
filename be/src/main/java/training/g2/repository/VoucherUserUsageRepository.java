package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import training.g2.model.User;
import training.g2.model.Voucher;
import training.g2.model.VoucherUserUsage;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherUserUsageRepository extends JpaRepository<VoucherUserUsage, Long> {

    Optional<VoucherUserUsage> findByVoucherAndUser(Voucher voucher, User user);

    List<VoucherUserUsage> findByVoucher(Voucher voucher);


   
    void deleteAllByVoucher(Voucher voucher);
}
