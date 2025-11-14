package training.g2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import training.g2.exception.common.BusinessException;
import training.g2.model.Product;
import training.g2.model.ProductImage;
import training.g2.repository.ProductImageRepository;
import training.g2.repository.ProductRepository;

@Service
public class ProductImageServiceImp implements training.g2.service.ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    public ProductImageServiceImp(ProductImageRepository productImageRepository, ProductRepository productRepository) {
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ProductImage update(long id, ProductImage req) {
        ProductImage current = productImageRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tim thấy ảnh"));
        current.setUrl(req.getUrl());
        productImageRepository.save(current);
        return current;

    }

    @Override
    public void delete(long id) {
        productImageRepository.deleteById(id);
    }

    @Override
    public List<ProductImage> getAllProduct(long productId) {
        List<ProductImage> list = productImageRepository.findAllProductImage(productId);
        return list;
    }

    @Override
    public ProductImage create(long productId, ProductImage req) {
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy sản phẩm"));
        ProductImage img = new ProductImage();
        img.setProduct(p);
        img.setUrl(req.getUrl());
        productImageRepository.save(img);
        return img;
    }

}
