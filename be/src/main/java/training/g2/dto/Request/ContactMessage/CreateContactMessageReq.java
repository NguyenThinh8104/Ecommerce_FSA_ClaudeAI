package training.g2.dto.Request.ContactMessage;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateContactMessageReq(
        @NotBlank
        @Size(max = 100) String fullName,
        @NotBlank @Email @Size(max = 150) String email,
        @Size(max = 20) String phone,
        @Size(max = 255) String subject,
        @NotBlank String message
) {
}
