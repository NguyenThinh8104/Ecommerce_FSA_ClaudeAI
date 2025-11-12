package training.g2.controller.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import training.g2.dto.Request.ContactMessage.CreateContactMessageReq;
import training.g2.model.ApiResponse;
import training.g2.model.ContactMessage;
import training.g2.service.impl.ContactMessageServiceImpl;
@RestController
@RequestMapping("/api/v1/contact-message")
public class ContactMessageController {
    private final ContactMessageServiceImpl contactMessageService;

    public ContactMessageController(ContactMessageServiceImpl contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

    @PostMapping
    public ApiResponse<ContactMessage> create(
            @Validated @RequestBody CreateContactMessageReq req,
            @RequestHeader(value = "User-Agent", required = false) String ua,
            @RequestHeader(value = "X-Forwarded-For", required = false) String xfwdFor,
            HttpServletRequest http) {
        return ApiResponse.<ContactMessage>builder()
                .message("Tạo thành công liên hệ")
                .data(contactMessageService.create(req, ua, xfwdFor, http))
                .build();

    }
}
