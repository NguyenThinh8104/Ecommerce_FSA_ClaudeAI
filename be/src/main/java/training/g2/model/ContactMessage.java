package training.g2.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
@Getter
@Setter
@Entity
@Table(name = "contact_messages",
        indexes = {
                @Index(name = "idx_contact_messages_status", columnList = "status"),
                @Index(name = "idx_contact_messages_created_at", columnList = "created_at")
        })
public class ContactMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=100, nullable=false)
    private String fullName;

    @Column(length=150, nullable=false)
    private String email;

    @Column(length=20)
    private String phone;

    @Column(length=255)
    private String subject;

    @Lob @Column(nullable=false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(length=10, nullable=false)
    private Status status = Status.PENDING;

    @Column(name="ip_address", length=45)
    private String ipAddress;

    @Column(name="user_agent", length=255)
    private String userAgent;

    @Column(name="created_at", updatable=false)
    private Instant createdAt;

    @Column(name="updated_at")
    private Instant updatedAt;

    public enum Status { PENDING, READ, RESOLVED }

    @PrePersist void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }
    @PreUpdate void onUpdate() { updatedAt = Instant.now(); }

}
