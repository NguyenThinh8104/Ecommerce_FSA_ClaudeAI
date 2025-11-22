package training.g2.config;

import java.io.IOException;
import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import training.g2.model.User;
import training.g2.model.enums.TokenTypeEnum;
import training.g2.service.TokenService;
import training.g2.service.UserService;
import training.g2.util.SecurityUtil;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final SecurityUtil securityUtil;
    private final TokenService tokenService;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;
    @Value("${app.frontend-url}")
    private String frontendUrl;

    public CustomOAuth2SuccessHandler(UserService userService, SecurityUtil securityUtil, TokenService tokenService) {
        this.userService = userService;
        this.securityUtil = securityUtil;
        this.tokenService = tokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = userService.getUserByEmail(email);

        Long userId = user.getId();
        String roleCode = user.getRole().getName();

        String accessToken = securityUtil.createAccessToken(userId, roleCode, email);
        String refreshToken = securityUtil.createRefreshToken(userId, roleCode, email);
        tokenService.saveNewToken(user, refreshToken, TokenTypeEnum.REFRESH_TOKEN);

        ResponseCookie cookie = buildRefreshCookie(refreshToken, (int) refreshTokenExpiration);

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendUrl + "/oauth2/callback")
                .queryParam("access_token", accessToken)
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
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
}
