package training.g2.controller.admin;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import training.g2.dto.Request.Inventory.InventoryAdjustReqDTO;
import training.g2.dto.Request.Inventory.InventoryUpdateReqDTO;
import training.g2.dto.Response.Inventory.InventoryResDTO;
import training.g2.model.ApiResponse;
import training.g2.service.InventoryService;

import static training.g2.constant.Constants.Message.*;

@RestController
@RequestMapping("/api/v1/admin/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<ApiResponse<InventoryResDTO>> getInventory(@PathVariable long id) {

        InventoryResDTO inventoryResDTO = inventoryService.getInventoryById(id);

        return ResponseEntity.ok(new ApiResponse<>(GET_PRODUCT_SUCCESS, inventoryResDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryResDTO>> updateInventory(
            @PathVariable long id, @RequestBody InventoryUpdateReqDTO inventoryUpdateReqDTO) {
        InventoryResDTO inventoryResDTO = inventoryService.updateInventory(id, inventoryUpdateReqDTO);
        return ResponseEntity.ok(new ApiResponse<>(PRODUCT_UPDATED_SUCCESS, inventoryResDTO));
    }

    @PostMapping ("/{id}/stockout")
    public ResponseEntity<ApiResponse<InventoryResDTO>> dispatchStock(
            @PathVariable long id,
            @Valid @RequestBody InventoryAdjustReqDTO ReqDTO){

        InventoryResDTO invenResDTO = inventoryService.StockOut(id,ReqDTO);

        return ResponseEntity.ok(new ApiResponse<>(INVENTORY_ADJUST_SUCCESS, invenResDTO));
    }

}
