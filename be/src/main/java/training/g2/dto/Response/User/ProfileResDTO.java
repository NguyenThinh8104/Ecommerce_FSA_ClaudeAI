package training.g2.dto.Response.User;

import lombok.Getter;
import lombok.Setter;
import training.g2.model.enums.GenderEnum;

@Getter
@Setter
public class ProfileResDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private GenderEnum gender;
    private String avatar;

}
