package training.g2.dto.response.Cart;

import java.math.BigDecimal;

public record CartDetailResponse(
        Long id,
        Long variantId,
        String sku,
        String productName,
        String thumbnailUrl,
        int quantity,
        BigDecimal price,
        BigDecimal total
) {}

