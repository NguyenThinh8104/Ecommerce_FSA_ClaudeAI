package training.g2.dto.response.Cart;


import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Long cartId,
        List<CartDetailResponse> items,
        BigDecimal total
) {}

