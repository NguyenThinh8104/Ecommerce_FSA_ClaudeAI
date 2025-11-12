package training.g2.mapper;

// AuthMapper.java
import training.g2.dto.Response.User.LoginResDTO;
import training.g2.dto.Response.User.UserGetAccount;
import training.g2.model.User;

public class AuthMapper {

    public static LoginResDTO mapToLoginResponse(User user, String accessToken) {
        LoginResDTO res = new LoginResDTO();
        res.setAccessToken(accessToken);

        LoginResDTO.UserLogin u = new LoginResDTO.UserLogin();
        u.setId(user.getId());
        u.setFullname(user.getFullName());
        u.setEmail(user.getEmail());
        u.setPhone(user.getPhone());
        u.setAvatar(user.getAvatar());
        u.setRole(user.getRole().getId());
        u.setStatus(user.getStatus().toString());
        res.setUser(u);
        return res;
    }

    public static UserGetAccount mapToUserGetAccount(User dbUser) {
        UserGetAccount response = new UserGetAccount();

        UserGetAccount.User userDto = new UserGetAccount.User();
        userDto.setId(dbUser.getId());
        userDto.setEmail(dbUser.getEmail());
        userDto.setFullName(dbUser.getFullName());
        userDto.setRole(dbUser.getRole().getName());
        userDto.setPhone(dbUser.getPhone());
        userDto.setAvatar(dbUser.getAvatar());

        response.setUser(userDto);
        return response;
    }
}
