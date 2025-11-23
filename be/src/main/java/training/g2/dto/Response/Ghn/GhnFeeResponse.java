package training.g2.dto.Response.Ghn;



import lombok.Data;

@Data
public class GhnFeeResponse {

    private Integer code;
    private String message;
    private GhnFeeData data;

    @Data
    public static class GhnFeeData {
        private Integer total;         // tổng phí
        private Integer service_fee;    // phí dịch vụ
        private Integer insurance_fee;  // phí bảo hiểm
        // cần thêm gì thì bổ sung theo response thực tế
    }
}
