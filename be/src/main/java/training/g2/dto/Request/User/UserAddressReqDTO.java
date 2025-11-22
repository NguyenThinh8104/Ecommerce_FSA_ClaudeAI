package training.g2.dto.request.User;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class UserAddressReqDTO {
    private Long id;           // dùng cho update
    private String fullName;
    private String phone;
    private String province;
    private String district;
    private String ward;
    private String addressDetail;
    private Boolean isDefault; // có thể null
}


