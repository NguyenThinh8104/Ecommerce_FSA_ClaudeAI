package training.g2.service;

import java.util.List;

import training.g2.dto.Request.Attribute.AttributeReq;
import training.g2.dto.Response.Attribute.AttributeResDTO;
import training.g2.model.Attribute;

public interface AttributeService {

    Attribute create(AttributeReq req);

    Attribute update(long attributeId, AttributeReq req);

    void delete(long attributeId);

    List<AttributeResDTO> getAllByProduct(long productId);
}
