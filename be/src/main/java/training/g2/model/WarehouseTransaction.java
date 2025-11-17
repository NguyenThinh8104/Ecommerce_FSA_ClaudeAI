package training.g2.model;

import jakarta.persistence.*;
import lombok.*;
import training.g2.model.enums.TransactionTypeEnum;
import training.g2.util.SecurityUtil;

import java.time.LocalDateTime;

@Entity
@Table (name = "warehouse_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseTransaction {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant productVariant;

    private int quantity;

    @Column (name = "balance_after")
    private int balanceAfter;

    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum type;

    @Column (name = "reference_code")
    private String referenceCode;

    private String note;

    private LocalDateTime createdAt;
    private String createdBy;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = LocalDateTime.now();
        this.createdBy = SecurityUtil.getCurrentUserLogin().orElse("SYSTEM");
    }
}
