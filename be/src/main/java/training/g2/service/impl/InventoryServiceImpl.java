package training.g2.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import training.g2.dto.Request.Inventory.InventoryReqDTO;
import training.g2.dto.Request.Inventory.InventoryUpdateReqDTO;
import training.g2.dto.Request.StockIn.StockInCreateReqDTO;
import training.g2.dto.Response.Inventory.InventoryResDTO;
import training.g2.dto.Response.StockIn.StockInCreateResDTO;
import training.g2.exception.common.BusinessException;
import training.g2.mapper.InventoryMapper;
import training.g2.mapper.StockInMapper;
import training.g2.model.Inventory;
import training.g2.model.ProductVariant;
import training.g2.model.StockIn;
import training.g2.repository.InventoryRepository;
import training.g2.repository.ProductVariantRepository;
import training.g2.repository.StockInRepository;
import training.g2.service.InventoryService;

import java.time.LocalDateTime;
import java.util.List;

import static training.g2.constant.Constants.Message.*;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final StockInRepository stockInRepository;
    private final ProductVariantRepository productVariantRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, StockInRepository stockInRepository, ProductVariantRepository productVariantRepository) {
        this.inventoryRepository = inventoryRepository;
        this.stockInRepository = stockInRepository;
        this.productVariantRepository = productVariantRepository;
    }

    @Override
    public InventoryResDTO createInventory(InventoryReqDTO inventoryReqDTO) {

        Inventory inventory = new Inventory();
        int newMin = inventoryReqDTO.getMinimum();
        int newMax = inventoryReqDTO.getMaximum();

        if (newMin < 0) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Số lượng it nhat không được ít hơn 0");
        }

        if (newMax < 0) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Số lượng lon nhat không được ít hơn 0");
        }

        if (newMin > 0 && newMin > newMax) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "min khong duoc lon hon max");
        }
        ProductVariant productVariant = new ProductVariant();
        inventory.setMinimum(newMin);
        inventory.setMaximum(newMax);

        inventory.setProductVariant(productVariant);
        inventory.setMinimum(inventoryReqDTO.getMinimum());
        inventory.setMaximum(inventoryReqDTO.getMaximum());

        inventoryRepository.save(inventory);

        productVariant.setStock(productVariant.getStock());
        productVariant.setDeleted(false);

        productVariantRepository.save(productVariant);

        return InventoryMapper.toInventoryResDTO(inventory);
    }


    @Override
    public List<InventoryResDTO> getAllInventory() {
        List<Inventory> inventoryList = inventoryRepository.findAll();
        return inventoryList.stream().map(InventoryMapper::toInventoryResDTO).toList();
    }

    @Override
    public InventoryResDTO getInventoryById(long productVariantId) {
        Inventory inventory = inventoryRepository.findById(productVariantId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND));
        return InventoryMapper.toInventoryResDTO(inventory);
    }

    @Override
    public InventoryResDTO updateInventory(long productVariantId, InventoryUpdateReqDTO inventoryUpdateReqDTO) {
        Inventory inventory = inventoryRepository.findById(productVariantId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND));

        int newMin = inventoryUpdateReqDTO.getMinimum();
        int newMax = inventoryUpdateReqDTO.getMaximum();

        if (newMin < 0) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Số lượng it nhat không được ít hơn 0");
        }

        if (newMax < 0) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Số lượng lon nhat không được ít hơn 0");
        }

        if (newMin > 0 && newMin > newMax) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "min khong duoc lon hon max");
        }

        inventory.setMinimum(newMin);
        inventory.setMaximum(newMax);

        inventoryRepository.save(inventory);
        return InventoryMapper.toInventoryResDTO(inventory);
    }
}
