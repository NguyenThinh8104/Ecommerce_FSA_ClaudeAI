package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import training.g2.model.UserAddress;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

}
