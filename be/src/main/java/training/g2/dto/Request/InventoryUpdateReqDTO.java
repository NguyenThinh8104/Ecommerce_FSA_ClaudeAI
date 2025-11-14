package training.g2.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryUpdateReqDTO {
    private int minimum;
    private int maximum;
}
