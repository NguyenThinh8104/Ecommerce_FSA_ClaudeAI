package training.g2.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import training.g2.model.Role;
import training.g2.model.User;
import training.g2.model.enums.GenderEnum;
import training.g2.model.enums.UserStatusEnum;
import training.g2.repository.RoleRepository;
import training.g2.repository.UserRepository;

import java.time.Instant;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmailAndDeletedFalse("admin@gmail.com").isPresent()) {
            System.out.println(">>> SKIP: Admin user already exists");
            return;
        }

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ADMIN");
                    r.setDescription("Administrator role with full access");
                    return roleRepository.save(r);
                });

        roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("USER");
                    r.setDescription("Standard user role with limited access");
                    return roleRepository.save(r);
                });

        User admin = new User();
        admin.setFullName("Admin System");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("@Son123"));
        admin.setPhone("0123456789");
        admin.setGender(GenderEnum.MALE);
        admin.setStatus(UserStatusEnum.ACTIVE);
        admin.setDeleted(false);
        admin.setRole(adminRole);
        admin.setCreatedAt(Instant.now());
        admin.setUpdatedAt(Instant.now());
        admin.setCreatedBy("SYSTEM");
        admin.setUpdatedBy("SYSTEM");

        userRepository.save(admin);

        System.out.println(">>> CREATED DEFAULT ADMIN ACCOUNT: admin@gmail.com / 123456");
    }
}
