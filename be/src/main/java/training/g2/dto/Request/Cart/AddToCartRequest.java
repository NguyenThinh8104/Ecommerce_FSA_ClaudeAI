package training.g2.dto.Request.Cart;

public record AddToCartRequest(
        long variantId,
        int quantity
) {}
