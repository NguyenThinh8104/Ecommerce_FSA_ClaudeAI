package training.g2.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.StringJoiner;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.util.Base64;

import training.g2.model.Role;
import training.g2.model.User;
import training.g2.repository.UserRepository;

@Service
public class SecurityUtil {
    @Value("${jwt.base64-secret}")
    private String jwtKey;

    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepo;

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    public SecurityUtil(JwtEncoder jwtEncoder, UserRepository userRepository) {
        this.jwtEncoder = jwtEncoder;
        this.userRepo = userRepository;
    }

    public String createAccessToken(long userId, String roleCode, String email) {
        Instant now = Instant.now();
        Instant exp = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);
        User user = userRepo.findUserById(userId).orElse(null);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(email) // <-- sub = email
                .claim("uid", userId) // <-- user id ngắn
                .claim("r", buildScope(user)) // <-- role code ngắn
                .issuedAt(now)
                .expiresAt(exp)
                .build();

        JwsHeader header = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public String createRefreshToken(long userId, String roleCode, String email) {
        Instant now = Instant.now();
        Instant exp = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(email)
                .claim("uid", userId)
                .claim("r", roleCode)
                .issuedAt(now)
                .expiresAt(exp)
                .build();

        JwsHeader header = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    public Jwt checkValidRefreshToken(String token) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            System.out.println(">>> REfresh Token error: " + e.getMessage());
            throw e;
        }

    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    public User getCurrentUser() {
        return getCurrentUserLogin()
                .flatMap(userRepo::findByEmailAndDeletedFalse)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng hiện tại"));
    }


    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        Role role = user.getRole();
        if (role != null) {
            // Thêm tên role
            stringJoiner.add(role.getName());

            // Thêm permission nếu có
            if (!CollectionUtils.isEmpty(role.getPermissions())) {
                role.getPermissions().forEach(permission -> stringJoiner.add(permission.getCode()));
            }
        }

        return stringJoiner.toString();
    }
    public Long getCurrentUserId() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            Number uid = jwt.getClaim("uid");
            return uid != null ? uid.longValue() : null;
        }
        return null;
    }

}
