package training.g2.dto.Response.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import training.g2.model.Order;

@Getter
@Setter
@AllArgsConstructor
public class CreateOrderResponse {

    private Order order;
    private String paymentUrl;
}
