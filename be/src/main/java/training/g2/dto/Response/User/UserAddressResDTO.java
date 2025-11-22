package training.g2.dto.response.User;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserAddressResDTO {
    private Long id;
    private String fullName;
    private String phone;
    private String province;
    private String district;
    private String ward;
    private String addressDetail;
    private boolean isDefault;
    private Instant createdAt;
}

