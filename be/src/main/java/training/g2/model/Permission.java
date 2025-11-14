package training.g2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(
        name = "permissions",
        uniqueConstraints = @UniqueConstraint(name = "uk_permissions_code", columnNames = "code")
)
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 120)
    @Column(name = "code", nullable = false, length = 120)
    private String code; // ví dụ: PRODUCT_READ, PRODUCT_WRITE, ORDER_MANAGE

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;
}
