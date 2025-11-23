package training.g2.controller.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;

import training.g2.dto.Request.User.ChangePassReq;
import training.g2.dto.Request.User.LoginReqDTO;
import training.g2.dto.Request.User.RegisterReqDTO;
import training.g2.dto.Response.User.CreateUserResDTO;
import training.g2.dto.Response.User.LoginResDTO;
import training.g2.dto.Response.User.TokenReq;
import training.g2.dto.Response.User.UserGetAccount;
import training.g2.exception.common.BusinessException;
import training.g2.mapper.AuthMapper;
import training.g2.model.ApiResponse;
import training.g2.model.User;
import training.g2.model.enums.TokenTypeEnum;
import training.g2.model.enums.UserStatusEnum;
import training.g2.repository.UserRepository;
import training.g2.service.TokenService;
import training.g2.service.UserService;
import training.g2.util.SecurityUtil;

// AuthController.java
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
        private final AuthenticationManagerBuilder authenticationManagerBuilder;
        private final SecurityUtil securityUtil;
        private final UserService userService;
        private final TokenService tokenService;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Value("${jwt.refresh-token-validity-in-seconds}")
        private long refreshTokenExpiration;

        @Value("${app.frontend-url}")
        private String frontendUrl;

        public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
                        SecurityUtil securityUtil,
                        UserService userService,
                        TokenService tokenService, UserRepository userRepository,
                        PasswordEncoder passwordEncoder) {
                this.authenticationManagerBuilder = authenticationManagerBuilder;
                this.securityUtil = securityUtil;
                this.userService = userService;
                this.tokenService = tokenService;
                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
        }

        @PostMapping("/login")
        public ResponseEntity<ApiResponse<LoginResDTO>> login(@RequestBody LoginReqDTO loginDto) {
                try {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                        loginDto.getEmail(), loginDto.getPassword());

                        Authentication authentication = authenticationManagerBuilder.getObject()
                                        .authenticate(authenticationToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        User dbUser = userService.getUserByEmail(loginDto.getEmail());

                        if (dbUser.getStatus() == UserStatusEnum.BAN) {
                                throw new BusinessException(HttpStatus.FORBIDDEN,
                                                "Tài khoản của bạn đã bị khóa (banned)");
                        }

                        if (dbUser.getStatus() == UserStatusEnum.NOT_ACTIVE) {
                                throw new BusinessException(HttpStatus.FORBIDDEN,
                                                "Tài khoản chưa được kích hoạt, vui lòng kiểm tra email");
                        }

                        String roleCode = dbUser.getRole().getName();
                        String accessToken = securityUtil.createAccessToken(dbUser.getId(), roleCode,
                                        dbUser.getEmail());
                        String refreshToken = securityUtil.createRefreshToken(dbUser.getId(), roleCode,
                                        dbUser.getEmail());

                        LoginResDTO dto = AuthMapper.mapToLoginResponse(dbUser, accessToken);
                        tokenService.saveNewToken(dbUser, refreshToken, TokenTypeEnum.REFRESH_TOKEN);

                        ResponseCookie cookie = buildRefreshCookie(refreshToken, (int) refreshTokenExpiration);
                        return ResponseEntity.ok()
                                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                        .body(new ApiResponse<>("Đăng nhập thành công", dto));
                } catch (BadCredentialsException ex) {
                        throw new BusinessException(HttpStatus.UNAUTHORIZED, "Email hoặc mật khẩu không đúng");
                }
        }

        @GetMapping("/account")
        public ResponseEntity<ApiResponse<UserGetAccount>> getAccount() {
                String email = SecurityUtil.getCurrentUserLogin().orElse("");
                User currentUserDB = userService.getUserByEmail(email);
                UserGetAccount userGetAccount = AuthMapper.mapToUserGetAccount(currentUserDB);
                ApiResponse<UserGetAccount> apiResponse = new ApiResponse<>();
                apiResponse.setData(userGetAccount);
                apiResponse.setMessage("Lấy thông tin thành công");
                return ResponseEntity.ok().body(apiResponse);
        }

        @GetMapping("/refresh")
        public ResponseEntity<LoginResDTO> refresh(@CookieValue("refresh_token") String refreshCookie) {
                Jwt jwt = securityUtil.checkValidRefreshToken(refreshCookie);
                String email = jwt.getSubject();
                long userId = Long.parseLong(jwt.getClaimAsString("uid"));
                String role = jwt.getClaimAsString("r");
                User user = userService.getUserByEmail(email);
                LoginResDTO dto = AuthMapper.mapToLoginResponse(user, null);
                String newAccess = securityUtil.createAccessToken(userId, role, email);
                String newRefresh = securityUtil.createRefreshToken(userId, role, email);
                dto.setAccessToken(newAccess);

                tokenService.saveNewToken(user, newRefresh, TokenTypeEnum.REFRESH_TOKEN);

                ResponseCookie cookie = ResponseCookie.from("refresh_token", newRefresh)
                                .httpOnly(true).secure(true).path("/").maxAge(refreshTokenExpiration).build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(dto);
        }

        @PostMapping("/logout")
        public ResponseEntity<Void> logout() {
                String email = SecurityUtil.getCurrentUserLogin().orElse("");
                User user = userService.getUserByEmail(email);

                tokenService.deleteByToken(user);

                ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                                .httpOnly(true)
                                .secure(true)
                                .sameSite("Strict")
                                .path("/")
                                .maxAge(0)
                                .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                                .build();
        }

        private ResponseCookie buildRefreshCookie(String token, int maxAgeSeconds) {
                return ResponseCookie.from("refresh_token", token)
                                .httpOnly(true)
                                .secure(true)
                                .sameSite("Strict")
                                .path("/")
                                .maxAge(maxAgeSeconds)
                                .build();
        }

        @PostMapping("/register")
        public ResponseEntity<ApiResponse<CreateUserResDTO>> register(@RequestBody RegisterReqDTO req) {
                CreateUserResDTO dto = userService.register(req);
                return ResponseEntity.ok(new ApiResponse<>("Đăng kí người dùng thành công", dto));
        }

        @GetMapping("/activate")
        public ResponseEntity<Void> activateAccount(@RequestParam("token") String token) {
                User user = tokenService.getUserByToken(token);
                user.setStatus(UserStatusEnum.ACTIVE);
                userRepository.save(user);

                String frontendLoginUrl = frontendUrl + "/login";

                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(URI.create(frontendLoginUrl));

                return ResponseEntity.status(302).headers(headers).build();
        }

        @PostMapping("/update-password")
        public ResponseEntity<ApiResponse<User>> updatePassword(
                        @RequestBody ChangePassReq req) {
                User user = tokenService.validateToken(req.getToken());
                String password = passwordEncoder.encode(req.getPassword());
                user.setStatus(UserStatusEnum.ACTIVE);
                user.setPassword(password);
                userRepository.save(user);
                tokenService.deleteToken(req.getToken());
                return ResponseEntity.ok().body(new ApiResponse<User>("Cập nhật mật khẩu thành công", user));

        }

        @PostMapping("/resend-update-password")
        public ResponseEntity<ApiResponse<Void>> resend(@RequestBody TokenReq req) {
                userService.resendUpdatePassword(req.getToken());
                return ResponseEntity.ok(new ApiResponse<>("Đã gửi email đặt lại mật khẩu mới", null));
        }

}
