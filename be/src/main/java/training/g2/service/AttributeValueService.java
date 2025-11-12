package training.g2.service;

import training.g2.dto.Request.Attribute.AttributeValueReq;
import training.g2.model.AttributeValue;

public interface AttributeValueService {
    AttributeValue createAttributeValue(AttributeValueReq req);

    AttributeValue updateAttributeValue(long id, AttributeValueReq req);

    void deleteAttributeValue(long id);
}
