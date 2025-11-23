package training.g2.dto.Response.Voucher;

import lombok.Getter;
import lombok.Setter;
import training.g2.model.enums.VoucherApplyScopeEnum;
import training.g2.model.enums.VoucherDiscountTypeEnum;
import training.g2.model.enums.VoucherStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class VoucherResDTO {

    private Long id;
    private String code;
    private String imageUrl;
    private VoucherDiscountTypeEnum discountType;
    private BigDecimal discountValue;
    private BigDecimal maxDiscountAmount;
    private BigDecimal minOrderValue;
    private Integer usageLimit;
    private Integer usedCount;
    private Integer userLimit;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private VoucherStatusEnum status;
    private VoucherApplyScopeEnum applyScope;

    // cho admin xem nhanh
    private Integer remainingUsage; // usageLimit - usedCount (nếu usageLimit > 0)
}
