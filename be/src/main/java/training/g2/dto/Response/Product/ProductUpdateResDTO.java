package training.g2.dto.Response.Product;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateResDTO {
    private long id;
    private String name;
    private String code;
    private Category category;
    private boolean deleted;
    private Instant updatedAt;
    private String updatedBy;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private long id;
    }
}
