package training.g2.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import training.g2.dto.request.Stock.StockInCreateReqDTO;
import training.g2.dto.response.Stock.StockInResDTO;
import training.g2.service.StockInService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/stockIn")
public class StockInController {
    private final StockInService stockInService;

    public StockInController(StockInService stockInService) {
        this.stockInService = stockInService;
    }

    @PostMapping
    public ResponseEntity<StockInResDTO> createStockIn(@RequestBody StockInCreateReqDTO stockInCreateReqDTO) {
        return ResponseEntity.ok(stockInService.createStockIn(stockInCreateReqDTO));
    }

    @GetMapping
    public ResponseEntity<List<StockInResDTO>> getAllStockIn() {
        return ResponseEntity.ok(stockInService.getAllStockIn());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockInResDTO> getStockInById(@PathVariable Long id) {
        return ResponseEntity.ok(stockInService.getStockInById(id));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<StockInResDTO> confirmStockIn(@PathVariable Long id) {
        return ResponseEntity.ok(stockInService.comfirmStockIn(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<StockInResDTO> cancelStockIn(@PathVariable Long id) {
        return ResponseEntity.ok(stockInService.cancelStockIn(id));
    }
}
