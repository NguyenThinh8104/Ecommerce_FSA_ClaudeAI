package training.g2.controller.client;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import training.g2.dto.Response.Attribute.AttributeFilterDTO;
import training.g2.dto.Response.ProductVariant.FilterVariantByCateResDTO;
import training.g2.dto.common.PaginationDTO;
import training.g2.model.ApiResponse;
import training.g2.model.enums.PriceRange;
import training.g2.service.AttributeService;
import training.g2.service.ProductVariantService;

@RestController
@RequestMapping("/api/v1/category")
public class FilterByCategoryController {
    private final ProductVariantService productVariantService;
    private final AttributeService attributeService;

    public FilterByCategoryController(ProductVariantService productVariantService, AttributeService attributeService) {
        this.productVariantService = productVariantService;
        this.attributeService = attributeService;
    }

    @GetMapping("/{id}")
    public ApiResponse<PaginationDTO<List<FilterVariantByCateResDTO>>> getCategoryHome(
            @PathVariable("id") Long id,
            @RequestParam(required = false) List<PriceRange> priceRanges,
            @RequestParam(required = false, defaultValue = "createdAt_desc") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) List<String> attributeValues) {

        ApiResponse<PaginationDTO<List<FilterVariantByCateResDTO>>> result = new ApiResponse<PaginationDTO<List<FilterVariantByCateResDTO>>>(
                "Lấy thông tin sản phẩm thành công",
                productVariantService.getCategoryHome(
                        id,
                        priceRanges,
                        sort,
                        page - 1,
                        size,
                        attributeValues));

        return result;
    }

    @GetMapping("/{id}/attributes")
    public ApiResponse<List<AttributeFilterDTO>> getCategoryFilters(
            @PathVariable("id") Long categoryId) {

        return new ApiResponse<>(
                "Lấy danh sách bộ lọc thuộc tính thành công",
                attributeService.getAttributeFiltersByCategory(categoryId));
    }

}
