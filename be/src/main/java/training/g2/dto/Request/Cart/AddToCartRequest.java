package training.g2.dto.request.Cart;

public record AddToCartRequest(
        long variantId,
        int quantity
) {}
