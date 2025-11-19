package training.g2.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import training.g2.model.Order;
import training.g2.model.enums.PaymentStatusEnum;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
@Query ("SELECT SUM (o.totalPrice) FROM Order o WHERE o.paymentStatus = :status")
    Double calulateTotalRevenue(PaymentStatusEnum status);

@Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
List<Order> findRecentOrders(Pageable pageable);

@Query("SELECT YEAR (o.createdAt), MONTH (o.createdAt), SUM (o.totalPrice) FROM Order o " +
        "WHERE o.paymentStatus = 'PAID'" +
        "GROUP BY YEAR (o.createdAt), MONTH (o.createdAt)" +
        "ORDER BY YEAR (o.createdAt) ASC, MONTH (o.createdAt) ASC")
    List<Object[]> getMonthlyRevenue();
}
