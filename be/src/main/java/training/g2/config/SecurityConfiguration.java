package training.g2.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;

import org.springframework.web.client.RestTemplate;
import training.g2.service.UserService;
import training.g2.util.SecurityUtil;

@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration {
    @Value("${jwt.base64-secret}")
    private String jwtKey;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
            UserService userService, CustomOAuth2SuccessHandler oAuth2SuccessHandler,
            OAuth2FailureHandler oAuth2FailureHandler)
            throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/", "/api/v1/auth/login", "/api/v1/auth/refresh", "/storage/**",
                                        "/api/v1/auth/register", "/api/v1/email", "/api/v1/auth/account",
                                        "/api/v1/auth/update-password",
                                        "/api/v1/auth/resend-update-password",
                                        "/api/v1/category/**",
                                        "/api/v1/products/**",
                                        "/v3/api-docs/**",
                                        "/api/v1/auth/activate",
                                        "/api/v1/contact-message",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/api/v1/categories",
                                        "/api/v1/sliders",
                                        "/api/v1/home/**",
                                        "/api/v1/locations/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(user -> user.userService(new CustomOAuth2User(userService)))
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler))
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                System.out.println(">>> JWT error: " + e.getMessage());
                throw e;
            }
        };
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, SecurityUtil.JWT_ALGORITHM.getName());
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();

            Object roleClaim = jwt.getClaims().get("r");

            if (roleClaim instanceof String rolesStr) {
                // Tách chuỗi quyền theo dấu cách
                Arrays.stream(rolesStr.split(" "))
                        .filter(s -> !s.isBlank())
                        .forEach(role -> authorities.add(new SimpleGrantedAuthority(role.trim())));
            } else if (roleClaim instanceof Collection<?> roles) {
                // Nếu claim "r" là mảng (dạng ["ADMIN", "USER_READ", ...])
                roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
            }

            return authorities;
        });

        return converter;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
