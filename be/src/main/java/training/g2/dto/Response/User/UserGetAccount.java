package training.g2.dto.Response.User;

import lombok.Getter;
import lombok.Setter;
import training.g2.model.enums.UserStatusEnum;

@Getter
@Setter
public class UserGetAccount {
    private User user;

    @Getter
    @Setter
    public static class User {
        private long id;
        private String email;
        private String fullName;
        private String phone;
        private String gender;
        private String role;
        private String avatar;
        private UserStatusEnum status;

    }

}
