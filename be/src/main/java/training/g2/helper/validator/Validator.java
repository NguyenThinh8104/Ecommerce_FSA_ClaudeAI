package training.g2.helper.validator;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import training.g2.exception.common.BusinessException;
import static training.g2.constant.Constants.Regex.*;
import static training.g2.constant.Constants.UserExceptionInformation.*;
import static training.g2.constant.Constants.Message.*;

public class Validator {
    public static void validateRegex(String value, String regex, String errorMessage) {
        if (!value.matches(regex)) {
            throw BusinessException.of(HttpStatus.BAD_REQUEST, errorMessage);
        }
    }

    public static void isNullOrEmpty(String value, String errorMessage) {
        if (!StringUtils.hasText(value)) {
            throw BusinessException.of(HttpStatus.BAD_REQUEST, errorMessage);
        }
    }

    public static boolean isPasswordValid(String value) {
        return value.matches(REGEX_PASSWORD);
    }

    public static void validateUserCreation(String fullName, String email, String password, String phone,
            String gender) {
        validateUserFields(fullName, email, password, phone, gender);
    }

    private static void validateUserFields(String fullName, String email, String password, String phone,
            String gender) {
        isNullOrEmpty(fullName, EMPTY_FULL_NAME);
        validateRegex(fullName, REGEX_FULLNAME, FULL_NAME_INVALID);

        isNullOrEmpty(password, EMPTY_PASSWORD);
        validateRegex(password, REGEX_PASSWORD, PASSWORD_INVALID);

        isNullOrEmpty(email, EMPTY_EMAIL);
        validateRegex(email, REGEX_EMAIL, EMAIL_INVALID);

        isNullOrEmpty(phone, EMPTY_PHONE_NUMBER);
        validateRegex(phone, REGEX_PHONE, PHONE_INVALID);

        if (!"MALE".equals(gender) && !"FEMALE".equals(gender)) {
            throw BusinessException.of(GENDER_INVALID);
        }
    }

}
