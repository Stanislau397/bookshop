package edu.epam.bookshop.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@Slf4j
public class AuthorValidator {

    private static final Pattern FIRST_NAME_PATTERN =
            Pattern.compile("(?i)[a-z]([ ',.a-z]{0,30}[a-z])?");

    private static final Pattern LAST_NAME_PATTERN =
            Pattern.compile("(?i)[a-z]([ ',.a-z]{0,30}[a-z])?");

    private static final String FIRST_NAME_IS_INVALID_MSG = "Firstname is invalid!";
    private static final String LAST_NAME_IS_INVALID = "Lastname is invalid!";

    public boolean isFirstnameValid(String firstname) {
        boolean firstNameValid = firstname.matches(FIRST_NAME_PATTERN.pattern());
        if (!firstNameValid) {
            log.info(FIRST_NAME_IS_INVALID_MSG);
        }
        return firstNameValid;
    }

    public boolean isLastnameValid(String lastname) {
        boolean lastnameValid = lastname.matches(LAST_NAME_PATTERN.pattern());
        if (!lastnameValid) {
            log.info(LAST_NAME_IS_INVALID);
        }
        return lastnameValid;
    }
}
