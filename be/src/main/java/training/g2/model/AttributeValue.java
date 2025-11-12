package training.g2.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "attribute_values")
@Getter
@Setter
public class AttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id", nullable = false)
    @JsonIgnore
    private Attribute attribute;

    @ManyToMany(mappedBy = "values", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductVariant> variants;

}
