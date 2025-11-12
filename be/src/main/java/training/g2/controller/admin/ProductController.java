package training.g2.controller.admin;

import org.springframework.http.HttpStatus;
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

import training.g2.dto.Request.Product.ProductReqDTO;
import training.g2.dto.Response.Product.ProductCreateResDTO;
import training.g2.dto.Response.Product.ProductResDTO;
import training.g2.dto.Response.Product.ProductUpdateResDTO;
import training.g2.dto.common.PaginationDTO;
import training.g2.model.ApiResponse;
import training.g2.service.ProductService;
import static training.g2.constant.Constants.Message.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductCreateResDTO> createProduct(@RequestBody ProductReqDTO reqProduct) {
        ProductCreateResDTO dto = productService.createProduct(reqProduct);
        ApiResponse<ProductCreateResDTO> result = new ApiResponse<ProductCreateResDTO>(
                PRODUCT_CREATED_SUCCESS, dto);
        return result;
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResDTO> getProductById(@PathVariable("id") long id) {
        ProductResDTO dto = productService.getProductById(id);
        ApiResponse<ProductResDTO> result = new ApiResponse<ProductResDTO>(GET_PRODUCT_SUCCESS, dto);
        return result;
    }

    @GetMapping
    public ApiResponse<PaginationDTO<List<ProductResDTO>>> getAllProduct(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(required = false) Long cateId,
            @RequestParam(required = false) boolean deleted,
            @RequestParam(defaultValue = "createdAt") String sortField,

            @RequestParam(defaultValue = "desc") String sortDirection) {
        System.out.println(cateId + "ID Category");
        PaginationDTO<List<ProductResDTO>> products = productService.getAllProduct(page - 1, size, name, cateId,
                deleted, sortField, sortDirection);
        ApiResponse<PaginationDTO<List<ProductResDTO>>> result = new ApiResponse<PaginationDTO<List<ProductResDTO>>>(
                GET_PRODUCT_SUCCESS, products);
        return result;
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductUpdateResDTO> updateProduct(@PathVariable("id") long id,
            @RequestBody ProductReqDTO reqProduct) {
        ProductUpdateResDTO dto = productService.updateProduct(id, reqProduct);
        ApiResponse<ProductUpdateResDTO> result = new ApiResponse<ProductUpdateResDTO>(PRODUCT_UPDATED_SUCCESS, dto);
        return result;
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") long id) {
        productService.deleteProduct(id);
    }

}
