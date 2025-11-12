package training.g2.service.impl;

import static training.g2.constant.Constants.Message.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import training.g2.dto.Request.Attribute.AttributeValueReq;
import training.g2.exception.common.BusinessException;
import training.g2.model.Attribute;
import training.g2.model.AttributeValue;
import training.g2.repository.AttributeRepository;
import training.g2.repository.AttributeValueRepository;
import training.g2.service.AttributeValueService;

@Service
public class AttributeValueServiceIpm implements AttributeValueService {

    private final AttributeValueRepository attributeValueRepository;
    private final AttributeRepository attributeRepository;

    public AttributeValueServiceIpm(AttributeValueRepository attributeValueRepository,
            AttributeRepository attributeRepository) {
        this.attributeValueRepository = attributeValueRepository;
        this.attributeRepository = attributeRepository;
    }

    @Override
    public AttributeValue createAttributeValue(AttributeValueReq req) {
        try {
            if (attributeValueRepository.existsByValue(req.getValue())) {
                throw new BusinessException(ATTRIBUTE_VALUE_EXISTS);
            }
            AttributeValue value = new AttributeValue();
            value.setValue(req.getValue());
            if (req.getAttribute() != null) {
                Attribute attribute = attributeRepository.findById(req.getAttribute().getId())
                        .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, ATTRIBUTES_NOT_FOUND));
                value.setAttribute(attribute);
            }
            return attributeValueRepository.save(value);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, ATTRIBUTE_VALUE_CREATE_FAIL, e);
        }
    }

    @Override
    public AttributeValue updateAttributeValue(long id, AttributeValueReq req) {
        try {
            AttributeValue currentValue = attributeValueRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, ATTRIBUTE_VALUE_NOT_FOUND));

            if (req.getAttribute() != null) {
                Attribute attribute = attributeRepository.findById(req.getAttribute().getId())
                        .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, ATTRIBUTES_NOT_FOUND));
                currentValue.setAttribute(attribute);
            }

            currentValue.setValue(req.getValue());

            return attributeValueRepository.save(currentValue);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, ATTRIBUTE_VALUE_UPDATE_FAIL, e);
        }
    }

    @Override
    public void deleteAttributeValue(long id) {
        AttributeValue a = attributeValueRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, ATTRIBUTE_VALUE_NOT_FOUND));
        a.getVariants().stream().forEach(variant -> variant.getValues().remove(a));
        attributeValueRepository.delete(a);
    }

}
