package training.g2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sliders")
@Getter
@Setter
public class Slider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String imageUrl;
    private int displayOrder;
    private boolean active;
}
