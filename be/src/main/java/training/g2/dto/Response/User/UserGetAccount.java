package training.g2.dto.Response.User;

import lombok.Getter;
import lombok.Setter;

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
        private String role;
        private String avatar;

    }

}
