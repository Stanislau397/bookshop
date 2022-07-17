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

    //author
    public static final String FIRST_NAME_IS_NOT_VALID_MSG = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("first_name_is_invalid");

    public static final String LAST_NAME_IS_NOT_VALID_MSG = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("last_name_is_invalid");

    public static final String AUTHOR_WITH_GIVEN_ID_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("author_with_given_id_not_found");

    public static final String AUTHOR_WITH_GIVEN_KEYWORD_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("author_with_given_keyword_not_found");

    public static final String AUTHOR_ALREADY_EXISTS_IN_GIVEN_BOOK = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("author_already_exists_in_given_book");

    public static final String AUTHORS_BY_GIVEN_BOOK_ID_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("author_with_given_book_id_not_found");

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

    public static final String PUBLISHERS_BY_GIVEN_BOOK_ID_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("publishers_by_book_id_not_found");

    public static final String PUBLISHER_ALREADY_EXISTS_FOR_GIVEN_BOOK = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("publisher_exists_for_book");

    public static final String PUBLISHER_DOES_NOT_EXIST_FOR_BOOK = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("publisher_not_exist_for_book");

    //genre
    public static final String GENRE_WITH_GIVEN_ID_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("genre_with_given_id_not_found");

    public static final String GENRE_WITH_GIVEN_TITLE_EXISTS = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("genre_with_given_title_exists");

    public static final String GENRE_TITLE_IS_NOT_VALID = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("genre_title_is_invalid");

    public static final String GENRES_WITH_GIVEN_KEYWORD_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("genre_with_given_keyword_not_found");

    public static final String GENRE_ALREADY_EXISTS_FOR_GIVEN_BOOK = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("genre_already_exists_for_book");

    public static final String GENRE_NOT_FOUND_FOR_GIVEN_BOOK = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("genre_not_found_for_book");

    public static final String GENRES_BY_GIVEN_BOOK_ID_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("genres_by_given_book_id_not_found");

    //book
    public static final String BOOK_TITLE_IS_NOT_VALID = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("book_title_is_invalid");

    public static final String BOOK_WITH_GIVEN_TITLE_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("book_with_given_title_not_found");

    public static final String BOOKS_WITH_GIVEN_KEYWORD_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("books_with_given_keyword_not_found");

    public static final String BOOK_WITH_GIVEN_ID_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("book_with_given_id_not_found");

    public static final String BOOK_DESCRIPTION_IS_NOT_VALID = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("book_description_is_invalid");

    public static final String BOOK_PRICE_IS_NOT_VALID = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("book_price_is_invalid");

    public static final String BOOK_PAGES_FIELD_IS_NOT_VALID = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("book_pages_is_invalid");

    public static final String BOOK_ISBN_IS_NOT_VALID = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("bool_isbn_is_invalid");

    public static final String BOOK_DOES_NOT_EXIST_FOR_AUTHOR = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("book_does_not_exist_for_author");

    public static final String BOOKS_WITH_GIVEN_GENRE_TITLE_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("books_by_given_genre_title_not_found");

    public static final String BOOKS_BY_GIVEN_YEAR_NOT_FOUND = ResourceBundle.getBundle(BUNDLE_NAME)
            .getString("books_by_given_year_not_found");
}
