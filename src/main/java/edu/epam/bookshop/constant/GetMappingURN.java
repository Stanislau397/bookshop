package edu.epam.bookshop.constant;

public class GetMappingURN {

    private GetMappingURN() {

    }

    public static final String CHECK_IF_USER_NAME_TAKEN_URN = "/checkIfUsernameIsTaken";
    public static final String CHECK_IF_EMAIL_TAKEN_URN = "/checkIfEmailIsTaken";
    public static final String FIND_USERS_WITH_PAGINATION_URN = "/findUsersWithPagination";
    public static final String FIND_USERS_BY_KEYWORD = "/findUsersByKeyword";
    public static final String FIND_USER_BY_USERNAME = "/findUserByUsername";
    public static final String FIND_USER_BY_REVIEW_ID = "/findUserByReviewId";
    public static final String FIND_ALL_ROLES = "/findAllRoles";
    public static final String FIND_USER_ROLES_URN = "/findUserRoles";

    //author
    public static final String FIND_AUTHORS_BY_PAGE_URN = "/findAuthorsByPage";
    public static final String FIND_AUTHORS_BY_KEYWORD_URN = "/findAuthorsByKeyword";
    public static final String FIND_AUTHOR_INFO_BY_ID_URN = "/findAuthorInfoById";
    public static final String FIND_AUTHORS_BY_BOOK_ID_URN = "/findAuthorsByBookId";
    public static final String FIND_ALL_AUTHORS_URN = "/findAllAuthors";

    //genre
    public static final String GENRE_EXISTS_BY_TITLE_URN = "/isGenreExistsByTitle";
    public static final String FIND_ALL_GENRES_URN = "/findAllGenres";
    public static final String FIND_GENRES_BY_PAGE_URN = "/findGenresByPage";
    public static final String FIND_GENRES_BY_KEYWORD_URN = "/findGenresByKeyword";
    public static final String FIND_GENRES_BY_BOOK_ID_URN = "/findGenresByBookId";
    public static final String FIND_GENRES_FOR_AUTHOR_URN = "/findGenresForAuthor";
    public static final String FIND_LOCALIZED_GENRES_URN = "/findLocalizedGenres";

    //publisher
    public static final String PUBLISHER_EXISTS_BY_NAME_URN = "/isPublisherExistsByName";
    public static final String FIND_PUBLISHER_BY_NAME_URN = "/findPublisherByName";
    public static final String FIND_ALL_PUBLISHERS_URN = "/findAllPublishers";
    public static final String FIND_PUBLISHERS_BY_PAGE_URN = "/findPublishersByPage";
    public static final String FIND_PUBLISHERS_BY_KEYWORD_URN = "/findPublishersByKeyword";
    public static final String FIND_PUBLISHERS_BY_BOOK_ID_URN = "/findPublishersByBookId";

    //pages
    public static final String REGISTRATION_URN = "/bookshop/registration";
    public static final String LOGIN_URN = "/bookshop/login";
    public static final String MAIN_URN = "/bookshop/home";
    public static final String ADMIN_CABINET_URN = "/admin/cabinet";
    public static final String ALL_USERS_URN = "/admin/cabinet/all_users";
    public static final String ACCOUNT_SETTINGS_URN = "/user/account_settings";
    public static final String ALL_AUTHORS_URN = "/admin/cabinet/all_authors";
    public static final String EDIT_AUTHOR_URN = "/admin/cabinet/edit_author";
    public static final String EDIT_BOOK_URN = "/admin/cabinet/edit_book";
    public static final String ALL_GENRES_URN = "/admin/cabinet/all_genres";
    public static final String ALL_PUBLISHERS_URN = "/admin/cabinet/all_publishers";
    public static final String ALL_BOOKS_URN = "/admin/cabinet/all_books";
    public static final String AUTHOR_DETAILS_URN = "/bookshop/author";
    public static final String BOOK_DETAILS_URN = "/bookshop/book";
    public static final String BOOKS_BY_GENRE_TITLE_URN = "/bookshop/booksByGenre";
    public static final String BOOKS_BY_YEAR_URN = "/bookshop/booksByYear";
    public static final String BOOKS_BY_KEYWORD_PAGE_URN = "/bookshop/booksByKeyWord";
    public static final String BOOKS_WITH_HIGH_SCORE_PAGE_URN = "/bookshop/booksWithHighScore";
    public static final String USER_PROFILE_PAGE_URN = "/user/profile";

    //book
    public static final String FIND_BOOKS_BY_KEYWORD = "/findBooksByKeyword";
    public static final String FIND_BOOKS_BY_PAGE = "/findBooksByPage";
    public static final String FIND_BOOK_DETAILS = "/findBookDetails";
    public static final String FIND_BOOKS_BY_GENRE_TITLE_AND_PAGE_URN = "/findBooksByGenreTitleAndPageNumber";
    public static final String FIND_BOOKS_BY_YEAR_AND_PAGE_URN = "/findBooksByYearAndPageNumber";
    public static final String FIND_EXISTING_YEARS_IN_BOOKS_URN = "/findExistingYearsInBooks";
    public static final String FIND_BOOKS_BY_KEYWORD_AND_PAGE = "/findBooksByKeyWordAndPage";
    public static final String FIND_BOOKS_WITH_HIGH_SCORE_LIMIT_15 = "/findBooksWithHighScoreLimit15";
    public static final String FIND_BOOKS_BY_PAGE_HAVING_AVG_SCORE_GREATER_THAN = "/findBooksByPageHavingAvgScoreGreaterThan";
    public static final String COUNT_BOOKS_WITH_AVG_SCORE_GREATER_THAN = "/countBooksWithScoreGreaterThan";
    public static final String FIND_BOOKS_BY_SHELVE_ID_AND_BOOK_STATUS = "/findBooksByShelveIdAndBookStatus";
    public static final String CHECK_IF_BOOK_EXISTS_IN_SHELVE = "/isBookExistsInShelve";
    public static final String FIND_NUMBER_OF_BOOKS_ON_SHELVE = "/findNumberOfBooksOnShelve";
    public static final String FIND_BOOK_STATUS_ON_SHELVE = "/findBookStatusOnShelveByShelveIdAndBookId";

    //reviews
    public static final String FIND_BOOK_REVIEWS_BY_BOOK_ID_AND_PAGE_URN = "/findBookReviewsByBookIdAndPage";
    public static final String FIND_AVERAGE_BOOK_REVIEW_SCORE_URN = "/findAverageBookReviewScore";
    public static final String CHECK_IF_USER_REVIEWED_GIVEN_BOOK = "/isUserReviewedGivenBook";
    public static final String FIND_BOOK_SCORE_FOR_USER_URN = "/findBookScoreForUser";
}
