package training.g2.dto.Response.Shipping;

import lombok.Data;

@Data
public class ShipmentResponse {

    private Long shipmentId;
    private Long orderId;

    private String provider;          // "GHN"
    private String providerOrderCode; // order_code của GHN
    private String trackingCode;      // mã tracking

    private Integer fee;
    private String currentStatus;     // CREATED/PICKING/DELIVERING/DELIVERED/FAILED...

    private String expectedDeliveryTime;
}
