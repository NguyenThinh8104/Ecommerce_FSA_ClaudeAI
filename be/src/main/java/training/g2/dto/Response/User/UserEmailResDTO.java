

// UserEmailResDTO.java
package training.g2.dto.Response.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserEmailResDTO {
    private Long id;
    private String fullName;
    private String email;
}

