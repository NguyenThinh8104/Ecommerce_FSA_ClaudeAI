package training.g2.dto.request.Cart;

public record UpdateCartQuantityRequest(
        Long cartDetailId,
        int quantity
) {}
