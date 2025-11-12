package training.g2.dto.Request.Category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryReqDTO {

    private String name;
    private String description;
    private Long parentId;
}
