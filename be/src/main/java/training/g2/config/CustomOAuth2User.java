package training.g2.config;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import training.g2.model.Role;
import training.g2.model.User;
import training.g2.model.enums.UserStatusEnum;
import training.g2.service.UserService;

@Component
public class CustomOAuth2User extends DefaultOAuth2UserService {
    @Autowired
    private final UserService userService;

    public CustomOAuth2User(UserService userService) {
        this.userService = userService;

    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        if (email == null) {
            throw new OAuth2AuthenticationException("Email không tồn tại trong tài khoản Google");
        }

        User user = userService.findByEmail(email);

        if (user == null) {
            Role role = userService.getRoleByName("USER");

            User oUser = new User();
            oUser.setEmail(email);
            oUser.setFullName((String) attributes.get("name"));
            oUser.setAvatar((String) attributes.get("picture"));
            oUser.setStatus(UserStatusEnum.ACTIVE);
            oUser.setRole(role);
            oUser.setProvider("GOOGLE");
            userService.saveUser(oUser);

            user = oUser;
        } else {
            if (user.deleted || user.getStatus() == UserStatusEnum.BAN) {
                OAuth2Error error = new OAuth2Error(
                        "account_locked",
                        "Tài khoản của bạn đã bị khoá",
                        null);
                throw new OAuth2AuthenticationException(error, error.getDescription());
            }
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())),
                attributes,
                "email");
    }
}
