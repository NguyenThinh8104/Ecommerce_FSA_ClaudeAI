package training.g2.dto.request.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePassReq {
    private String token;
    private String password;
}
