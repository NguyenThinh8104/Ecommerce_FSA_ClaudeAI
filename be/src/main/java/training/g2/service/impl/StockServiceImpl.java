package training.g2.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import training.g2.dto.response.Stock.StockResDTO;
import training.g2.exception.common.BusinessException;
import training.g2.mapper.StockMapper;
import training.g2.model.Stock;
import training.g2.repository.StockRepository;
import training.g2.service.StockService;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<StockResDTO> getAll() {
        return stockRepository.findAll().stream().map(StockMapper::toDTO).toList();
    }

    @Override
    public Stock getById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Stock not found"));
    }
}
