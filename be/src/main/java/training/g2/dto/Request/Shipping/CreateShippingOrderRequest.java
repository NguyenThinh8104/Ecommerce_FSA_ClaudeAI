package training.g2.dto.Request.Shipping;

import lombok.Data;
import training.g2.model.enums.PaymentMethodEnum;

import java.util.List;

@Data
public class CreateShippingOrderRequest {

    private Long addressId; // địa chỉ nhận hàng đã lưu trong UserAddress
    private Integer codAmount; // tiền thu hộ

    private Integer itemsValue; // tổng giá trị hàng để insurance
    private List<OrderItemDto> items;
    private PaymentMethodEnum paymentMethod;

}
