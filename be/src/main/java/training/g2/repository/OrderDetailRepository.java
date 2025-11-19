package training.g2.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import training.g2.model.OrderDetail;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query ("SELECT p.name, SUM(od.quantity), SUM(od.price * od.quantity) " +
            "FROM OrderDetail od " +
            "JOIN od.productVariant pv " +
            "JOIN pv.product p " +
            "JOIN od.order o " +
            "WHERE o.paymentStatus ='PAID' " +
            "GROUP BY p.id, p.name " +
            "ORDER BY SUM (od.quantity) DESC ")
    List<Object[]> findTopSellingProducts(Pageable pageable);
}
