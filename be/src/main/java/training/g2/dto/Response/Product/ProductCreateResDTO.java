package training.g2.dto.Response.Product;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateResDTO {
    private long id;
    private String name;
    private String code;
    private String description;
    private Category category;
    private boolean deleted;
    private List<String> imgURL;
    private Instant createdAt;
    private String createdBy;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private long id;
    }
}
