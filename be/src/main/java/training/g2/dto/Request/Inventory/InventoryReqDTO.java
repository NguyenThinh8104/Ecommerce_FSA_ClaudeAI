package training.g2.dto.Request.Inventory;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InventoryReqDTO {
    private int quantity;
    private int maximum;
    private int minimum;
    private LocalDateTime lastInAt;
    private LocalDateTime lastOutAt;
}
