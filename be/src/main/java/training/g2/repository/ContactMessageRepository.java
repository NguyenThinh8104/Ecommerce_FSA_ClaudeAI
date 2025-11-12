package training.g2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import training.g2.model.ContactMessage;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long>, JpaSpecificationExecutor<ContactMessage> {
}
