package training.g2.dto.request.Stock;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockInItemReqDTO {
    private Long productVariantId;
    private Integer quantity;
    private Double cost;
}
