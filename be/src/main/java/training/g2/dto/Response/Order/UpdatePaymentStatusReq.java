package training.g2.dto.Response.Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePaymentStatusReq {
    private  String uuid;
    private String status;

}
