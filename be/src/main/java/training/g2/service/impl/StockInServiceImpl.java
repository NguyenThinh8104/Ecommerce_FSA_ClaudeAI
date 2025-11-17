package training.g2.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import training.g2.dto.Request.StockIn.StockInCreateReqDTO;
import training.g2.dto.Response.StockIn.StockInCreateResDTO;
import training.g2.exception.common.BusinessException;
import training.g2.mapper.StockInMapper;
import training.g2.model.Inventory;
import training.g2.model.ProductVariant;
import training.g2.model.StockIn;
import training.g2.repository.InventoryRepository;
import training.g2.repository.ProductVariantRepository;
import training.g2.repository.StockInRepository;
import training.g2.service.StockInService;

import java.time.LocalDateTime;
import java.util.List;

import static training.g2.constant.Constants.Message.PRODUCT_NOT_FOUND;

@Service
public class StockInServiceImpl implements StockInService {
    private final ProductVariantRepository productVariantRepository;
    private final InventoryRepository inventoryRepository;
    private final StockInRepository stockInRepository;

    public StockInServiceImpl(StockInRepository stockInRepository, ProductVariantRepository productVariantRepository, InventoryRepository inventoryRepository) {
        this.stockInRepository = stockInRepository;
        this.productVariantRepository = productVariantRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<StockInCreateResDTO> getAllStockIns() {
        List<StockIn> stockInList = stockInRepository.findAll();
        return stockInList.stream().map(StockInMapper::toStockInCreateResDTO).toList();
    }

    @Override
    public StockInCreateResDTO getById(Long id) {
        StockIn stockIn = stockInRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND));
        return StockInMapper.toStockInCreateResDTO(stockIn);
    }

    @Override
    public StockInCreateResDTO addStock(Long productVariantId, StockInCreateReqDTO stockInCreateReqDTO) {
        // Lấy thông tin product_variant từ productVariantId
        ProductVariant productVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND));


        // Lấy thông tin inventory từ product_variant (bảng inventory)
        Inventory inventory = inventoryRepository.findByProductVariantId(productVariantId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND));

        if (stockInCreateReqDTO.getQuantity() < 0) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Khong duoc nhap so luong am");
        }

        if (stockInCreateReqDTO.getPrice() < 0) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Khong duoc nhap gia tien am");
        }

        // Kiêm tra không được vượt max
        int curr = inventory.getQuantity();
        int add = stockInCreateReqDTO.getQuantity();

        if (curr + add > inventory.getMaximum()) {
           throw new BusinessException(HttpStatus.BAD_REQUEST, "Số lượng nhập vượt quá mức tối đa (" + inventory.getMaximum() + ")!");
        }

        if (curr + add < inventory.getMinimum()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "So luong sau khi nhap hang khong duoc in hon so luong toi thieu (" + inventory.getMinimum() + ")!");
        }

        // Cập nhật số lượng trong inventory (bảng kho)
        inventory.setQuantity(inventory.getQuantity() + stockInCreateReqDTO.getQuantity());  // Thêm số lượng vào kho
        inventoryRepository.save(inventory);  // Lưu lại vào bảng inventory

        // Sau khi inventory đã được cập nhật, đồng bộ số lượng vào product_variant
        productVariant.setStock(productVariant.getStock() + stockInCreateReqDTO.getQuantity());// Cập nhật số lượng sản phẩm
        productVariantRepository.save(productVariant);  // Lưu lại vào bảng product_variant

        // Tạo bản ghi stock_in để ghi nhận thông tin nhập kho
        StockIn stockIn = new StockIn();
        stockIn.setProductVariantId(productVariantId);
        stockIn.setQuantity(stockInCreateReqDTO.getQuantity());
        stockIn.setPrice(stockInCreateReqDTO.getPrice());
        stockIn.setTotal(stockInCreateReqDTO.getTotalPrice());
        stockIn.setCreatedAt(LocalDateTime.now());
        stockIn.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        stockIn.setNote(stockInCreateReqDTO.getNote());
        stockIn.setStatus("COMPLETED");
        inventory.setLastInAt(stockIn.getCreatedAt());
        stockInRepository.save(stockIn);

        return StockInMapper.toStockInCreateResDTO(stockIn);
    }
}
