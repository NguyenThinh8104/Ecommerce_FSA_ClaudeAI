package training.g2.service;

import training.g2.dto.Request.InventoryUpdateReqDTO;
import training.g2.dto.Response.Inventory.InventoryResDTO;

public interface InventoryService {
    InventoryResDTO getInventoryById(long productVariantId);
    InventoryResDTO updateInventory(long productVariantId, InventoryUpdateReqDTO inventoryUpdateReqDTO);

}
