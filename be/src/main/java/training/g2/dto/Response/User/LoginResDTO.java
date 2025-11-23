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
        private String fullName;
        private String email;
        private String phone;
        private String avatar;
        private String role;
        private String gender;
        private String status;
        private String provider;
    }
}
