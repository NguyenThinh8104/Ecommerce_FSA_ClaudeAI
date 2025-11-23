package training.g2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import training.g2.model.enums.VoucherApplyScopeEnum;
import training.g2.model.enums.VoucherDiscountTypeEnum;
import training.g2.model.enums.VoucherStatusEnum;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "vouchers")
@Getter
@Setter
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(name = "image_url", columnDefinition = "NVARCHAR(2000)")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 20)
    private VoucherDiscountTypeEnum discountType;

    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "max_discount_amount", precision = 10, scale = 2)
    private BigDecimal maxDiscountAmount;

    @Column(name = "min_order_value", precision = 10, scale = 2)
    private BigDecimal minOrderValue = BigDecimal.ZERO;

    @Column(name = "usage_limit")
    private Integer usageLimit = 0; // 0 = không giới hạn

    @Column(name = "used_count")
    private Integer usedCount = 0;

    @Column(name = "user_limit")
    private Integer userLimit = 0; // 0 = không giới hạn / mỗi user

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private VoucherStatusEnum status = VoucherStatusEnum.INACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "apply_scope", length = 20)
    private VoucherApplyScopeEnum applyScope = VoucherApplyScopeEnum.ALL;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
    
    @Column(name = "is_delete")
    private boolean isDelete;
    
    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    // helper
    public boolean isExpiredByDate() {
        return endDate != null && endDate.isBefore(LocalDateTime.now());
    }
}

