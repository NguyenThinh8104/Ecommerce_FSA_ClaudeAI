package training.g2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stock_in")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "supplier_name")
    private String supplierName;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "stockIn", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockInItem> items = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();

        if (this.status == null) this.status = "DRAFT";
    }

    public void addItem(StockInItem item) {
        item.setStockIn(this);
        this.items.add(item);
    }
}
