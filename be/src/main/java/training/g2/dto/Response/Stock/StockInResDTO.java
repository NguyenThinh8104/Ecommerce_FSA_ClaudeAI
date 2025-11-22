package training.g2.dto.response.Stock;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class StockInResDTO {
    private Long id;
    private String supplierName;
    private String status;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    private List<StockInItemResDTO> items;
}
