package training.g2.dto.response.ProductVariant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterVariantByCateResDTO {

    private Long id;
    private String name;
    private String sku;
    private Double price;
    private Integer sold;
    private Integer stock;
    private String thumbnail;

}
