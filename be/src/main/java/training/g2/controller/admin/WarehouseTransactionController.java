package training.g2.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import training.g2.dto.Request.Inventory.TransactionReqDTO;
import training.g2.dto.Response.Inventory.TransactionResDTO;
import training.g2.dto.common.PaginationDTO;
import training.g2.model.ApiResponse;
import training.g2.service.WarehouseTransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/warehouse_transactions")
@RequiredArgsConstructor
public class WarehouseTransactionController {
    private final WarehouseTransactionService transactionService;

    @PostMapping
    @ResponseStatus (HttpStatus.CREATED)
    public ApiResponse<TransactionResDTO> createTransaction
            (@Valid @RequestBody TransactionReqDTO reqDTO) {
        TransactionResDTO result = transactionService.createTransaction(reqDTO);
        return new ApiResponse<>("Ghi nhận giao dịch kho thành công", result);
    }

    @GetMapping("/variant/{variantId}")
    public ApiResponse<PaginationDTO<List<TransactionResDTO>>> getHistoryByVariantId
            (@PathVariable Long variantId,
             @RequestParam(defaultValue = "1") int page,
             @RequestParam (defaultValue = "10") int size) {
        PaginationDTO<List<TransactionResDTO>> result = transactionService.getHistoryByVariant(variantId, page - 1, size);

        return new ApiResponse<>("Lấy lịch sử giao dịch thành công", result);
    }

    @GetMapping
    public ApiResponse<PaginationDTO<List<TransactionResDTO>>> getAllHistory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationDTO<List<TransactionResDTO>> result = transactionService.getAllHistory(page - 1, size);

        return new ApiResponse<>("Lấy toàn bộ lịch sử kho thành công", result);
    }
}
