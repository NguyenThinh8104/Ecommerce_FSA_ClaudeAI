package training.g2.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import training.g2.dto.response.Stock.StockResDTO;
import training.g2.mapper.StockMapper;
import training.g2.model.Stock;
import training.g2.service.StockService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/stock")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<List<StockResDTO>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResDTO> getById(@PathVariable Long id) {
        Stock stock = stockService.getById(id);
        return ResponseEntity.ok(StockMapper.toDTO(stock));
    }
}
