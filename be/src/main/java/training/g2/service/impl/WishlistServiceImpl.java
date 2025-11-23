package training.g2.service.impl;

import java.math.BigDecimal;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.g2.dto.Response.Wishlist.WishlistItemResponse;
import training.g2.dto.common.PaginationDTO;
import training.g2.exception.common.BusinessException;
import training.g2.model.Product;
import training.g2.model.ProductImage;
import training.g2.model.ProductVariant;
import training.g2.model.User;
import training.g2.model.Wishlist;
import training.g2.repository.ProductVariantRepository;
import training.g2.repository.WishlistRepository;
import training.g2.service.WishlistService;
import training.g2.util.SecurityUtil;

import static training.g2.constant.Constants.Message.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductVariantRepository productVariantRepository;
    private final SecurityUtil securityUtil;
    private User getCurrentUser() {
        return securityUtil.getCurrentUser();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationDTO<List<WishlistItemResponse>> getMyWishlist(
            int page, int size, String sortBy, String sortDir
    ) {
        Sort.Direction sortDirection = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection,sortBy));
        User currentUser = getCurrentUser();
        Page<Wishlist> wishlists = wishlistRepository.findByUser(currentUser, pageable);

        List<WishlistItemResponse> list = wishlists.getContent().
                stream()
                .map(this::mapToWishlistItemResponse)
                .toList();

        return PaginationDTO.<List<WishlistItemResponse>>builder()
                .page(page + 1)
                .size(size)
                .total(wishlists.getTotalElements())
                .items(list)
                .build();
    }

    @Override
    public void addToWishlist(Long productVariantId) {
        User currentUser = getCurrentUser();

        ProductVariant variant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,PRODUCT_NOT_FOUND));

        boolean exists = wishlistRepository.existsByUserAndProductVariant(currentUser, variant);
        if (exists) {
            return; // đã có rồi thì thôi
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(currentUser);
        wishlist.setProductVariant(variant);

        wishlistRepository.save(wishlist);
    }

    @Override
    public void removeFromWishlist(Long productVariantId) {
        User currentUser = getCurrentUser();

        ProductVariant variant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,PRODUCT_NOT_FOUND));


        wishlistRepository.deleteByUserAndProductVariant(currentUser, variant);
    }

    @Override
    public boolean toggleWishlist(Long productVariantId) {
        User currentUser = getCurrentUser();

        ProductVariant variant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,PRODUCT_NOT_FOUND));


        return wishlistRepository.findByUserAndProductVariant(currentUser, variant)
                .map(existing -> {
                    // đã tồn tại → xóa
                    wishlistRepository.delete(existing);
                    return false; // đã xóa
                })
                .orElseGet(() -> {
                    // chưa tồn tại → thêm
                    Wishlist wishlist = new Wishlist();
                    wishlist.setUser(currentUser);
                    wishlist.setProductVariant(variant);
                    wishlistRepository.save(wishlist);
                    return true; // đã thêm
                });
    }

    // Map entity -> DTO
    private WishlistItemResponse mapToWishlistItemResponse(Wishlist wishlist) {
        ProductVariant variant = wishlist.getProductVariant();
        Product product = variant.getProduct();

        String thumbnail = null;

        if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
            ProductImage image = product.getProductImages().iterator().next();
            thumbnail = image.getUrl();
        }

        return WishlistItemResponse.builder()
                .wishlistId(wishlist.getId())
                .variantId(product.getId())
                .productName(product.getName())
                .variantName(variant.getName())
                .stock(variant.getStock())
                .sold(variant.getSold())
                .price(BigDecimal.valueOf(variant.getPrice()))
                .thumbnailUrl(thumbnail)
                .build();
    }
}
