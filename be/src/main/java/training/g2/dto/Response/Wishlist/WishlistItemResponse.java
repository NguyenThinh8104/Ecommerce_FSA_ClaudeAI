package training.g2.dto.Response.Wishlist;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WishlistItemResponse {

    private Long wishlistId;

    private Long variantId;
    private String variantName;
    private String productName;


    private BigDecimal price;

    private String thumbnailUrl;


    private int stock;
    private int sold;
}
