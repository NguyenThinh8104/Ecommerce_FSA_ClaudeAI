package training.g2.service;

import jakarta.servlet.http.HttpServletRequest;
import training.g2.dto.Request.ContactMessage.CreateContactMessageReq;
import training.g2.dto.common.PaginationDTO;
import training.g2.model.ContactMessage;

import java.time.LocalDate;
import java.util.List;

public interface ContactMessageService {
    ContactMessage create(CreateContactMessageReq createContactMessageReq, String ua, String xfwFor,
            HttpServletRequest request);

    PaginationDTO<List<ContactMessage>> list(
            ContactMessage.Status status,
            String search,
            LocalDate startDate, // yyyy-MM-dd
            LocalDate endDate, // yyyy-MM-dd (inclusive)
            int page, int size, String sort // ví dụ "createdAt,desc"
    );

    ContactMessage updateStatus(Long id, ContactMessage.Status status);

    void delete(Long id);

    long bulkDelete(List<Long> ids);

    long bulkUpdateStatus(List<Long> ids, ContactMessage.Status status);

}
