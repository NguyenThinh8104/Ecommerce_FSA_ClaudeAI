package training.g2.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "districts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // DistrictID của GHN
    @Column(name = "ghn_district_id", nullable = false, unique = true)
    private Integer ghnDistrictId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    // FK -> provinces.id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

    // Quan hệ 1-nhiều với Ward
    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY)
    private List<Ward> wards;
}
