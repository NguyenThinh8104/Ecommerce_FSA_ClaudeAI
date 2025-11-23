package training.g2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_details")
@Getter
@Setter
public class CartDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @Column(precision = 12, scale = 2)
    private BigDecimal price; // giá snapshot

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
}
