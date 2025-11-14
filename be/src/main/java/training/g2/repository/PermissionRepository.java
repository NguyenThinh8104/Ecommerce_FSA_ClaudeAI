package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training.g2.model.Permission;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Optional<Permission> findByCode(String code);
    Set<Permission> findByCodeIn(Collection<String> codes);
}
