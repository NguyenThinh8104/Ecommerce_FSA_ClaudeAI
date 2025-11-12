package training.g2.controller.admin;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import training.g2.dto.Request.Attribute.AttributeReq;
import training.g2.dto.Response.Attribute.AttributeResDTO;
import training.g2.model.ApiResponse;
import training.g2.model.Attribute;
import training.g2.service.AttributeService;

import static training.g2.constant.Constants.Message.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/products")
public class AttributesController {

    private final AttributeService attributeService;

    public AttributesController(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @PostMapping("/attributes")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Attribute> create(@Valid @RequestBody AttributeReq req) {
        Attribute created = attributeService.create(req);
        return new ApiResponse<>(ATTRIBUTES_CREATED_SUCCESS, created);
    }

    @GetMapping("/{productId}/attributes")
    public ApiResponse<List<AttributeResDTO>> getAllByProduct(@PathVariable("productId") long productId) {
        List<AttributeResDTO> data = attributeService.getAllByProduct(productId);
        return new ApiResponse<>(ATTRIBUTES_GET_SUCCESS, data);
    }

    @PutMapping("/attributes/{id}")
    public ApiResponse<Attribute> update(@PathVariable long id,
            @Valid @RequestBody AttributeReq req) {
        Attribute updated = attributeService.update(id, req);
        return new ApiResponse<>(ATTRIBUTES_UPDATED_SUCCESS, updated);
    }

    @DeleteMapping("/attributes/{id}")
    public ApiResponse<Void> delete(@PathVariable long id) {
        attributeService.delete(id);
        return new ApiResponse<>(ATTRIBUTES_DELETED_SUCCESS, null);
    }
}
