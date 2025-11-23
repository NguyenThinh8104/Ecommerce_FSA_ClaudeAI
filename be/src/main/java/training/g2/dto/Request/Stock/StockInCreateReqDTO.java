package training.g2.dto.request.Stock;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StockInCreateReqDTO {
    private String supplierName;

    @NotEmpty (message = "Danh sách sản phẩm không dược để trống")
    private List<Item> items;

    @Getter
    @Setter
    public static class Item {
        private Long productVariantId;
        private Integer quantity;
        private Double cost;
    }
}
