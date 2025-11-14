package training.g2.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import training.g2.model.Permission;
import training.g2.model.Role;
import training.g2.model.User;
import training.g2.model.enums.GenderEnum;
import training.g2.model.enums.UserStatusEnum;
import training.g2.repository.PermissionRepository;
import training.g2.repository.RoleRepository;
import training.g2.repository.UserRepository;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepo;

    /** Danh sách quyền nền tảng cho e-commerce cá nhân */
    private static final List<String> BASE_PERMISSIONS = List.of(
            // Users & roles (dùng cho ADMIN)
            "USER_READ", "USER_MANAGE",
            "ROLE_READ", "ROLE_MANAGE",

            // Product & catalog
            "PRODUCT_READ", "PRODUCT_WRITE",
            "CATEGORY_READ", "CATEGORY_WRITE",
            "INVENTORY_ADJUST",

            // Orders & payments
            "ORDER_READ", "ORDER_MANAGE",
            "PAYMENT_READ", "PAYMENT_MANAGE",

            // Discount & promotion
            "DISCOUNT_MANAGE",
            "CONTACT_MANAGE",

            // Content (blog/landing… nếu có)
            "CONTENT_READ", "CONTENT_WRITE",

            // Settings
            "SETTING_READ", "SETTING_WRITE");

    public DatabaseInitializer(UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder, PermissionRepository permissionRepo) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionRepo = permissionRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        seedRoles();
        seedPermissions();
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

    private void seedPermissions() {
        for (String code : BASE_PERMISSIONS) {
            permissionRepo.findByCode(code).orElseGet(() -> permissionRepo.save(Permission.builder()
                    .code(code)
                    .description(code.replace('_', ' ').toLowerCase())
                    .build()));
        }
    }

    private void seedRoles() {
        // ADMIN
        Role admin = roleRepository.findByName("ADMIN").orElseGet(
                () -> roleRepository.save(Role.builder().name("ADMIN").description("System administrator").build()));
        // Gán ALL permissions cho ADMIN
        var allPerms = permissionRepo.findByCodeIn(BASE_PERMISSIONS);
        admin.setPermissions(allPerms);
        ;
        roleRepository.save(admin);

        // CUSTOMER
        Role customer = roleRepository.findByName("CUSTOMER").orElseGet(
                () -> roleRepository.save(Role.builder().name("CUSTOMER").description("End customer").build()));
        // CUSTOMER không cần quyền hệ thống; để rỗng
        customer.setPermissions(new HashSet<>());
        roleRepository.save(customer);
    }

}
