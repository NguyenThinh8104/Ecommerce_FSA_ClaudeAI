package training.g2.dto.response.Stock;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockInItemResDTO {
    private Long productVariantId;
    private String sku;
    private Integer quantity;
    private Double cost;
}
