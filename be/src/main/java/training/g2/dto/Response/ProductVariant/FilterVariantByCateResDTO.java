package training.g2.dto.Response.ProductVariant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterVariantByCateResDTO {

    private Long id;
    private String name;
    private long productId;
    private String sku;
    private Double price;
    private Integer sold;
    private Integer stock;
    private String thumbnail;

}
