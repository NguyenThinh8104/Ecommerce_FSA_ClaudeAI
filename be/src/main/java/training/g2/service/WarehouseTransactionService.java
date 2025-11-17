package training.g2.service;

import training.g2.dto.Request.Inventory.TransactionReqDTO;
import training.g2.dto.Response.Inventory.TransactionResDTO;
import training.g2.dto.common.PaginationDTO;

import java.util.List;

public interface WarehouseTransactionService {
    TransactionResDTO createTransaction(TransactionReqDTO req);
    PaginationDTO<List<TransactionResDTO>> getHistoryByVariant(long variantId, int page, int size);
    PaginationDTO<List<TransactionResDTO>> getAllHistory(int page, int size);
}
