package training.g2.controller.admin;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import training.g2.dto.Request.Attribute.AttributeValueReq;
import training.g2.model.ApiResponse;
import training.g2.model.AttributeValue;
import training.g2.service.AttributeValueService;
import training.g2.repository.AttributeValueRepository;

import static training.g2.constant.Constants.Message.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AttributeValueController {

    private final AttributeValueService attributeValueService;

    public AttributeValueController(AttributeValueService attributeValueService,
            AttributeValueRepository attributeValueRepository) {
        this.attributeValueService = attributeValueService;
    }

    @PostMapping("/attribute-values")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AttributeValue> create(@Valid @RequestBody AttributeValueReq req) {
        AttributeValue created = attributeValueService.createAttributeValue(req);
        return new ApiResponse<>(ATTRIBUTE_VALUE_CREATED_SUCCESS, created);
    }

    @PutMapping("/attribute-values/{id}")
    public ApiResponse<AttributeValue> update(@PathVariable long id,
            @Valid @RequestBody AttributeValueReq req) {
        AttributeValue updated = attributeValueService.updateAttributeValue(id, req);
        return new ApiResponse<>(ATTRIBUTE_VALUE_UPDATED_SUCCESS, updated);
    }

    @DeleteMapping("/attribute-values/{id}")
    public ApiResponse<Void> delete(@PathVariable long id) {
        attributeValueService.deleteAttributeValue(id);
        return new ApiResponse<>(ATTRIBUTE_VALUE_DELETED_SUCCESS, null);
    }

}
