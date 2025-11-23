package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import training.g2.model.Slider;

import java.util.List;


@Repository
public interface SliderRepository extends JpaRepository<Slider, Long>, JpaSpecificationExecutor<Slider> {
    List<Slider> findAllByActiveTrueOrderByPositionAsc();
}
