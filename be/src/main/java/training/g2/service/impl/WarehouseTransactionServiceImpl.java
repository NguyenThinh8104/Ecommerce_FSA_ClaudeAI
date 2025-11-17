package training.g2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.g2.dto.Request.Inventory.TransactionReqDTO;
import training.g2.dto.Response.Inventory.TransactionResDTO;
import training.g2.dto.common.PaginationDTO;
import training.g2.exception.common.BusinessException;
import training.g2.model.Inventory;
import training.g2.model.ProductVariant;
import training.g2.model.WarehouseTransaction;
import training.g2.model.enums.TransactionTypeEnum;
import training.g2.repository.InventoryRepository;
import training.g2.repository.ProductVariantRepository;
import training.g2.repository.WarehouseTransactionRepository;
import training.g2.service.WarehouseTransactionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static training.g2.constant.Constants.Message.*;

@Service
public class WarehouseTransactionServiceImpl implements WarehouseTransactionService {
 private final WarehouseTransactionRepository transactionRepo;
 private final InventoryRepository inventoryRepo;
 private final ProductVariantRepository variantRepo;

    public WarehouseTransactionServiceImpl(WarehouseTransactionRepository transactionRepo, InventoryRepository inventoryRepo, ProductVariantRepository variantRepo) {
        this.transactionRepo = transactionRepo;
        this.inventoryRepo = inventoryRepo;
        this.variantRepo = variantRepo;
    }


    @Override
    public TransactionResDTO createTransaction(TransactionReqDTO req) {
        ProductVariant variant = variantRepo.findById(req.getVariantId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, VARIANT_NOT_FOUND));
        Inventory inventory = inventoryRepo.findById(req.getVariantId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Chưa có thông tin tồn kho cho biến thể này (ID: " + req.getVariantId() + "). Vui lòng khởi tạo kho trước."));

        int currentQuantity = inventory.getQuantity();
        int requestQuantity = req.getQuantity();
        int changeQuantity = 0;
        int newQuantity = currentQuantity;

        if (req.getType() == TransactionTypeEnum.EXPORT){
            if(currentQuantity < requestQuantity){
                throw new BusinessException(HttpStatus.BAD_REQUEST, String.format("Không đủ hàng để xuất.\n Tồn kho %d - Yêu cầu: %d",currentQuantity,requestQuantity));
            }
            changeQuantity = requestQuantity;
            newQuantity = currentQuantity - requestQuantity;
            inventory.setLastOutAt(LocalDateTime.now());
            ProductVariant productVariant = variantRepo.findById(req.getVariantId())
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,VARIANT_NOT_FOUND));
            productVariant.setStock(productVariant.getStock() - changeQuantity);

        } else if(req.getType() == TransactionTypeEnum.IMPORT){
            changeQuantity = requestQuantity;
            newQuantity = currentQuantity + requestQuantity;
            inventory.setLastInAt(LocalDateTime.now());
            ProductVariant productVariant = variantRepo.findById(req.getVariantId())
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,VARIANT_NOT_FOUND));
            productVariant.setStock(productVariant.getStock() + changeQuantity);

        }
        inventory.setQuantity(newQuantity);
        inventoryRepo.save(inventory);

        ProductVariant proV = variantRepo.findById(req.getVariantId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, VARIANT_NOT_FOUND));
        proV.setStock(currentQuantity);

        String refCode = req.getReferenceCode();
        if (refCode == null || refCode.trim().isEmpty()){
            String prefix = (req.getType() == TransactionTypeEnum.IMPORT) ? "IMP" : "EXP";
            String suffix = UUID.randomUUID().toString().substring(0, 8);
            refCode = prefix + "-" + suffix;
        }

        WarehouseTransaction trans = new WarehouseTransaction();
        trans.setProductVariant(variant);
        trans.setQuantity(changeQuantity);
        trans.setBalanceAfter(newQuantity);
        trans.setType(req.getType());
        trans.setReferenceCode(refCode);
        trans.setNote(req.getNote());

        WarehouseTransaction savedTransaction = transactionRepo.save(trans);

        return mapToResDTO(savedTransaction);
    }

    @Override
    public PaginationDTO<List<TransactionResDTO>> getHistoryByVariant(long variantId, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

            Page<WarehouseTransaction> warehousePage = transactionRepo.findByProductVariantIdOrderByCreatedAtDesc(variantId, pageable);

            return buildPaginationResponse(warehousePage,page,size);
        } catch (Exception e){
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Lấy lịch sử giao dịch thất bại", e);
        }
    }

    @Override
    public PaginationDTO<List<TransactionResDTO>> getAllHistory(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<WarehouseTransaction> warehousePage = transactionRepo.findAll(pageable);
            return buildPaginationResponse(warehousePage,page,size);
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Lấy danh sách giao dịch thất bại", e);
        }
    }

    private TransactionResDTO mapToResDTO(WarehouseTransaction entity) {
        TransactionResDTO dto = new TransactionResDTO();
        dto.setId(entity.getId());
        if (entity.getProductVariant() != null) {
            dto.setVariantSKU(entity.getProductVariant().getSku());
            if (entity.getProductVariant().getProduct() != null) {
                dto.setProductName(entity.getProductVariant().getProduct().getName());
            }
        }
        dto.setQuantityChange(entity.getQuantity());
        dto.setBalanceAfter(entity.getBalanceAfter());
        dto.setTransactionType(entity.getType());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setNote(entity.getNote());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedBy(entity.getCreatedBy());

        return dto;
    }

    private PaginationDTO<List<TransactionResDTO>> buildPaginationResponse(Page<WarehouseTransaction> warehousePage, int page, int size) {
        List<TransactionResDTO> items = warehousePage.getContent().stream().map(this::mapToResDTO).collect(Collectors.toList());

        return PaginationDTO.<List<TransactionResDTO>>builder()
                .page(page + 1)
                .size(size)
                .total(warehousePage.getTotalElements())
                .items(items)
                .build();
    }
}
