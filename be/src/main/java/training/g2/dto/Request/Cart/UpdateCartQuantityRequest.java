package training.g2.dto.Request.Cart;

public record UpdateCartQuantityRequest(
        Long cartDetailId,
        int quantity
) {}
