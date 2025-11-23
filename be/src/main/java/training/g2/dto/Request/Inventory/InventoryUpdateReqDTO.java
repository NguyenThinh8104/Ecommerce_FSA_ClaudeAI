package training.g2.dto.request.Inventory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryUpdateReqDTO {
    private int minimum;
    private int maximum;
}
