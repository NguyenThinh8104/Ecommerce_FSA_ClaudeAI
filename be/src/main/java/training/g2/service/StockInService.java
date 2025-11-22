package training.g2.service;

import training.g2.dto.request.Stock.StockInCreateReqDTO;
import training.g2.dto.response.Stock.StockInResDTO;

import java.util.List;

public interface StockInService {
    //Tao phieu nhap
    StockInResDTO createStockIn(StockInCreateReqDTO stockInCreateReqDTO);
    //Get tat ca phieu nhap
    List<StockInResDTO> getAllStockIn();
    //Get phieu nhap theo id
    StockInResDTO getStockInById(Long id);
    //Comfirm phieu nhap tang quantity
    StockInResDTO comfirmStockIn(Long id);
    //Cancel
    StockInResDTO cancelStockIn(Long id);
}
