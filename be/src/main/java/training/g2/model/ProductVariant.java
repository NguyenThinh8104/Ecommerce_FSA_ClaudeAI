package training.g2.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import training.g2.util.SecurityUtil;

@Entity
@Table(name = "product_variants")
@Getter
@Setter
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String sku;
    private double price;
    private int stock;
    private int sold;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "variants" })
    @JoinTable(name = "product_variant_value", joinColumns = @JoinColumn(name = "variant_id"), inverseJoinColumns = @JoinColumn(name = "attribute_values_id"))
    private List<AttributeValue> values = new ArrayList<>();

    private boolean deleted;
    @JsonFormat (pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat (pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.updatedAt = LocalDateTime.now();
    }

}
