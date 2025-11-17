package training.g2.dto.Response.Product;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat (pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    private String createdBy;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private long id;
    }
}
