package training.g2.controller.admin;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import training.g2.model.ApiResponse;
import training.g2.model.ProductImage;
import training.g2.service.ProductImageService;

@RestController
@RequestMapping("/api/v1/admin")
public class ProductImageController {
    public ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @GetMapping("/products/{id}/images")
    public ApiResponse<List<ProductImage>> getAllImages(@PathVariable("id") long id) {

        return new ApiResponse<>("Lấy danh sách ảnh thành công", productImageService.getAllProduct(id));
    }

    @PostMapping("/products/{id}/images")
    public ApiResponse<ProductImage> getAllImages(@PathVariable("id") long id, @RequestBody ProductImage req) {

        return new ApiResponse<>("Tạo ảnh thành công", productImageService.create(id, req));
    }

    @PutMapping("/images/{id}")
    public ApiResponse<ProductImage> updateImage(@PathVariable("id") long id, @RequestBody ProductImage req) {
        ProductImage productImage = productImageService.update(id, req);
        return new ApiResponse<ProductImage>("Cập nhật thành công", productImage);
    }

    @DeleteMapping("/images/{id}")
    public void deleteImage(@PathVariable("id") long id) {
        productImageService.delete(id);
    }

}
