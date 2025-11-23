package training.g2.dto.Response.ProductVariant;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantDetailDTO {
    private long id;
    private String sku;
    private String name;
    private double price;
    private int stock;
    private int sold;
    private String thumbnail;

    private List<AttributeItem> attributes;

    @Getter
    @Setter
    public static class AttributeItem {
        private long id;
        private String name;
        private String value;
    }
}
