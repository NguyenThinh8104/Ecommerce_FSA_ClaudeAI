package training.g2.service;

import training.g2.dto.response.Stock.StockResDTO;
import training.g2.model.Stock;

import java.util.List;

public interface StockService {
    List<StockResDTO> getAll();
    Stock getById(Long id);
}
