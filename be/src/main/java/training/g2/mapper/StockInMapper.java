package training.g2.mapper;

import training.g2.dto.Request.StockIn.StockInCreateReqDTO;
import training.g2.dto.Response.StockIn.StockInCreateResDTO;
import training.g2.model.StockIn;

public class StockInMapper {

    public static StockIn toStockInEntity(StockInCreateReqDTO stockInCreatReqDTO) {
        StockIn stockIn = new StockIn();

        stockIn.setProductVariantId(stockInCreatReqDTO.getProductVariantId());
        stockIn.setQuantity(stockInCreatReqDTO.getQuantity());
        stockIn.setPrice(stockInCreatReqDTO.getPrice());
        stockIn.setNote(stockInCreatReqDTO.getNote());
        stockIn.setCreatedAt(stockInCreatReqDTO.getCreatedAt());
        stockIn.setCreatedBy(stockInCreatReqDTO.getCreatedBy());
        stockIn.setStatus("completed");  // Đặt trạng thái là "completed"
        stockIn.setTotal(stockInCreatReqDTO.getTotalPrice());
        return stockIn;
    }

    public static StockInCreateResDTO toStockInCreateResDTO(StockIn stockIn) {
        StockInCreateResDTO stockInCreateResDTO = new StockInCreateResDTO();

        stockInCreateResDTO.setId(stockIn.getId());
        stockInCreateResDTO.setProductVariantId(stockIn.getProductVariantId());
        stockInCreateResDTO.setQuantity(stockIn.getQuantity());
        stockInCreateResDTO.setPrice(stockIn.getPrice());
        stockInCreateResDTO.setTotalPrice(stockIn.getPrice() * stockIn.getQuantity());
        stockInCreateResDTO.setCreatedAt(stockIn.getCreatedAt());
        stockInCreateResDTO.setCreatedBy(stockIn.getCreatedBy());
        stockInCreateResDTO.setStatus(stockIn.getStatus());
        stockInCreateResDTO.setNote(stockIn.getNote());

        return stockInCreateResDTO;
    }
}
