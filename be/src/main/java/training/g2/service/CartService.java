package training.g2.service;

import training.g2.dto.Request.Cart.AddToCartRequest;
import training.g2.dto.Request.Cart.UpdateCartQuantityRequest;
import training.g2.dto.Response.Cart.CartResponse;

import training.g2.model.User;

public interface CartService {

    // Lấy Card của người dùng
    public CartResponse getCart(User user) ;

    // Thêm item vào cart
    public CartResponse addToCart(User user, AddToCartRequest req);

    // Update quantity
    public CartResponse updateQuantity(User user, UpdateCartQuantityRequest req) ;

    // Remove item from cart
    public CartResponse removeItem(User user, Long cartDetailId) ;

    // Clear cart
    public void clearCart(User user) ;
}
