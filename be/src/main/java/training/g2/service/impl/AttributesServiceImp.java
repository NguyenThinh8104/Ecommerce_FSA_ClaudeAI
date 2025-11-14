package training.g2.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import training.g2.dto.Request.Attribute.AttributeReq;
import training.g2.dto.Response.Attribute.AttributeResDTO;
import training.g2.exception.common.BusinessException;
import training.g2.model.Attribute;
import training.g2.model.Product;
import training.g2.repository.AttributeRepository;
import training.g2.repository.ProductRepository;
import training.g2.service.AttributeService;
import static training.g2.constant.Constants.Message.*;

@Service
public class AttributesServiceImp implements AttributeService {
    private final AttributeRepository attributeRepository;
    private final ProductRepository productRepository;

    public AttributesServiceImp(AttributeRepository attributeRepository, ProductRepository productRepository) {
        this.attributeRepository = attributeRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Attribute create(AttributeReq req) {
        try {
            Attribute entity = new Attribute();
            entity.setCode(req.getCode());
            entity.setName(req.getName());
            if (req.getProduct() != null) {
                Product p = productRepository.findById(req.getProduct().getId())
                        .orElseThrow(() -> new BusinessException(PRODUCT_NOT_FOUND));
                entity.setProduct(p);
            }

            return attributeRepository.save(entity);
        } catch (BusinessException e) {
            throw e;
        }

        catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, ATTRIBUTES_ADD_FAIL, e);
        }
    }

    @Override
    public Attribute update(long id, AttributeReq req) {
        try {
            Attribute entity = attributeRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, ATTRIBUTES_NOT_FOUND));

            entity.setCode(req.getCode());
            entity.setName(req.getName());

            if (req.getProduct() != null) {
                Product p = productRepository.findById(req.getProduct().getId())
                        .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND));
                entity.setProduct(p);
            }

            return attributeRepository.save(entity);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, ATTRIBUTES_UPDATE_FAIL, e);
        }
    }

    @Override
    public void delete(long id) {
        try {
            Attribute entity = attributeRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, ATTRIBUTES_NOT_FOUND));

            entity.setDeleted(true);
            attributeRepository.save(entity);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, ATTRIBUTES_DELETE_FAIL, e);
        }
    }

    @Override
    public List<AttributeResDTO> getAllByProduct(long productId) {
        try {

            List<AttributeResDTO> attributes = attributeRepository.findAllAttributeByProductId(productId).stream()
                    .map(a -> toDto(a)).toList();

            return attributes;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, ATTRIBUTES_GET_FAIL, e);
        }
    }

    private AttributeResDTO toDto(Attribute a) {
        AttributeResDTO dto = new AttributeResDTO();
        dto.setId(a.getId());
        dto.setCode(a.getCode());
        dto.setName(a.getName());

        List<AttributeResDTO.AttributeValueDTO> vals = a.getValues() == null ? List.of()
                : a.getValues().stream().map(v -> {
                    AttributeResDTO.AttributeValueDTO x = new AttributeResDTO.AttributeValueDTO();
                    x.setId(v.getId());
                    x.setValue(v.getValue());
                    return x;
                }).toList();

        dto.setValues(vals);
        return dto;
    }

}
