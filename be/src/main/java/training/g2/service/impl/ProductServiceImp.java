package training.g2.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import training.g2.dto.Request.Product.ProductReqDTO;
import training.g2.dto.Response.Product.ProductCreateResDTO;
import training.g2.dto.Response.Product.ProductResDTO;
import training.g2.dto.Response.Product.ProductUpdateResDTO;
import training.g2.dto.common.PaginationDTO;
import training.g2.exception.common.BusinessException;
import training.g2.mapper.ProductMapper;
import training.g2.model.Category;
import training.g2.model.Product;
import training.g2.model.ProductImage;
import training.g2.repository.CategoryRepository;
import training.g2.repository.ProductImageRepository;
import training.g2.repository.ProductRepository;
import training.g2.service.ProductService;
import static training.g2.constant.Constants.Message.*;

@Service
public class ProductServiceImp implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    private final ProductImageRepository productImageRepository;

    public ProductServiceImp(ProductRepository productRepository, ProductMapper productMapper,
            CategoryRepository categoryRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;
    }

    @Transactional
    @Override
    public ProductCreateResDTO createProduct(ProductReqDTO req) {

        if (productRepository.existsByNameAndDeletedFalse(req.getName())) {
            throw new BusinessException(PRODUCT_ALREADY_EXISTS);
        }
        if (productRepository.existsByCodeAndDeletedFalse(req.getCode())) {
            throw new BusinessException(CODE_ALREADY_EXISTS);
        }

        Product product = productMapper.toProductEntity(req);

        if (req.getCategory() != null) {
            Category cate = categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, CATEGORY_NOT_FOUND));
            product.setCategory(cate);
        }

        Product saved = productRepository.save(product);

        List<ProductImage> images = new ArrayList<>();
        if (req.getImgURL() != null) {
            for (String url : req.getImgURL()) {
                if (url == null || url.isBlank())
                    continue;
                ProductImage img = new ProductImage();
                img.setUrl(url);
                img.setProduct(saved);
                images.add(img);
            }
            if (!images.isEmpty())
                productImageRepository.saveAll(images);
            if (saved.getProductImages() != null) {
                saved.getProductImages().clear();
                saved.getProductImages().addAll(images);
            }
        }

        return productMapper.toCreateDTO(saved);
    }

    @Override
    public ProductUpdateResDTO updateProduct(long id, ProductReqDTO productReqDTO) {
        try {
            Product current = productRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND));
            current.setName(productReqDTO.getName());
            current.setCode(productReqDTO.getCode());
            current.setDescription(productReqDTO.getDescription());
            if (productReqDTO.getCategory() != null) {
                Category category = categoryRepository.findById(productReqDTO.getCategory().getId())
                        .orElseThrow(() -> new BusinessException(CATEGORY_NOT_FOUND));
                current.setCategory(category);
            }
            Product saveProduct = productRepository.save(current);
            ProductUpdateResDTO dto = productMapper.toUpdateDTO(saveProduct);
            return dto;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, UPDATE_PRODUCT_FAIL);
        }
    }

    @Override
    public ProductResDTO getProductById(long id) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND));
            ProductResDTO dto = productMapper.toDTO(product);
            return dto;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, PRODUCT_NOT_FOUND, e);
        }
    }

    @Override
    public PaginationDTO<List<ProductResDTO>> getAllProduct(int page, int size, String name, Long cateId,
            Boolean isDeleted,
            String sortField, String sortDirection) {
        try {
            Sort sort = sortDirection.equalsIgnoreCase("asc")
                    ? Sort.by(sortField).ascending()
                    : Sort.by(sortField).descending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Specification<Product> spec = buildQuerySpecification(name, isDeleted, cateId);
            Page<Product> productPage = productRepository.findAll(spec, pageable);

            List<ProductResDTO> products = productPage.getContent().stream().map(p -> productMapper.toDTO(p))
                    .collect(Collectors.toList());

            PaginationDTO<List<ProductResDTO>> pagingDTO = PaginationDTO.<List<ProductResDTO>>builder()
                    .page(page + 1)
                    .size(size)
                    .total(productPage.getTotalElements())
                    .items(products)
                    .build();

            return pagingDTO;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, GET_PRODUCT_FAIL, e);
        }

    }

    public Specification<Product> buildQuerySpecification(String name, Boolean deleted, Long cateId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.trim().isEmpty()) {
                String likeName = "%" + name.toLowerCase().trim() + "%";
                predicates.add(cb.like(cb.lower(root.get("name")), likeName));

            }

            if (Boolean.TRUE.equals(deleted)) {
                predicates.add(cb.equal(root.get("deleted"), true));
            } else {
                predicates.add(cb.equal(root.get("deleted"), false));
            }

            if (cateId != null) {
                Join<Product, Category> categoryJoin = root.join("category", JoinType.LEFT);
                if (cateId == 0L) {
                    predicates.add(cb.isNull(root.get("category")));
                } else if (cateId > 0L) {
                    predicates.add(cb.equal(categoryJoin.get("id"), cateId));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));

        };
    }

    @Override
    public void deleteProduct(long id) {
        Product currentProduct = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PRODUCT_NOT_FOUND));
        currentProduct.setDeleted(true);
        productRepository.save(currentProduct);
    }

}
