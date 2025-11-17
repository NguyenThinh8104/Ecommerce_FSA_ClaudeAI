package training.g2.service;

import training.g2.dto.Request.Inventory.InventoryAdjustReqDTO;
import training.g2.dto.Request.Inventory.InventoryUpdateReqDTO;
import training.g2.dto.Response.Inventory.InventoryResDTO;

public interface InventoryService {
    InventoryResDTO getInventoryById(long productVariantId);
    InventoryResDTO updateInventory(long productVariantId, InventoryUpdateReqDTO inventoryUpdateReqDTO);

    InventoryResDTO StockOut(long productVariantId, InventoryAdjustReqDTO reqDTO);
}
