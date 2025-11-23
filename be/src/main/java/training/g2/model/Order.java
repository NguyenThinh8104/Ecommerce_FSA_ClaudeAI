package training.g2.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import training.g2.model.enums.OrderStatusEnum;
import training.g2.model.enums.PaymentMethodEnum;
import training.g2.model.enums.PaymentStatusEnum;

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

    // GHN INFO
    private String ghnOrderCode;
    private Integer ghnFee;

    private LocalDateTime ghnExpectedDelivery;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;

    private String uuid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderDetail> items;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

}
