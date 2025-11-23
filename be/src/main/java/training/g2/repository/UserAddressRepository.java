package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import training.g2.model.UserAddress;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    List<UserAddress> findByUser_Id(Long userId);

    Optional<UserAddress> findByIdAndUser_Id(Long id, Long userId);

    @Modifying
    @Query("UPDATE UserAddress a SET a.isDefault = false WHERE a.user.id = :userId")
    void clearDefaultByUserId(Long userId);

    Optional<UserAddress> findFirstByUser_IdOrderByCreatedAtAsc(Long userId);
}


