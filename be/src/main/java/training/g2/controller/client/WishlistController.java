package training.g2.controller.client;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import training.g2.dto.Request.Wishlist.WishlistRequest;
import training.g2.dto.Response.Wishlist.WishlistItemResponse;
import training.g2.dto.common.PaginationDTO;
import training.g2.model.ApiResponse;
import training.g2.service.WishlistService;

@RestController
@RequestMapping("/api/v1/wishlists")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public ApiResponse<PaginationDTO<List<WishlistItemResponse>>> getMyWishlist(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {

        int pageIndex = Math.max(page - 1, 0);

        PaginationDTO<List<WishlistItemResponse>> items =
                wishlistService.getMyWishlist(pageIndex, size, sortBy, sortDir);


        items.setPage(page);

        return ApiResponse.<PaginationDTO<List<WishlistItemResponse>>>builder()
                .data(items)
                .message("Lấy danh sách yêu thích thành công")
                .build();
    }


    @PostMapping
    public ApiResponse<Void> addToWishlist(@RequestBody WishlistRequest request) {
        wishlistService.addToWishlist(request.getProductVariantId());
        return ApiResponse.<Void>builder()
                .message("Thêm vào danh sách yêu thích thành công")
                .build();
    }

    @DeleteMapping("/{productVariantId}")
    public ApiResponse<Void> removeFromWishlist(@PathVariable Long productVariantId) {
        wishlistService.removeFromWishlist(productVariantId);
        return ApiResponse.<Void>builder()
                .message("Xóa khỏi danh sách yêu thích thành công")
                .build();
    }

    // Nếu bạn muốn endpoint toggle:
    @PostMapping("/toggle")
    public ApiResponse<Boolean> toggleWishlist(@RequestBody WishlistRequest request) {
        boolean added = wishlistService.toggleWishlist(request.getProductVariantId());
        return ApiResponse.<Boolean>builder()
                .data(added)
                .message(added ? "Đã thêm vào danh sách yêu thích" : "Đã xóa khỏi danh sách yêu thích")
                .build();
    }
}
