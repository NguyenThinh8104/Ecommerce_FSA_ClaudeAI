package training.g2.dto.response.ProductVariant;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class HomeProductVariantDTO {
    private Long productId;
    private Long variantId;
    private String variantName;
    private String productName;
    private BigDecimal price;
    private int stock;
    private int sold;
    private String thumbnailUrl;
}

