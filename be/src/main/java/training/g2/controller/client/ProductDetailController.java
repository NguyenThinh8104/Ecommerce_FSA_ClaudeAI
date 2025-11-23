package training.g2.controller.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import training.g2.dto.Response.Product.VariantDetailResDTO;
import training.g2.model.ApiResponse;
import training.g2.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductDetailController {
    private final ProductService productService;

    public ProductDetailController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ApiResponse<VariantDetailResDTO> getDetailProduct(@PathVariable("id") long id,
            @RequestParam(required = false) String sku) {

        return new ApiResponse<VariantDetailResDTO>("Lấy thông tin sản phẩm thành công",
                productService.findVariantDetailByProduct(id, sku));
    }
}
