package training.g2.dto.Request.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ProductReqDTO {
    private String name;
    private String code;
    private Category category;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private long id;

    }

}
