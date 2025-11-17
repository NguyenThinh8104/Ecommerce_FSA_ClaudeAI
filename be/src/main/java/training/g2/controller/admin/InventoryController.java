package training.g2.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import training.g2.dto.Request.Inventory.InventoryReqDTO;
import training.g2.dto.Request.Inventory.InventoryUpdateReqDTO;
import training.g2.dto.Request.StockIn.StockInCreateReqDTO;
import training.g2.dto.Response.Inventory.InventoryResDTO;
import training.g2.dto.Response.StockIn.StockInCreateResDTO;
import training.g2.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<InventoryResDTO> createInventory(@RequestBody InventoryReqDTO inventoryReqDTO) {
        InventoryResDTO inventoryResDTO = inventoryService.createInventory(inventoryReqDTO);
        return ResponseEntity.ok(inventoryResDTO);
    }

    @GetMapping
    public ResponseEntity<List<InventoryResDTO>> getAllInventory() {
        List<InventoryResDTO> inventoryResDTOS = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventoryResDTOS);
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
