package training.g2.dto.Request.Shipping;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShippingQuoteRequest {

    private Long addressId;

}
