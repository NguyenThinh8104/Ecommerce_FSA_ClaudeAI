package training.g2.dto.Response.Ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GhnLeadtimeResponse {

    private Integer code;
    private String message;
    private LeadtimeData data;

    @Data
    public static class LeadtimeData {

        private Long leadtime;  // 1763571599

        @JsonProperty("leadtime_order")
        private LeadtimeOrder leadtimeOrder;
    }

    @Data
    public static class LeadtimeOrder {

        @JsonProperty("from_estimate_date")
        private String fromEstimateDate; // "2025-11-19T16:59:59Z"

        @JsonProperty("to_estimate_date")
        private String toEstimateDate;   // "2025-11-19T16:59:59Z"
    }
}

