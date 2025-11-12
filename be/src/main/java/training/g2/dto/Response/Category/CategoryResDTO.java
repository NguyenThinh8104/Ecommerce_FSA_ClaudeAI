package training.g2.dto.Response.Category;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResDTO {
    private Long id;
    private String name;
    private String description;
    private boolean deleted;
    private Long parentId;
    private List<ChildCategoryDTO> children;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @Getter
    @Setter
    public static class ChildCategoryDTO {
        private Long id;
    }
}
