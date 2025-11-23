package training.g2.dto.Request.ProductVariant;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductVariantCreateReqDTO {
    private List<VariantItemReq> items;

    @Getter
    @Setter
    public static class VariantItemReq {
        private List<AttributeValue> values;
        private String name;
        private double price;
        private int stock;
        private String thumbnail;

        @Getter
        @Setter
        public static class AttributeValue {
            private long id;
        }

    }

}
