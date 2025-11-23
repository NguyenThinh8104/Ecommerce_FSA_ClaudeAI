package training.g2.service;

import training.g2.dto.request.Inventory.InventoryAdjustReqDTO;
import training.g2.dto.request.Inventory.InventoryUpdateReqDTO;
import training.g2.dto.response.Inventory.InventoryResDTO;

public interface InventoryService {
    InventoryResDTO getInventoryById(long productVariantId);
    InventoryResDTO updateInventory(long productVariantId, InventoryUpdateReqDTO inventoryUpdateReqDTO);

    InventoryResDTO StockOut(long productVariantId, InventoryAdjustReqDTO reqDTO);
}
