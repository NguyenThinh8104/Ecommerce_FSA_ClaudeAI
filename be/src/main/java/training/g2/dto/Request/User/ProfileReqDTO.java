package training.g2.dto.Request.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import training.g2.model.enums.GenderEnum;

@Getter
@Setter
public class ProfileReqDTO {
    @NotBlank(message = "fullName không được trống")
    private String fullName;

    @NotBlank(message = "phone không được trống")
    @Pattern(regexp = "^[0-9]{8,15}$", message = "Số điện thoại không hợp lệ")
    private String phone;


    private GenderEnum gender;
    private String avatar;

}
