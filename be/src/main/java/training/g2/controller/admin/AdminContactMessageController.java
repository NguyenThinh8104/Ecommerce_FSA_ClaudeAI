package training.g2.controller.admin;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import training.g2.dto.common.PaginationDTO;
import training.g2.model.ApiResponse;
import training.g2.model.ContactMessage;
import training.g2.service.impl.ContactMessageServiceImpl;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/contact-message")
public class AdminContactMessageController {
    private final ContactMessageServiceImpl contactMessageService;

    public AdminContactMessageController(ContactMessageServiceImpl contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

    @PostMapping
    public ApiResponse<PaginationDTO<List<ContactMessage>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) ContactMessage.Status status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        var data = contactMessageService.list(status, search, startDate, endDate, page, size, sort);
        return new ApiResponse<>("OK", data);
    }

    @GetMapping("/{id}")
    public ApiResponse<ContactMessage> get(@PathVariable Long id) {
        return new ApiResponse<>("OK", contactMessageService.get(id));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<ContactMessage> updateStatus(@PathVariable Long id,
            @RequestParam ContactMessage.Status status) {
        return new ApiResponse<>("OK", contactMessageService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        contactMessageService.delete(id);
        return new ApiResponse<>("Deleted", null);
    }

    @PatchMapping("/bulk/status")
    public ApiResponse<java.util.Map<String, Long>> bulkStatus(@RequestBody BulkStatusReq req) {
        long updated = contactMessageService.bulkUpdateStatus(req.getIds(), req.getStatus());
        return new ApiResponse<>("OK", java.util.Map.of("updated", updated));
    }

    @DeleteMapping("/bulk")
    public ApiResponse<java.util.Map<String, Long>> bulkDelete(@RequestBody BulkDeleteReq req) {
        long deleted = contactMessageService.bulkDelete(req.getIds());
        return new ApiResponse<>("OK", java.util.Map.of("deleted", deleted));
    }

    class BulkStatusReq {
        private java.util.List<Long> ids;
        private ContactMessage.Status status;

        public java.util.List<Long> getIds() {
            return ids;
        }

        public ContactMessage.Status getStatus() {
            return status;
        }
    }

    class BulkDeleteReq {
        private java.util.List<Long> ids;

        public java.util.List<Long> getIds() {
            return ids;
        }
    }
}
