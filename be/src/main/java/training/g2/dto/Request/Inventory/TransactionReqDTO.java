package training.g2.dto.request.Inventory;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import training.g2.model.enums.TransactionTypeEnum;


@Getter
@Setter
public class TransactionReqDTO {
    @Id
    @NotNull (message = "Variant ID is required")
    private long variantId;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;

    @NotNull (message = "Type is required")
    private TransactionTypeEnum type;

    private String referenceCode;
    private String note;

}
