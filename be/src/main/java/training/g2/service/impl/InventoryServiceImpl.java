package training.g2.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import training.g2.dto.Request.Inventory.InventoryAdjustReqDTO;
import training.g2.dto.Request.Inventory.InventoryUpdateReqDTO;
import training.g2.dto.Response.Inventory.InventoryResDTO;
import training.g2.exception.common.BusinessException;
import training.g2.mapper.InventoryMapper;
import training.g2.model.Inventory;
import training.g2.repository.InventoryRepository;
import training.g2.service.InventoryService;
import training.g2.util.SecurityUtil;

import java.time.LocalDateTime;

import static training.g2.constant.Constants.Message.*;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final SecurityUtil securityUtil;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, SecurityUtil securityUtil) {
        this.inventoryRepository = inventoryRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    public InventoryResDTO getInventoryById(long productVariantId) {
        Inventory inventory = inventoryRepository.findById(productVariantId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND));
                return InventoryMapper.toInventoryResDTO(inventory);

    }

    @Override
    @Transactional
    public InventoryResDTO updateInventory(long productVariantId, InventoryUpdateReqDTO inventoryUpdateReqDTO) {

        try {
            Inventory inventory = inventoryRepository.findById(productVariantId)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, INVENTORY_NOT_FOUND));


            int newMin = inventoryUpdateReqDTO.getMinimum();
            int newMax = inventoryUpdateReqDTO.getMaximum();

            if (newMin < 0) {
                throw new BusinessException(HttpStatus.BAD_REQUEST, "Số lượng ít nhất không được ít hơn 0");
            }

            if (newMax < 0) {
                throw new BusinessException(HttpStatus.BAD_REQUEST, "Số lượng lon nhat không được ít hơn 0");
            }

            if (newMin > 0 && newMin > newMax) {
                throw new BusinessException(HttpStatus.BAD_REQUEST, "Min không được lớn hơn Max");
            }

            inventory.setMinimum(newMin);
            inventory.setMaximum(newMax);

            Inventory saveInvent = inventoryRepository.save(inventory);
            return InventoryMapper.toInventoryResDTO(saveInvent);

        } catch (ObjectOptimisticLockingFailureException e) {
            throw new BusinessException(HttpStatus.CONFLICT, INVENTORY_OPTIMISTIC_LOCK_FAIL);
        } catch (BusinessException e) {
            throw e;
        }  catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, INVENTORY_ADJUST_FAIL, e);
        }
    }

    @Override
    @Transactional
    public InventoryResDTO StockOut(long productVariantId, InventoryAdjustReqDTO reqDTO) {
        try {
            if (reqDTO.getQuantity() <= 0){
                throw new BusinessException(HttpStatus.NOT_FOUND, INVENTORY_ADJUST_QUANTITY_INVALID);
            }

            Inventory inventory = inventoryRepository.findById(productVariantId)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND));

            int currentQuantity = inventory.getQuantity();
            if (currentQuantity < reqDTO.getQuantity()) {
                throw new BusinessException(HttpStatus.NOT_FOUND, INVENTORY_NOT_ENOUGH_STOCK);
            }

            inventory.setQuantity(currentQuantity - reqDTO.getQuantity());
            inventory.setLastOutAt(LocalDateTime.now());

            Inventory updatedInventory = inventoryRepository.save(inventory);

            return InventoryMapper.toInventoryResDTO(updatedInventory);
        }
        catch (ObjectOptimisticLockingFailureException e) {
            throw new BusinessException(HttpStatus.CONFLICT, INVENTORY_OPTIMISTIC_LOCK_FAIL);
        } catch (BusinessException e) {
            throw e;
        }catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, INVENTORY_ADJUST_FAIL,e);
        }
    }
}
