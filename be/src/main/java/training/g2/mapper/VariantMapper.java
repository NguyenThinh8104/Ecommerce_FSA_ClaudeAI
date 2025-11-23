package training.g2.mapper;

import java.util.ArrayList;
import java.util.List;

import training.g2.dto.Response.ProductVariant.VariantCreateResDTO;
import training.g2.dto.Response.ProductVariant.VariantDetailDTO;
import training.g2.model.AttributeValue;
import training.g2.model.ProductVariant;

public class VariantMapper {

    public static VariantCreateResDTO toCreateResDTO(ProductVariant pv) {
        VariantCreateResDTO dto = new VariantCreateResDTO();
        dto.setId(pv.getId());
        dto.setSku(pv.getSku());
        dto.setPrice(pv.getPrice());
        dto.setStock(pv.getStock());
        dto.setSold(pv.getSold());
        dto.setName(pv.getName());
        dto.setThumbnail(pv.getThumbnail());

        List<VariantCreateResDTO.AttributeItem> attrs = new ArrayList<>();
        for (AttributeValue v : pv.getValues()) {
            VariantCreateResDTO.AttributeItem item = new VariantCreateResDTO.AttributeItem();
            item.setId(v.getId());
            item.setName(v.getAttribute().getName());
            item.setValue(v.getValue());
            attrs.add(item);
        }
        dto.setAttributes(attrs);

        return dto;
    }

    public static VariantDetailDTO toDTO(ProductVariant pv) {
        VariantDetailDTO dto = new VariantDetailDTO();
        dto.setId(pv.getId());
        dto.setSku(pv.getSku());
        dto.setPrice(pv.getPrice());
        dto.setStock(pv.getStock());
        dto.setSold(pv.getSold());
        dto.setName(pv.getName());
        dto.setThumbnail(pv.getThumbnail());

        List<VariantDetailDTO.AttributeItem> attrs = new ArrayList<>();
        for (AttributeValue v : pv.getValues()) {
            VariantDetailDTO.AttributeItem item = new VariantDetailDTO.AttributeItem();
            item.setId(v.getId());
            item.setName(v.getAttribute().getName());
            item.setValue(v.getValue());
            attrs.add(item);
        }
        dto.setAttributes(attrs);

        return dto;
    }
}
