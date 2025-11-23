package training.g2.service;

import org.springframework.stereotype.Service;
import training.g2.dto.response.Stock.StockResDTO;
import training.g2.model.Stock;

import java.util.List;
@Service
public interface StockService {
    List<StockResDTO> getAll();
    Stock getById(Long id);
}
