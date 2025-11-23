package training.g2.dto.Response.Ghn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GhnProvinceResponse {
    private Integer code;
    private String message;
    private List<GhnProvince> data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GhnProvince {

        @JsonProperty("ProvinceID")
        private Integer provinceId;

        @JsonProperty("ProvinceName")
        private String provinceName;
    }
}
