package training.g2.dto.Request.Ghn;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GhnFeeRequest {

    @JsonProperty("from_district_id")
    private Integer fromDistrictId;

    @JsonProperty("from_ward_code")
    private String fromWardCode;

    // GHN thường dùng service_id, service_type_id optional.
    @JsonProperty("service_id")
    private Integer serviceId;

    @JsonProperty("service_type_id")
    private Integer serviceTypeId;

    @JsonProperty("to_district_id")
    private Integer toDistrictId;

    @JsonProperty("to_ward_code")
    private String toWardCode;

    @JsonProperty("weight")
    private Integer weight;

    @JsonProperty("length")
    private Integer length;

    @JsonProperty("width")
    private Integer width;

    @JsonProperty("height")
    private Integer height;

    @JsonProperty("insurance_value")
    private Integer insuranceValue;

    @JsonProperty("coupon")
    private String coupon;
}

