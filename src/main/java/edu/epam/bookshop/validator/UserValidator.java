package edu.epam.bookshop.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Slf4j
@Component
public class UserValidator {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}");

    private static final Pattern USER_NAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9](_(?!(\\.|_))|\\.(?!(_|\\.))|[a-zA-Z0-9]){2,25}[a-zA-Z0-9]$");

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^@]+@[^@]+\\.[^@]+$");

    private static final int MAX_PASSWORD_LENGTH = 25;

    private static final String INVALID_USER_NAME_MSG = "Invalid username data!";
    private static final String INVALID_EMAIL_MSG = "Invalid email data!";
    private static final String INVALID_PASSWORD_MSG = "Invalid password data!";


    public boolean isUsernameValid(String username) {
        boolean userNameValid = USER_NAME_PATTERN.matcher(username).matches();
        if (!userNameValid) {
            log.info(INVALID_USER_NAME_MSG);
        }
        return userNameValid;
    }

    public boolean isEmailValid(String email) {
        boolean emailValid = EMAIL_PATTERN.matcher(email).matches();
        if (!emailValid) {
            log.info(INVALID_EMAIL_MSG);
        }
        return emailValid;
    }

    public boolean isPasswordValid(String password) {
        boolean passwordValid = PASSWORD_PATTERN.matcher(password).matches()
                && password.length() <= MAX_PASSWORD_LENGTH;
        if (!passwordValid) {
            log.info(INVALID_PASSWORD_MSG);
        }
        return passwordValid;
    }
}
