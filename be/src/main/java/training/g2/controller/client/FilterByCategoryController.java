package training.g2.controller.client;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import training.g2.dto.response.ProductVariant.FilterVariantByCateResDTO;
import training.g2.dto.common.PaginationDTO;
import training.g2.model.ApiResponse;
import training.g2.model.enums.PriceRange;
import training.g2.service.ProductVariantService;

@RestController
@RequestMapping("/api/v1/category")
public class FilterByCategoryController {
    private final ProductVariantService productVariantService;

    public FilterByCategoryController(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    @GetMapping("/{id}")
    public ApiResponse<PaginationDTO<List<FilterVariantByCateResDTO>>> getCategoryHome(
            @PathVariable("id") Long id,
            @RequestParam(required = false) PriceRange priceRange,
            @RequestParam(defaultValue = "createdAt_desc") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        ApiResponse<PaginationDTO<List<FilterVariantByCateResDTO>>> result = new ApiResponse<PaginationDTO<List<FilterVariantByCateResDTO>>>(
                "Lấy danh sách thành công",
                productVariantService.getCategoryHome(id, priceRange, sort, page - 1, size));
        return result;
    }
}
