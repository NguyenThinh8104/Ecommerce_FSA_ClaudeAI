package training.g2.dto.Response.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TopProductDTO {
    private String name;
    private Long sold;
    private Double revenue;
}
