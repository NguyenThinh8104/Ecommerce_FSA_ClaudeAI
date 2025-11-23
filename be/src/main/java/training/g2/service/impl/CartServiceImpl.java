package training.g2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import training.g2.dto.Request.Cart.AddToCartRequest;
import training.g2.dto.Request.Cart.UpdateCartQuantityRequest;
import training.g2.dto.Response.Cart.CartDetailResponse;
import training.g2.dto.Response.Cart.CartResponse;
import training.g2.exception.common.BusinessException;
import training.g2.model.Cart;
import training.g2.model.CartDetail;
import training.g2.model.ProductVariant;
import training.g2.model.User;
import training.g2.repository.CartDetailRepository;
import training.g2.repository.CartRepository;
import training.g2.repository.ProductVariantRepository;
import training.g2.service.CartService;

import java.math.BigDecimal;
import java.util.List;

import static training.g2.constant.Constants.Message.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepo;
    private final CartDetailRepository detailRepo;
    private final ProductVariantRepository variantRepo;

    // Ensure each user has exactly 1 cart
    private Cart getOrCreateCart(User user) {
        return cartRepo.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepo.save(cart);
                });
    }

    // Convert Cart -> CartResponse
    private CartResponse buildCartResponse(Cart cart) {

        List<CartDetailResponse> items = cart.getItems().stream()
                .map(i -> new CartDetailResponse(
                        i.getId(),
                        i.getProductVariant().getId(),
                        i.getProductVariant().getSku(),
                        i.getProductVariant().getProduct().getName() + " " + i.getProductVariant().getName(),
                        i.getProductVariant().getThumbnail(),
                        i.getQuantity(),
                        i.getPrice(),
                        i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity()))))
                .toList();

        BigDecimal subtotal = items.stream()
                .map(CartDetailResponse::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(cart.getId(), items, subtotal);
    }

    // Get cart
    public CartResponse getCart(User user) {
        Cart cart = getOrCreateCart(user);
        return buildCartResponse(cart);
    }

    public CartResponse addToCart(User user, AddToCartRequest req) {

        Cart cart = getOrCreateCart(user);

        ProductVariant variant = variantRepo.findById(req.variantId())
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, PRODUCT_NOT_FOUND));

        int stock = variant.getStock(); // tồn kho thực tế

        // Kiểm tra đã có item trong cart chưa
        CartDetail existing = cart.getItems().stream()
                .filter(i -> i.getProductVariant().getId() == (req.variantId()))
                .findFirst()
                .orElse(null);

        int newQuantity = req.quantity();

        if (existing != null) {
            newQuantity = existing.getQuantity() + req.quantity();
        }

        // Validate vượt quá tồn kho
        if (newQuantity > stock) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Số lượng yêu cầu (" + newQuantity +
                            ") vượt quá số lượng tồn kho (" + stock + ")");
        }

        // Cập nhật giỏ
        if (existing != null) {
            existing.setQuantity(newQuantity);
        } else {
            CartDetail detail = new CartDetail();
            detail.setProductVariant(variant);
            detail.setQuantity(req.quantity());
            detail.setPrice(BigDecimal.valueOf(variant.getPrice()));
            detail.setCart(cart);
            cart.getItems().add(detail);
        }

        cartRepo.save(cart);
        return buildCartResponse(cart);
    }

    public CartResponse updateQuantity(User user, UpdateCartQuantityRequest req) {

        Cart cart = getOrCreateCart(user);

        CartDetail detail = cart.getItems().stream()
                .filter(i -> i.getId().equals(req.cartDetailId()))
                .findFirst()
                .orElseThrow(
                        () -> new BusinessException(HttpStatus.BAD_REQUEST, "Sản phẩm không tồn tại trong giỏ hàng"));

        ProductVariant variant = detail.getProductVariant();
        int stock = variant.getStock();

        if (req.quantity() > stock) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    "Số lượng yêu cầu (" + req.quantity() +
                            ") vượt quá số lượng tồn kho (" + stock + ")");
        }

        // Nếu quantity <= 0 → xoá item
        if (req.quantity() <= 0) {
            cart.getItems().remove(detail);
            detailRepo.delete(detail);
        } else {
            detail.setQuantity(req.quantity());
        }

        cartRepo.save(cart);
        return buildCartResponse(cart);
    }

    // Remove item from cart
    public CartResponse removeItem(User user, Long cartDetailId) {

        Cart cart = getOrCreateCart(user);

        CartDetail detail = cart.getItems().stream()
                .filter(i -> i.getId().equals(cartDetailId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        cart.getItems().remove(detail);
        detailRepo.delete(detail);

        cartRepo.save(cart);
        return buildCartResponse(cart);
    }

    // Clear cart
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cart.getItems().clear();
        cartRepo.save(cart);
    }
}
