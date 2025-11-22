package training.g2.mapper;

import org.springframework.stereotype.Component;
import training.g2.dto.response.Stock.StockInItemResDTO;
import training.g2.dto.response.Stock.StockInResDTO;
import training.g2.model.StockIn;
import training.g2.model.StockInItem;

import java.util.ArrayList;
import java.util.List;

@Component
public class StockInMapper {
    public static StockInResDTO toStockInResDTO(StockIn stockIn) {
        StockInResDTO stockInResDTO = new StockInResDTO();
        stockInResDTO.setId(stockIn.getId());
        stockInResDTO.setSupplierName(stockIn.getSupplierName());
        stockInResDTO.setStatus(stockIn.getStatus());
        stockInResDTO.setCreatedAt(stockIn.getCreatedAt());

        List<StockInItemResDTO> dto = new ArrayList<>();
        for (StockInItem stockInItem : stockIn.getItems()) {
            StockInItemResDTO stockInItemResDTO = new StockInItemResDTO();
            stockInItemResDTO.setProductVariantId(stockInItem.getProductVariant().getId());
            stockInItemResDTO.setSku(stockInItem.getProductVariant().getSku());
            stockInItemResDTO.setQuantity(stockInItem.getQuantity());
            stockInItemResDTO.setCost(stockInItem.getCost());
            dto.add(stockInItemResDTO);
        }
        stockInResDTO.setItems(dto);
        return stockInResDTO;
    }
}
