package training.g2.controller.client;


import org.springframework.web.bind.annotation.*;
import training.g2.dto.request.Cart.AddToCartRequest;
import training.g2.dto.request.Cart.UpdateCartQuantityRequest;
import training.g2.dto.response.Cart.CartResponse;
import training.g2.model.ApiResponse;
import training.g2.model.User;
import training.g2.service.CartService;
import training.g2.util.SecurityUtil;


@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;
    private final SecurityUtil securityUtil;

    public CartController(CartService cartService, SecurityUtil securityUtil) {
        this.cartService = cartService;
        this.securityUtil = securityUtil;
    }

    // Lấy user trong request
    private User getCurrentUser() {
        return securityUtil.getCurrentUser();
    }

    @GetMapping
    public ApiResponse<CartResponse> getCart() {
        return ApiResponse.<CartResponse>builder()
                .message("Lấy giỏ hàng thành công")
                .data(cartService.getCart(getCurrentUser()))
                .build();
    }

    @PostMapping("/add")
    public ApiResponse<CartResponse> addToCart(@RequestBody AddToCartRequest req) {
        return ApiResponse.<CartResponse>builder()
                .data(cartService.addToCart(getCurrentUser(), req))
                .message("Thêm vào giỏ hàng thành công")
                .build();
    }

    @PutMapping("/update")
    public ApiResponse<CartResponse> updateQuantity(@RequestBody UpdateCartQuantityRequest req) {
        return ApiResponse.<CartResponse>builder()
                .data(cartService.updateQuantity(getCurrentUser(), req))
                .message("Cập nhật số lượng thành công")
                .build();
    }

    @DeleteMapping("/remove/{detailId}")
    public ApiResponse<CartResponse> removeItem(@PathVariable Long detailId) {
        return ApiResponse.<CartResponse>builder()
                .data(cartService.removeItem(getCurrentUser(), detailId))
                .message("Đã xóa sản phẩm khỏi giỏ")
                .build();
    }

    @DeleteMapping("/clear")
    public ApiResponse<Void> clearCart() {
        cartService.clearCart(getCurrentUser());
        return ApiResponse.<Void>builder()
                .message("Dọn sạch giỏ hàng")
                .build();
    }
}



