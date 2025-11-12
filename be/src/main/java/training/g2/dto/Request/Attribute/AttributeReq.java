package training.g2.dto.Request.Attribute;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeReq {
    private String code;
    private String name;
    private Product Product;

    @Getter
    @Setter
    public static class Product {
        private long id;
    }
}
