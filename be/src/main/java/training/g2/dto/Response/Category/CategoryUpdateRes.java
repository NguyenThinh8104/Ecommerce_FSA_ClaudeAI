package training.g2.dto.Response.Category;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdateRes {
    private Long id;
    private String name;
    private String description;
    private Long parentId;
    private Instant updatedAt;
    private String updatedBy;
}
