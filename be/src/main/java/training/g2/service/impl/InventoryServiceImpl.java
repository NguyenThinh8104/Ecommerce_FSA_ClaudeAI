package training.g2.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import training.g2.dto.Request.InventoryUpdateReqDTO;
import training.g2.dto.Response.Inventory.InventoryResDTO;
import training.g2.exception.common.BusinessException;
import training.g2.mapper.InventoryMapper;
import training.g2.model.Inventory;
import training.g2.repository.InventoryRepository;
import training.g2.service.InventoryService;
import static training.g2.constant.Constants.Message.*;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
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
