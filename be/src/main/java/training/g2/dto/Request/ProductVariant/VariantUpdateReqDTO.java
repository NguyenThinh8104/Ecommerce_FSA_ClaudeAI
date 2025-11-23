package training.g2.dto.Request.ProductVariant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantUpdateReqDTO {
    private String name;
    private double price;
    private int stock;

}
