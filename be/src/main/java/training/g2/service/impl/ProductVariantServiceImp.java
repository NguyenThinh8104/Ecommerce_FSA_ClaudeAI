package training.g2.service.impl;

import static training.g2.constant.Constants.Message.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import training.g2.dto.Request.ProductVariant.ProductVariantCreateReqDTO;
import training.g2.dto.Request.ProductVariant.VariantUpdateReqDTO;
import training.g2.dto.Request.ProductVariant.ProductVariantCreateReqDTO.VariantItemReq;
import training.g2.dto.Response.ProductVariant.VariantCreateResDTO;
import training.g2.dto.Response.ProductVariant.VariantDetailDTO;
import training.g2.dto.common.PaginationDTO;
import training.g2.exception.common.BusinessException;
import training.g2.helper.SkuHelper;
import training.g2.mapper.VariantMapper;
import training.g2.model.AttributeValue;
import training.g2.model.Product;
import training.g2.model.ProductVariant;
import training.g2.repository.AttributeValueRepository;
import training.g2.repository.ProductRepository;
import training.g2.repository.ProductVariantRepository;
import training.g2.service.ProductVariantService;

@Service
public class ProductVariantServiceImp implements ProductVariantService {
    private final ProductRepository productRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final ProductVariantRepository productVariantRepository;

    public ProductVariantServiceImp(ProductRepository productRepository,
            AttributeValueRepository attributeValueRepository,
            ProductVariantRepository productVariantRepository) {
        this.productRepository = productRepository;
        this.attributeValueRepository = attributeValueRepository;
        this.productVariantRepository = productVariantRepository;
    }

    @Transactional
    @Override
    public List<VariantCreateResDTO> createVariant(long productId, ProductVariantCreateReqDTO req) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND));

            if (req.getItems() == null || req.getItems().isEmpty()) {
                throw new BusinessException(HttpStatus.BAD_REQUEST, ATTRIBUTE_VALUE_REQUIRED);
            }

            List<String> skus = new ArrayList<>();
            List<List<AttributeValue>> valuesList = new ArrayList<>();
            List<Long> prices = new ArrayList<>();
            List<Integer> stocks = new ArrayList<>();

            for (VariantItemReq item : req.getItems()) {
                if (item.getValues() == null || item.getValues().isEmpty()) {
                    throw new BusinessException(HttpStatus.BAD_REQUEST, ATTRIBUTE_VALUE_REQUIRED);
                }

                List<Long> valueIds = item.getValues().stream()
                        .map(VariantItemReq.AttributeValue::getId)
                        .sorted()
                        .collect(Collectors.toList());

                List<AttributeValue> values = attributeValueRepository.findAllById(valueIds);
                if (values.size() != valueIds.size()) {
                    throw new BusinessException(HttpStatus.BAD_REQUEST, ATTRIBUTE_VALUE_CONFLICT);
                }

                String sku = SkuHelper.generateSku(product.getCode(), values);

                skus.add(sku);
                valuesList.add(values);
                prices.add((long) item.getPrice());
                stocks.add(item.getStock());
            }

            if (!skus.isEmpty()) {
                List<String> existedSkus = productVariantRepository.findSkusByProductAndSkuIn(productId, skus);
                if (!existedSkus.isEmpty()) {
                    throw new BusinessException(HttpStatus.BAD_REQUEST, DUPLICATE_VARIANT_EXISTS);
                }
            }

            List<VariantCreateResDTO> result = new ArrayList<>();
            for (int i = 0; i < skus.size(); i++) {
                ProductVariant pv = new ProductVariant();
                pv.setProduct(product);
                pv.setPrice(prices.get(i));
                pv.setStock(stocks.get(i));
                pv.setValues(valuesList.get(i));
                pv.setSku(skus.get(i));
                pv.setSold(0);

                productVariantRepository.save(pv);
                result.add(VariantMapper.toCreateResDTO(pv));
            }

            return result;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, FAILED);
        }
    }

    @Override
    public VariantDetailDTO updateVariant(long variantId, VariantUpdateReqDTO reqDto) {
        try {
            ProductVariant pv = productVariantRepository.findById(variantId)
                    .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, VARIANT_NOT_FOUND));
            pv.setPrice(reqDto.getPrice());
            pv.setStock(reqDto.getStock());
            productVariantRepository.save(pv);
            return VariantMapper.toDTO(pv);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, FAILED);
        }
    }

    @Override
    public void deleteVariant(long variantId) {
        try {
            ProductVariant pv = productVariantRepository.findById(variantId)
                    .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, VARIANT_NOT_FOUND));
            pv.setDeleted(true);
            productVariantRepository.save(pv);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, FAILED);
        }

    }

    @Override
    public VariantDetailDTO getVariantById(long variantId) {
        try {
            ProductVariant pv = productVariantRepository.findById(variantId)
                    .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, VARIANT_NOT_FOUND));
            VariantDetailDTO dto = VariantMapper.toDTO(pv);
            return dto;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, FAILED);
        }
    }

    @Override
    public PaginationDTO<List<VariantDetailDTO>> getAllVariantByProduct(long productId, int page, int size) {
        try {
            if (!productRepository.existsById(productId)) {
                throw new BusinessException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND);
            }

            Pageable pageable = PageRequest.of(page, size);

            Page<ProductVariant> variantPage = productVariantRepository
                    .findAllVariantsWithProductId(productId, pageable);

            List<VariantDetailDTO> items = variantPage.getContent()
                    .stream()
                    .map(VariantMapper::toDTO)
                    .collect(Collectors.toList());

            return PaginationDTO.<List<VariantDetailDTO>>builder()
                    .page(page + 1)
                    .size(size)
                    .total(variantPage.getTotalElements())
                    .items(items)
                    .build();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, FAILED);
        }
    }

}
