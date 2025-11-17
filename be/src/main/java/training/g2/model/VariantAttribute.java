package training.g2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "variant_attributes")
@Getter
@Setter
public class VariantAttribute {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (length = 100, nullable = false)
    private String name;
    @Column (length = 100, nullable = false)
    private String value;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "variant_id", nullable = false)
    private ProductVariant variant;
}
