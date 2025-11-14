package training.g2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Getter
@Setter
public class Inventory {
    @Id
    private long productVariantId;
    private int quantity;
    private int minimum;
    private int maximum;
    private LocalDateTime lastInAt;
    private LocalDateTime lastOutAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

    @Version
    private int version;
    }
