package training.g2.dto.Response.User;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import training.g2.model.enums.GenderEnum;
import training.g2.model.enums.UserStatusEnum;

@Getter
@Setter
public class UsersResDTO {
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private UserStatusEnum status;
    private GenderEnum gender;
    private Role role;
    private boolean deleted;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @Getter
    @Setter
    public static class Role {
        private long id;
    }
}
