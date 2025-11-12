package training.g2.dto.Response.Category;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreateResDTO {
    private Long id;
    private String name;
    private String description;
    private Long parentId;
    private Instant createdAt;
    private String createdBy;
}
