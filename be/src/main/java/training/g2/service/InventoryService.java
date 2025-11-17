package training.g2.service;

import training.g2.dto.Request.Inventory.InventoryReqDTO;
import training.g2.dto.Request.Inventory.InventoryUpdateReqDTO;
import training.g2.dto.Request.StockIn.StockInCreateReqDTO;
import training.g2.dto.Response.Inventory.InventoryResDTO;
import training.g2.dto.Response.StockIn.StockInCreateResDTO;

import java.util.List;

public interface InventoryService {
    InventoryResDTO createInventory(InventoryReqDTO inventoryReqDTO);
    List<InventoryResDTO> getAllInventory();
    InventoryResDTO getInventoryById(long productVariantId);
    InventoryResDTO updateInventory(long productVariantId, InventoryUpdateReqDTO inventoryUpdateReqDTO);
}
