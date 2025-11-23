package training.g2.dto.Request.User;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class UserAddressReqDTO {
    private Long id;           // dùng cho update
    private String fullName;
    private String phone;
    private Long provinceId;
    private Long districtId;
    private Long wardId;
    private String addressDetail;
    private Boolean isDefault; // có thể null
}


