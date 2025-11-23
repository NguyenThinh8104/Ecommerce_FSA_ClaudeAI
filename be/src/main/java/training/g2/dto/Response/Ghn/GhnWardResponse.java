package training.g2.dto.Response.Ghn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GhnWardResponse {

    private Integer code;
    private String message;
    private List<GhnWard> data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GhnWard {

        @JsonProperty("WardCode")
        private String wardCode;

        @JsonProperty("WardName")
        private String wardName;

        @JsonProperty("DistrictID")
        private Integer districtId;
    }
}