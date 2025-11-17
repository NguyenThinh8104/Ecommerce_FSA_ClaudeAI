package training.g2.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import training.g2.dto.Request.StockIn.StockInCreateReqDTO;
import training.g2.dto.Response.StockIn.StockInCreateResDTO;
import training.g2.mapper.StockInMapper;
import training.g2.model.StockIn;
import training.g2.service.StockInService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/stockIn")
public class StockInController {
    private final StockInService stockInService;

    public StockInController(StockInService stockInService) {
        this.stockInService = stockInService;
    }

    @GetMapping
    public ResponseEntity<List<StockInCreateResDTO>> getStockIns() {
        List<StockInCreateResDTO> stockInList = stockInService.getAllStockIns();
        return ResponseEntity.ok(stockInList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockInCreateResDTO> getStockInById(@PathVariable Long id) {
        StockInCreateResDTO stockInCreateResDTO = stockInService.getById(id);
        return ResponseEntity.ok(stockInCreateResDTO);
    }

    @PostMapping("/{id}")
    public ResponseEntity<StockInCreateResDTO> addStockIn(@PathVariable Long id, @RequestBody StockInCreateReqDTO stockInCreateReqDTO) {
        StockInCreateResDTO stockInCreateResDTO = stockInService.addStock(id, stockInCreateReqDTO);
        return ResponseEntity.ok(stockInCreateResDTO);
    }
}
