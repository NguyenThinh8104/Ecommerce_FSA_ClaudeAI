package training.g2.dto.request.Inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class InventoryAdjustReqDTO {
@Min(value = 1, message = "Số lượng phải lớn hơn 0")
private int quantity;

@NotBlank(message = "Lý do không được để trống")
@Size (max = 255, message = "Lý do tối đa 255 ký tự")

private String reason;

}
