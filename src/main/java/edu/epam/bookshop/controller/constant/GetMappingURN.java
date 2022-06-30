package edu.epam.bookshop.controller.constant;

public class GetMappingURN {

    private GetMappingURN() {

    }

    public static final String CHECK_IF_USER_NAME_TAKEN_URN = "/checkIfUsernameIsTaken";
    public static final String CHECK_IF_EMAIL_TAKEN_URN = "/checkIfEmailIsTaken";
    public static final String FIND_USERS_WITH_PAGINATION_URN = "/findUsersWithPagination";
    public static final String FIND_USERS_BY_KEYWORD = "/findUsersByKeyword";
    public static final String FIND_USER_BY_USERNAME = "/findUserByUsername";
    public static final String FIND_ALL_ROLES = "/findAllRoles";

    //author
    public static final String FIND_AUTHORS_BY_PAGE = "/findAuthorsByPage";
    public static final String FIND_AUTHORS_BY_KEYWORD = "/findAuthorsByKeyword";
    public static final String FIND_AUTHOR_INFO_BY_ID = "/findAuthorInfoById";

    //genre
    public static final String GENRE_EXISTS_BY_TITLE_URN = "/isGenreExistsByTitle";
    public static final String FIND_ALL_GENRES_URN = "/findAllGenres";
    public static final String FIND_GENRES_BY_PAGE_URN = "/findGenresByPage";
    public static final String FIND_GENRES_BY_KEYWORD_URN = "/findGenresByKeyword";

    //publisher
    public static final String PUBLISHER_EXISTS_BY_NAME_URN = "/isPublisherExistsByName";
    public static final String FIND_PUBLISHER_BY_NAME_URN = "/findPublisherByName";
    public static final String FIND_ALL_PUBLISHERS_URN = "/findAllPublishers";
    public static final String FIND_PUBLISHERS_BY_PAGE_URN = "/findPublishersByPage";
    public static final String FIND_PUBLISHERS_BY_KEYWORD_URN = "/findPublishersByKeyword";

    //pages
    public static final String REGISTRATION_URN = "/registration";
    public static final String LOGIN_URN = "/login";
    public static final String MAIN_URN = "/home";
    public static final String ADMIN_CABINET_URN = "/admin/cabinet";
    public static final String ALL_USERS_URN = "/admin/cabinet/all_users";
    public static final String ACCOUNT_SETTINGS_URN = "/user/account_settings";
    public static final String ALL_AUTHORS_URN = "/admin/cabinet/all_authors";
    public static final String EDIT_AUTHOR_URN = "/admin/cabinet/edit_author";

    //book
    public static final String FIND_BOOKS_BY_KEYWORD = "/findBooksByKeyword";
    public static final String FIND_BOOKS_BY_PAGE = "/findBooksByPage";
    public static final String FIND_BOOK_DETAILS = "/findBookDetails";

}
