package training.g2.dto.Request.StockIn;

import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@Getter
@Setter
public class StockInCreateReqDTO {
    private long productVariantId;
    private int quantity;
    private double price;
    private double totalPrice;
    private String note;
    private LocalDateTime createdAt;
    private String createdBy;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.createdBy = SecurityContextHolder.getContext().getAuthentication().getName();
        this.totalPrice = price * quantity;
    }
}
