package training.g2.dto.Response.Product;

import java.time.Instant;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ProductResDTO {
    private long id;
    private String name;
    private String code;
    private Category category;
    private boolean deleted;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat (pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private long id;
    }

}
