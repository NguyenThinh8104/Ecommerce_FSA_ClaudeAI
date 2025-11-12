package training.g2.dto.Response.User;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResDTO {
    @JsonProperty("access_token")
    private String accessToken;
    private UserLogin user;

    @Getter
    @Setter
    public static class UserLogin {
        private long id;
        private String fullname;
        private String email;
        private String phone;
        private String avatar;
        private long role;
        private String status;
    }
}
