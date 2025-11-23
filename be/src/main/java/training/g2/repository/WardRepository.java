package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training.g2.model.District;
import training.g2.model.Ward;

import java.util.List;
import java.util.Optional;

public interface WardRepository extends JpaRepository<Ward, Long> {

    Optional<Ward> findByGhnWardCode(String ghnWardCode);

    List<Ward> findByDistrict(District district);

    List<Ward> findByDistrictId(Long districtId);
}
