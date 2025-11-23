
package training.g2.dto.Request.Ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GhnCreateOrderRequest {

    @JsonProperty("payment_type_id")
    private Integer paymentTypeId;

    private String note;

    @JsonProperty("required_note")
    private String requiredNote;

    // TO
    @JsonProperty("to_name")
    private String toName;

    @JsonProperty("to_phone")
    private String toPhone;

    @JsonProperty("to_address")
    private String toAddress;

    @JsonProperty("to_ward_code")
    private String toWardCode;

    @JsonProperty("to_district_id")
    private Integer toDistrictId;

    @JsonProperty("cod_amount")
    private Integer codAmount;

    private String content;

    private Integer weight;

    @JsonProperty("pick_station_id")
    private Integer pickStationId;

    @JsonProperty("deliver_station_id")
    private Integer deliverStationId;

    @JsonProperty("insurance_value")
    private Integer insuranceValue;

    @JsonProperty("service_id")
    private Integer serviceId;

    @JsonProperty("service_type_id")
    private Integer serviceTypeId;

    private String coupon;

    @JsonProperty("pick_shift")
    private java.util.List<Integer> pickShift;

    @JsonProperty("items")
    private java.util.List<GhnItem> items;
}
