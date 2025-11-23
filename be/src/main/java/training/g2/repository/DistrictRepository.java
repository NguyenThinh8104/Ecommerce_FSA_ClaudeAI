package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training.g2.model.District;
import training.g2.model.Province;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, Long> {

    Optional<District> findByGhnDistrictId(Integer ghnDistrictId);

    List<District> findByProvince(Province province);

    List<District> findByProvinceId(Long provinceId);
}
