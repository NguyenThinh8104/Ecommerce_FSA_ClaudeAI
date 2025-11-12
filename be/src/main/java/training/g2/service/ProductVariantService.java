package training.g2.service;

import java.util.List;

import training.g2.dto.Request.ProductVariant.ProductVariantCreateReqDTO;
import training.g2.dto.Request.ProductVariant.VariantUpdateReqDTO;
import training.g2.dto.Response.ProductVariant.VariantCreateResDTO;
import training.g2.dto.Response.ProductVariant.VariantDetailDTO;
import training.g2.dto.common.PaginationDTO;

public interface ProductVariantService {
    List<VariantCreateResDTO> createVariant(long productId, ProductVariantCreateReqDTO req);

    PaginationDTO<List<VariantDetailDTO>> getAllVariantByProduct(long productId, int page, int size);

    VariantDetailDTO getVariantById(long variantId);

    VariantDetailDTO updateVariant(long variantId, VariantUpdateReqDTO dto);

    void deleteVariant(long variantId);
}
