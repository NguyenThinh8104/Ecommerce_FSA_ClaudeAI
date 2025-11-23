package training.g2.dto.Response.Ghn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GhnDistrictResponse {

    private Integer code;
    private String message;
    private List<GhnDistrict> data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GhnDistrict {

        @JsonProperty("DistrictID")
        private Integer districtId;

        @JsonProperty("DistrictName")
        private String districtName;

        @JsonProperty("ProvinceID")
        private Integer provinceId;
    }
}
