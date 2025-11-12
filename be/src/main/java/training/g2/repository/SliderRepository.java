package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import training.g2.model.Slider;

@Repository
public interface SliderRepository extends JpaRepository<Slider, Long> {

}
