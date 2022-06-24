package edu.epam.bookshop.constant;

import java.util.ResourceBundle;

public class ExceptionMessage {

    private ExceptionMessage() {

    }

    public static final String BUNDLE_NAME = "messages";

    public static final String USER_NAME_TAKEN = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("username_taken_error_msg");

    public static final String EMAIL_TAKEN = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("email_taken_error_msg");

    public static final String INVALID_USER_NAME_MSG = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("username_regex_error_msg");

    public static final String INVALID_EMAIL_MSG = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("email_regex_error_msg");

    public static final String INVALID_PASSWORD_MSG = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("password_regex_error_msg");

    public static final String USER_WITH_GIVEN_NAME_NOT_FOUND_MSG = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("user_with_this_name_not_found");

    public static final String NOTHING_WAS_FOUND_MSG = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("nothing_was_found");

    public static final String FILE_IS_EMPTY_MSG = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("file_is_empty");

    public static final String IMAGE_IS_NOT_VALID_MSG = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("image_is_not_valid");

    public static final String CURRENT_PASSWORD_IS_INVALID = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("current_password_is_invalid");

    public static final String ROLE_WITH_GIVEN_NAME_DOESNT_EXIST = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("role_does_not_exist");

    public static final String ROLE_WITH_GIVEN_ID_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("role_with_given_id_not_found");

    public static final String USER_WITH_GIVEN_ID_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("user_with_this_id_not_found");

    public static final String FIRST_NAME_IS_NOT_VALID_MSG = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("first_name_is_invalid");

    public static final String LAST_NAME_IS_NOT_VALID_MSG = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("last_name_is_invalid");

    public static final String AUTHOR_WITH_GIVEN_ID_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("author_with_given_id_not_found");

    public static final String AUTHOR_WITH_GIVEN_KEYWORD_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("author_with_given_keyword_not_found");

    //publisher
    public static final String PUBLISHER_WITH_GIVEN_NAME_ALREADY_EXISTS = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("publisher_with_given_name_already_exists");

    public static final String PUBLISHER_WITH_GIVEN_NAME_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("publisher_with_given_name_not_found");

    public static final String PUBLISHER_WITH_GIVEN_ID_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("publisher_with_given_id_not_found");

    public static final String PUBLISHER_NAME_IS_INVALID = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("publisher_name_is_invalid");

    public static final String PUBLISHER_DESCRIPTION_IS_INVALID = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("publisher_description_is_invalid");
}
