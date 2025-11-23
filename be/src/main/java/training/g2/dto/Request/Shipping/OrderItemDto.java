package training.g2.dto.Request.Shipping;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long variantId;
    private Integer quantity;
    private Integer price;
}