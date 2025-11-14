package training.g2.service;

import java.util.List;

import training.g2.model.ProductImage;

public interface ProductImageService {

    List<ProductImage> getAllProduct(long productId);

    ProductImage update(long longId, ProductImage req);

    ProductImage create(long longId, ProductImage req);

    void delete(long id);
}
