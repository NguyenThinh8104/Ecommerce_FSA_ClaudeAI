package training.g2.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import training.g2.dto.request.Stock.StockInCreateReqDTO;
import training.g2.dto.response.Stock.StockInResDTO;
import training.g2.exception.common.BusinessException;
import training.g2.mapper.StockInMapper;
import training.g2.model.ProductVariant;
import training.g2.model.Stock;
import training.g2.model.StockIn;
import training.g2.model.StockInItem;
import training.g2.repository.ProductVariantRepository;
import training.g2.repository.StockInItemRepository;
import training.g2.repository.StockInRepository;
import training.g2.repository.StockRepository;
import training.g2.service.StockInService;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static training.g2.constant.Constants.Message.*;


@Service
public class StockInServiceImpl implements StockInService {
    private final StockInRepository stockInRepository;
    private final StockInMapper stockInMapper;
    private final ProductVariantRepository productVariantRepository;
    private final StockInItemRepository stockInItemRepository;
    private final StockRepository stockRepository;

    public StockInServiceImpl(StockInRepository stockInRepository, StockInMapper stockInMapper, ProductVariantRepository productVariantRepository, StockInItemRepository stockInItemRepository, StockRepository stockRepository) {
        this.stockInRepository = stockInRepository;
        this.stockInMapper = stockInMapper;
        this.productVariantRepository = productVariantRepository;
        this.stockInItemRepository = stockInItemRepository;
        this.stockRepository = stockRepository;
    }


    @Override
    public StockInResDTO createStockIn(StockInCreateReqDTO stockInCreateReqDTO) {

        if (stockInCreateReqDTO.getSupplierName() == null || stockInCreateReqDTO.getSupplierName().trim().isEmpty()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,"SUPPLIER_REQUIRED");
        }

        if (stockInCreateReqDTO.getItems() == null || stockInCreateReqDTO.getItems().isEmpty()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,"ITEM_REQUIRED");
        }

        for (StockInCreateReqDTO.Item item : stockInCreateReqDTO.getItems()) {
            if (item.getProductVariantId() == null) {
                throw new BusinessException(HttpStatus.BAD_REQUEST,"PRODUCT_VARIANT_REQUIRED");
            }

            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new BusinessException(HttpStatus.BAD_REQUEST,"QUANTITY_REQUIRED");
            }

            if (item.getCost() == null || item.getCost() < 0) {
                throw new BusinessException(HttpStatus.BAD_REQUEST,"COST_REQUIRED");
            }
        }

        StockIn stockIn = new StockIn();

        stockIn.setSupplierName(stockInCreateReqDTO.getSupplierName());
        stockIn.setStatus("PENDING");
        stockIn.setCreatedAt(LocalDateTime.now());
        stockIn = stockInRepository.save(stockIn);

        List<StockInItem> items = new ArrayList<>();

        for (StockInCreateReqDTO.Item itemReq : stockInCreateReqDTO.getItems()) {

            ProductVariant productVariant = productVariantRepository.findById(itemReq.getProductVariantId())
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND));

            StockInItem item = new StockInItem();
            item.setProductVariant(productVariant);
            item.setStockIn(stockIn);
            item.setQuantity(itemReq.getQuantity());
            item.setCost(itemReq.getCost());

            items.add(item);
        }

        stockInItemRepository.saveAll(items);

        stockIn.setItems(items);

        return stockInMapper.toStockInResDTO(stockIn);
    }

    @Override
    public List<StockInResDTO> getAllStockIn() {
        List<StockIn> stockIns = stockInRepository.findAll();
        return stockInRepository.findAll().stream().map(StockInMapper::toStockInResDTO).toList();
    }

    @Override
    public StockInResDTO getStockInById(Long id) {
        StockIn stockIn = stockInRepository.findById(id)
                .orElseThrow(() ->new BusinessException(HttpStatus.NOT_FOUND, "STOCK_NOT_FOUND"));
        return stockInMapper.toStockInResDTO(stockIn);
    }

    @Override
    public StockInResDTO comfirmStockIn(Long id) {

        StockIn stockIn = stockInRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "STOCK_NOT_FOUND"));

        if (!stockIn.getStatus().equals("PENDING")) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "STOCK_IN_CONFIRM_OR_INVALID");
        }

        for (StockInItem item : stockIn.getItems()) {
            Stock stock = stockRepository.findByProductVariantId(item.getProductVariant().getId())
                    .orElse(null);

            if (stock == null) {
                stock = new Stock();
                stock.setProductVariant(item.getProductVariant());
                stock.setQuantity(item.getQuantity());
            } else {
                stock.setQuantity(stock.getQuantity() + item.getQuantity());
            }
            stockRepository.save(stock);
        }
        stockIn.setStatus("CONFIRMED");
        stockInRepository.save(stockIn);
        System.out.println("chay dc");
        return StockInMapper.toStockInResDTO(stockIn);
    }

    @Override
    public StockInResDTO cancelStockIn(Long id) {
        StockIn stockIn = stockInRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "STOCK_NOT_FOUND"));

        if (stockIn.getStatus().equals("CONFIRMED")) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "STOCK_IN_CONFIRM_OR_INVALID");
        }

        if (stockIn.getStatus().equals("CANCELLED")) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "STOCK_IN_CANCELLED");
        }

        stockIn.setStatus("CANCELLED");
        stockInRepository.save(stockIn);
        return stockInMapper.toStockInResDTO(stockIn);
    }
}
