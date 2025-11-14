package training.g2.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import training.g2.dto.Request.InventoryUpdateReqDTO;
import training.g2.dto.Response.Inventory.InventoryResDTO;
import training.g2.service.InventoryService;

@RestController
@RequestMapping("/api/v1/admin/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<InventoryResDTO> getInventory(@PathVariable long id) {

        InventoryResDTO inventoryResDTO = inventoryService.getInventoryById(id);

        return ResponseEntity.ok(inventoryResDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryResDTO> updateInventory(@PathVariable long id, @RequestBody InventoryUpdateReqDTO inventoryUpdateReqDTO) {
        InventoryResDTO inventoryResDTO = inventoryService.updateInventory(id, inventoryUpdateReqDTO);
        return ResponseEntity.ok(inventoryResDTO);
    }
}
