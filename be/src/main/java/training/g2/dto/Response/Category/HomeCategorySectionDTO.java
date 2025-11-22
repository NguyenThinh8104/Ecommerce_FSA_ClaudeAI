package training.g2.dto.response.Category;

import lombok.Getter;
import lombok.Setter;
import training.g2.dto.response.ProductVariant.HomeProductVariantDTO;

import java.util.List;

@Getter
@Setter
public class HomeCategorySectionDTO {
    private Long categoryId;
    private String categoryName;
    private List<HomeProductVariantDTO> variants;
}
