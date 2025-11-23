package training.g2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import training.g2.dto.Response.ProductVariant.HomeProductVariantDTO;
import training.g2.dto.common.PaginationDTO;
import training.g2.model.Product;
import training.g2.model.ProductVariant;
import training.g2.repository.ProductVariantRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final ProductVariantRepository productVariantRepository;

    public PaginationDTO<List<HomeProductVariantDTO>> getHomeVariantsBySold(int page, int size) {
        int pageIndex = Math.max(page - 1, 0);

        // sort theo sold DESC
        var pageable = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "sold"));

        Page<ProductVariant> variantPage = productVariantRepository.findByDeletedFalseAndProduct_DeletedFalse(pageable);

        List<HomeProductVariantDTO> items = variantPage.getContent()
                .stream()
                .map(this::toHomeDTO)
                .toList();

        PaginationDTO<List<HomeProductVariantDTO>> res = new PaginationDTO<>();
        res.setPage(page); // trả lại page 1-based cho FE
        res.setSize(size);
        res.setTotal(variantPage.getTotalElements());
        res.setItems(items);

        return res;
    }

    private HomeProductVariantDTO toHomeDTO(ProductVariant pv) {
        HomeProductVariantDTO dto = new HomeProductVariantDTO();
        dto.setVariantId(pv.getId());
        dto.setVariantName(pv.getName());

        Product product = pv.getProduct();
        if (product != null) {
            dto.setProductName(product.getName() + " " + pv.getName());
            dto.setProductId(pv.getProduct().getId());
            dto.setThumbnailUrl(pv.getThumbnail());
        }
        dto.setSku(pv.getSku());
        dto.setPrice(BigDecimal.valueOf(pv.getPrice()));
        dto.setStock(pv.getStock());
        dto.setSold(pv.getSold());

        return dto;
    }
}
