package training.g2.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import training.g2.dto.Request.Category.CategoryReqDTO;
import training.g2.dto.Response.Category.CategoryCreateResDTO;
import training.g2.dto.Response.Category.CategoryResDTO;
import training.g2.dto.Response.Category.CategoryUpdateRes;
import training.g2.dto.common.PaginationDTO;
import training.g2.model.ApiResponse;
import training.g2.service.CategoryService;
import static training.g2.constant.Constants.Message.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CategoryCreateResDTO> createCategory(@RequestBody CategoryReqDTO reqDTO) {
        CategoryCreateResDTO data = categoryService.createCategory(reqDTO);
        ApiResponse<CategoryCreateResDTO> result = new ApiResponse<CategoryCreateResDTO>(
                ADD_CATEGORY_SUCCESS, data);
        return result;
    }

    @GetMapping("/admin/categories")
    public ApiResponse<PaginationDTO<List<CategoryResDTO>>> getAllCategory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long parentId,
            @RequestParam(required = false) Boolean deleted,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginationDTO<List<CategoryResDTO>> categories = categoryService.getAllCategory(page - 1, size, name, parentId,
                deleted,
                sortField, sortDirection);
        var result = new ApiResponse<>(GET_ALL_CATEGORY_SUCCESS, categories);
        return result;

    }

    @GetMapping("/admin/categories/{id}")
    public ApiResponse<CategoryResDTO> getCategoryById(
            @PathVariable("id") long id) {
        CategoryResDTO data = categoryService.getCategoryById(id);
        ApiResponse<CategoryResDTO> result = new ApiResponse<CategoryResDTO>(GET_CATEGORY_SUCCESS, data);
        return result;
    }

    @PutMapping("/admin/categories/{id}")
    public ApiResponse<CategoryUpdateRes> updateCategory(@PathVariable("id") long id,
            @RequestBody CategoryReqDTO reqDTO) {
        CategoryUpdateRes data = categoryService.updateCategory(id, reqDTO);
        ApiResponse<CategoryUpdateRes> result = new ApiResponse<CategoryUpdateRes>(UPDATE_CATEGORY_SUCCESS, data);
        return result;
    }

    @DeleteMapping("/admin/categories/{id}")
    public void deleteCategory(@PathVariable("id") long id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/admin/categories/children")
    public ResponseEntity<ApiResponse<List<CategoryResDTO>>> getChildCategories() {
        List<CategoryResDTO> children = categoryService.getChildCategories();
        ApiResponse<List<CategoryResDTO>> result = new ApiResponse<List<CategoryResDTO>>("Lấy thông tin thành công",
                children);
        return ResponseEntity.ok().body(result);
    }

}
