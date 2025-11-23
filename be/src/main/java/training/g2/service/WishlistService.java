package training.g2.service;
import training.g2.dto.Response.Wishlist.WishlistItemResponse;
import training.g2.dto.common.PaginationDTO;

import java.util.List;


public interface WishlistService {

    PaginationDTO<List<WishlistItemResponse>> getMyWishlist(int page, int size, String sortDir, String sortBy);

    void addToWishlist(Long productVariantId);

    void removeFromWishlist(Long productVariantId);

    /**
     * toggle wishlist cho variant:
     * @return true = đã thêm, false = đã xóa
     */
    boolean toggleWishlist(Long productVariantId);
}

