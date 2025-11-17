package training.g2.service;

import training.g2.dto.Request.StockIn.StockInCreateReqDTO;
import training.g2.dto.Response.StockIn.StockInCreateResDTO;

import java.util.List;

public interface StockInService {
    List<StockInCreateResDTO> getAllStockIns();
    StockInCreateResDTO getById(Long id);
    StockInCreateResDTO addStock(Long id, StockInCreateReqDTO stockInCreatReqDTO);
}
