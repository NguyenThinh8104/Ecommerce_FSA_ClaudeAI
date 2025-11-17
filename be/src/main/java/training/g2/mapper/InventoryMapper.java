package training.g2.mapper;

import training.g2.dto.Response.Inventory.InventoryResDTO;
import training.g2.model.Inventory;

public class InventoryMapper {
    public static InventoryResDTO toInventoryResDTO(Inventory inventory) {
        InventoryResDTO inventoryResDTO = new InventoryResDTO();

        inventoryResDTO.setProductVariantId(inventory.getProductVariantId());

        inventoryResDTO.setQuantity(inventory.getQuantity());

        inventoryResDTO.setMinimum(inventory.getMinimum());
        inventoryResDTO.setMaximum(inventory.getMaximum());

        inventoryResDTO.setLastInAt(inventory.getLastInAt());
        inventoryResDTO.setLastOutAt(inventory.getLastOutAt());

        return inventoryResDTO;
    }
}
