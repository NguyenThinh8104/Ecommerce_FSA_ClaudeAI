package training.g2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_in")
@Getter
@Setter
public class StockIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "product_variant_id")
    private long productVariantId;

    private int quantity;
    private double price;
    private double total;
    private LocalDateTime createdAt;
    private String createdBy;
    private String status;
    private String note;

    @OneToOne
    @JoinColumn(name = "id")
    private Inventory inventory;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.createdBy = SecurityContextHolder.getContext().getAuthentication().getName();
        this.total = this.price * this.quantity;

    }
}
