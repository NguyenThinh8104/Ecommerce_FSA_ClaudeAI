package training.g2.dto.response.Dashboard;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentOrderDTO {
    private long orderId;
    private String customerName;
    private double totalPrice;
    private String status;
    private String createdAt;
}
