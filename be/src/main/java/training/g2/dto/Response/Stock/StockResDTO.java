package training.g2.dto.response.Stock;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockResDTO {
    private Long variantId;
    private String sku;
    private Integer quantity;
    private Integer reserved;
}
