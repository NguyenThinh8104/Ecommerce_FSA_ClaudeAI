package training.g2.service;

import java.util.List;

import training.g2.dto.Request.Product.ProductReqDTO;
import training.g2.dto.Response.Product.ProductCreateResDTO;
import training.g2.dto.Response.Product.ProductResDTO;
import training.g2.dto.Response.Product.ProductUpdateResDTO;
import training.g2.dto.common.PaginationDTO;

public interface ProductService {
    ProductCreateResDTO createProduct(ProductReqDTO productReqDTO);

    ProductUpdateResDTO updateProduct(long id, ProductReqDTO productReqDTO);

    ProductResDTO getProductById(long id);

    PaginationDTO<List<ProductResDTO>> getAllProduct(int page, int size, String name, Long cateId,
            Boolean isDeleted,
            String sortField, String sortDirection);

    void deleteProduct(long id);
}
