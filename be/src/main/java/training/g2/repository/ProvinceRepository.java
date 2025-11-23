package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training.g2.model.Province;

import java.util.Optional;

public interface ProvinceRepository extends JpaRepository<Province, Long> {

    Optional<Province> findByGhnProvinceId(Integer ghnProvinceId);

    Optional<Province> findByName(String name);
}
