package training.g2.dto.Response.Category;

import lombok.Getter;
import lombok.Setter;
import training.g2.dto.Response.ProductVariant.HomeProductVariantDTO;

import java.util.List;

@Getter
@Setter
public class HomeCategorySectionDTO {
    private Long categoryId;
    private String categoryName;
    private List<HomeProductVariantDTO> variants;
}
