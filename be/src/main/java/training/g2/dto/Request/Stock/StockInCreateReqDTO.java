package training.g2.dto.request.Stock;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StockInCreateReqDTO {
    private String supplierName;
    private List<Item> items;

    @Getter
    @Setter
    public static class Item {
        private Long productVariantId;
        private Integer quantity;
        private Double cost;
    }
}
