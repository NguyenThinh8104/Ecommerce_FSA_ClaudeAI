package training.g2.dto.Request.Voucher;

import lombok.Getter;
import lombok.Setter;
import training.g2.model.enums.VoucherApplyScopeEnum;
import training.g2.model.enums.VoucherDiscountTypeEnum;
import training.g2.model.enums.VoucherStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class VoucherReqDTO {
    private Long id; 

    private String code;
    private String imageUrl;
    private VoucherDiscountTypeEnum discountType;
    private BigDecimal discountValue;
    private BigDecimal maxDiscountAmount;
    private BigDecimal minOrderValue;
    private Integer usageLimit;
    private Integer userLimit;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private VoucherStatusEnum status;
    private VoucherApplyScopeEnum applyScope;

   
    private List<String> assignedUserEmails;
}
