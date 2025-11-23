package training.g2.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import training.g2.dto.Response.Ghn.GhnDistrictResponse;
import training.g2.dto.Response.Ghn.GhnProvinceResponse;
import training.g2.dto.Response.Ghn.GhnWardResponse;
import training.g2.model.*;
import training.g2.model.enums.GenderEnum;
import training.g2.model.enums.UserStatusEnum;
import training.g2.repository.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepo;
    private final GhnLocationClient ghnLocationClient;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;

    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "@Son123";
    private static final String ADMIN_PHONE = "0123456789";
    private static final String SYSTEM = "SYSTEM";

    /**
     * Danh sách quyền nền tảng cho e-commerce cá nhân
     */
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

            // Content
            "CONTENT_READ", "CONTENT_WRITE",

            // Contact messages
            "MESSAGE_READ", "MESSAGE_MANAGE",

            // Settings
            "SETTING_READ", "SETTING_WRITE"
    );

    @Override

    public void run(String... args) {
        log.info(">>> DATABASE INITIALIZER START");

        seedPermissions();
        seedRoles();
//        syncLocations();
        seedAdminUser();

    }

    // ===================== SEED PERMISSION & ROLE =====================

    private void seedPermissions() {
        BASE_PERMISSIONS.forEach(code ->
                permissionRepo.findByCode(code).orElseGet(() ->
                        permissionRepo.save(
                                Permission.builder()
                                        .code(code)
                                        .description(code.replace('_', ' ').toLowerCase())
                                        .build()
                        )
                )
        );
    }

    private Role ensureRole(String name, String description) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(
                        Role.builder()
                                .name(name)
                                .description(description)
                                .build()
                ));
    }

    private void seedRoles() {
        // ADMIN: có toàn bộ quyền
        Role admin = ensureRole("ADMIN", "System administrator");
        var allPerms = permissionRepo.findByCodeIn(BASE_PERMISSIONS);
        admin.setPermissions(new HashSet<>(allPerms));
        roleRepository.save(admin);

        // USER: hiện tại không gán quyền hệ thống (nếu cần thì filter permission ở đây)
        Role customer = ensureRole("USER", "End customer");
        customer.setPermissions(new HashSet<>());
        roleRepository.save(customer);
    }

    private void seedAdminUser() {
        if (userRepository.findByEmailAndDeletedFalse(ADMIN_EMAIL).isPresent()) {
            log.info(">>> SKIP: Admin user already exists");
            return;
        }

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() ->
                        new IllegalStateException("ADMIN role must exist before creating admin user"));

        User admin = new User();
        admin.setFullName("Admin System");
        admin.setEmail(ADMIN_EMAIL);
        admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
        admin.setPhone(ADMIN_PHONE);
        admin.setGender(GenderEnum.MALE);
        admin.setStatus(UserStatusEnum.ACTIVE);
        admin.setDeleted(false);
        admin.setRole(adminRole);
        admin.setCreatedAt(Instant.now());
        admin.setUpdatedAt(Instant.now());
        admin.setCreatedBy(SYSTEM);
        admin.setUpdatedBy(SYSTEM);

        userRepository.save(admin);
        log.info(">>> CREATED DEFAULT ADMIN ACCOUNT: {} / {}", ADMIN_EMAIL, ADMIN_PASSWORD);
    }

    // ===================== SYNC LOCATION (GHN) =====================

//    private void syncLocations() {
//        syncProvinces();
//        syncDistrictsAndWards();
//    }

    private void syncProvinces() {
        GhnProvinceResponse res = ghnLocationClient.getProvinces();
        if (res == null || res.getData() == null || res.getData().isEmpty()) {
            log.warn(">>> GHN provinces response is empty");
            return;
        }



        res.getData().stream()
                .filter(p -> p.getProvinceId() != null && p.getProvinceName() != null)
                .forEach(p -> {
                    Province province = provinceRepository
                            .findByGhnProvinceId(p.getProvinceId())
                            .orElseGet(Province::new);

                    province.setGhnProvinceId(p.getProvinceId());
                    province.setName(p.getProvinceName());

                    provinceRepository.save(province);
                });
    }

    private void syncDistrictsAndWards() {
        var provinces = provinceRepository.findAll();
        if (provinces.isEmpty()) {
            log.warn(">>> No provinces in DB, skip district/ward sync");
            return;
        }

        for (Province province : provinces) {
            // 1. Sync Districts cho từng Province
            GhnDistrictResponse dRes = ghnLocationClient.getDistricts(province.getGhnProvinceId());
            if (dRes == null || dRes.getData() == null || dRes.getData().isEmpty()) {
                continue;
            }

            for (GhnDistrictResponse.GhnDistrict d : dRes.getData()) {
                District district = districtRepository
                        .findByGhnDistrictId(d.getDistrictId())
                        .orElseGet(District::new);

                district.setGhnDistrictId(d.getDistrictId());
                district.setName(d.getDistrictName());
                district.setProvince(province);

                district = districtRepository.save(district);

                // 2. Sync Wards cho từng District
                syncWardsForDistrict(district);
            }
        }
    }

    private void syncWardsForDistrict(District district) {
        GhnWardResponse wRes = ghnLocationClient.getWards(district.getGhnDistrictId());
        if (wRes == null || wRes.getData() == null || wRes.getData().isEmpty()) {
            return;
        }

        for (GhnWardResponse.GhnWard w : wRes.getData()) {
            Ward ward = wardRepository
                    .findByGhnWardCode(w.getWardCode())
                    .orElseGet(Ward::new);

            ward.setGhnWardCode(w.getWardCode());
            ward.setName(w.getWardName());
            ward.setDistrict(district);

            wardRepository.save(ward);
        }
    }
}
