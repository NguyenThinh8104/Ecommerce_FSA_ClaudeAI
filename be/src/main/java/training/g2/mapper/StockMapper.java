package training.g2.mapper;

import training.g2.dto.response.Stock.StockResDTO;
import training.g2.model.Stock;

public class StockMapper {
    public static StockResDTO toDTO(Stock stock) {
        StockResDTO dto = new StockResDTO();
        dto.setVariantId(stock.getProductVariant().getId());
        dto.setQuantity(stock.getQuantity());
        dto.setReserved(stock.getReserved());
        return dto;
    }
}
