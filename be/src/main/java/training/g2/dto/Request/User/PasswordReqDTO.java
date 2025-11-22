package training.g2.dto.request.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordReqDTO {
    private String currentPassword;
    private String newPassword;
}
