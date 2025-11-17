package training.g2.dto.Response.StockIn;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StockInCreateResDTO {

    private Long id;
    private Long productVariantId;
    private int quantity;
    private double price;
    private double totalPrice;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime createdAt;
    private String createdBy;
    private String status;
    private String note;
}
