package training.g2.controller.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import training.g2.dto.Response.Category.CategoryResDTO;
import training.g2.model.ApiResponse;
import training.g2.service.impl.CategoryServiceImp;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class ClientCategoryController {
    private final CategoryServiceImp categoryService;
    @GetMapping()
    public ApiResponse<List<CategoryResDTO>> getCategories() {
        return ApiResponse.<List<CategoryResDTO>>builder()
                .data(categoryService.getTreeCategory())
                .message("Lấy thành công cây thư mục ")
                .build();
    }
}
