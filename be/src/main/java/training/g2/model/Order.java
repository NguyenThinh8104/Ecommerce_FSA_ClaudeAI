package training.g2.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import training.g2.model.enums.OrderStatusEnum;
import training.g2.model.enums.PaymentMethodEnum;
import training.g2.model.enums.PaymentStatusEnum;
import training.g2.model.enums.ShippingStatusEnum;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double totalPrice;
    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatusEnum paymentStatus;

    @Enumerated(EnumType.STRING)
    private ShippingStatusEnum shippingStatus;
    private String shippingProvider;
    private String tracking_code;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

}
