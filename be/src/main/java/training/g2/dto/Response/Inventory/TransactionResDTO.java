package training.g2.dto.Response.Inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import training.g2.model.enums.TransactionTypeEnum;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionResDTO {
    private Long id;
    private String variantSKU;
    private String productName;
    private int quantityChange;
    private int balanceAfter;
    private TransactionTypeEnum transactionType;
    private String referenceCode;
    private String note;
    private String createdBy;
    @JsonFormat (pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
}
