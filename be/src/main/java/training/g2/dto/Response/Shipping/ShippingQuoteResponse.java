package training.g2.dto.Response.Shipping;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShippingQuoteResponse {

    private Integer fee;               // tổng phí
    private Integer serviceFee;        // phí dịch vụ
    private Integer insuranceFee;      // phí bảo hiểm nếu có
    private String expectedDeliveryTime; // time dự kiến giao (string ISO hoặc yyyy-MM-dd)

    public ShippingQuoteResponse(Integer fee) {
        this.fee = fee;
    }
}
