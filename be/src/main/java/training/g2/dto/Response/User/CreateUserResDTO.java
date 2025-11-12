package training.g2.dto.Response.User;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import training.g2.model.enums.GenderEnum;
import training.g2.model.enums.UserStatusEnum;

@Getter
@Setter
public class CreateUserResDTO {
    private long id;
    private String fullName;
    private String email;
    private String phone;
    private UserStatusEnum status;
    private GenderEnum gender;
    private Role role;
    private Instant createdAt;
    private String createdBy;
    private boolean deleted;

    @Getter
    @Setter
    public static class Role {
        private long id;
    }
}
