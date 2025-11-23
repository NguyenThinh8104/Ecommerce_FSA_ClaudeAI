package training.g2.controller.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import training.g2.dto.Response.ProductVariant.HomeProductVariantDTO;
import training.g2.dto.common.PaginationDTO;
import training.g2.model.ApiResponse;
import training.g2.service.impl.HomeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @GetMapping("/products")
    public ApiResponse<PaginationDTO<List<HomeProductVariantDTO>>> getHomeProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size) {
        PaginationDTO<List<HomeProductVariantDTO>> data = homeService.getHomeVariantsBySold(page, size);

        return ApiResponse.<PaginationDTO<List<HomeProductVariantDTO>>>builder()
                .data(data)
                .message("Lấy danh sách category + biến thể cho trang chủ thành công")
                .build();
    }
}
