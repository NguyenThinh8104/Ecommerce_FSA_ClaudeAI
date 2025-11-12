package training.g2.controller.admin;

import static training.g2.constant.Constants.Message.*;

import java.util.List;

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

import jakarta.validation.Valid;
import training.g2.dto.Request.ProductVariant.ProductVariantCreateReqDTO;
import training.g2.dto.Request.ProductVariant.VariantUpdateReqDTO;
import training.g2.dto.Response.ProductVariant.VariantCreateResDTO;
import training.g2.dto.Response.ProductVariant.VariantDetailDTO;
import training.g2.dto.common.PaginationDTO;
import training.g2.model.ApiResponse;
import training.g2.service.ProductVariantService;

@RestController
@RequestMapping("/api/v1/admin")
public class ProductVariantController {
    private final ProductVariantService productVariantService;

    public ProductVariantController(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    @PostMapping("/products/{productId}/variants")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<List<VariantCreateResDTO>> create(@PathVariable("productId") long productId,
            @RequestBody ProductVariantCreateReqDTO req) {
        List<VariantCreateResDTO> results = productVariantService.createVariant(productId, req);
        return new ApiResponse<List<VariantCreateResDTO>>(VARIANT_CREATE_SUCCESS, results);

    }

    @PutMapping("/variants/{variantId}")
    public ApiResponse<VariantDetailDTO> update(
            @PathVariable("variantId") long variantId,
            @RequestBody @Valid VariantUpdateReqDTO req) {

        VariantDetailDTO dto = productVariantService.updateVariant(variantId, req);
        return new ApiResponse<>(VARIANT_UPDATE_SUCCESS, dto);
    }

    @GetMapping("/variants/{variantId}")
    public ApiResponse<VariantDetailDTO> getById(@PathVariable("variantId") long variantId) {
        VariantDetailDTO dto = productVariantService.getVariantById(variantId);
        return new ApiResponse<>(VARIANT_GET_SUCCESS, dto);
    }

    @DeleteMapping("/variants/{variantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("variantId") long variantId) {
        productVariantService.deleteVariant(variantId);

    }

    @GetMapping("/products/{productId}/variants")
    public ApiResponse<PaginationDTO<List<VariantDetailDTO>>> getAllByProduct(
            @PathVariable("productId") long productId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginationDTO<List<VariantDetailDTO>> result = productVariantService.getAllVariantByProduct(productId, page - 1,
                size);
        return new ApiResponse<>(VARIANT_GET_LIST_SUCCESS, result);
    }
}
