package training.g2.dto.Request.User;

import lombok.Getter;
import lombok.Setter;
import training.g2.model.enums.GenderEnum;

@Getter
@Setter
public class RegisterReqDTO {
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private GenderEnum gender;

}
