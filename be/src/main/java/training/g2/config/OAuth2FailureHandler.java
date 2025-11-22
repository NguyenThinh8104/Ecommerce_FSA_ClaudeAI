package training.g2.config;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {
    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        String errorMsg = exception instanceof OAuth2AuthenticationException
                ? ((OAuth2AuthenticationException) exception).getError().getDescription()
                : exception.getMessage();

        if (errorMsg == null)
            errorMsg = "Authentication failed";

        String error = URLEncoder.encode(errorMsg, StandardCharsets.UTF_8);

        response.sendRedirect(frontendUrl + "/login?error=" + error);
    }

}
