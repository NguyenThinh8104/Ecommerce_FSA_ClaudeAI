package training.g2.dto.Response.Product;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantDetailResDTO {
    private long id;
    private String name;
    private String code;
    private String description;
    private List<String> images;

    private List<AttributeDTO> attributes;
    private List<VariantDTO> variants;
    private Long defaultVariantId;

    @Getter
    @Setter
    public static class AttributeDTO {
        private Long id;
        private String code;
        private String name;
        private List<AttributeValueDTO> values;
    }

    @Getter
    @Setter
    public static class AttributeValueDTO {
        private Long id;
        private String value;
    }

    @Getter
    @Setter
    public static class VariantDTO {
        private Long id;
        private String sku;
        private String name;
        private Double price;
        private Integer stock;
        private Integer sold;
        private String thumbnail;
        private List<Long> valueIds;
    }

}
