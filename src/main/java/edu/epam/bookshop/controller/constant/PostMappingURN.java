package edu.epam.bookshop.controller.constant;

public class PostMappingURN {

    private PostMappingURN() {

    }

    public static final String REGISTER_USER_URN = "/register_user";
    public static final String UPDATE_USER_STATUS_URN = "/updateUserStatusByUsername";
    public static final String UPDATE_USER_AVATAR_URN = "/updateUserAvatarByUsername";
    public static final String UPDATE_USER_PASSWORD_URN = "/updateUserPasswordByUsername";
    public static final String UPDATE_USER_ROLE_URN = "/updateUserRoleByUserId";

    //author
    public static final String ADD_AUTHOR_URN = "/addAuthor";
    public static final String UPDATE_AUTHOR_INFO_URN = "/updateAuthorInfo";
    public static final String DELETE_AUTHOR_URN = "/deleteAuthorById";

    //genre
    public static final String ADD_GENRE_URN = "/addGenre";
    public static final String UPDATE_GENRE_TITLE_URN = "/updateGenreTitle";
    public static final String DELETE_GENRE_BY_ID_URN = "/deleteGenreById";

    //publisher
    public static final String ADD_PUBLISHER_URN = "/addPublisher";
    public static final String ADD_PUBLISHER_TO_BOOK_URN = "/addPublisherToBook";
    public static final String DELETE_PUBLISHER_FROM_BOOK_URN = "/deletePublisherFromBook";
    public static final String UPDATE_PUBLISHER_INFO_URN = "/updatePublisherInfo";
    public static final String DELETE_PUBLISHER_BY_ID = "/deletePublisherById";

    //book
    public static final String ADD_BOOK_URN = "/addBook";
    public static final String UPDATE_BOOK_INFO_URN = "/updateBookInfo";
    public static final String ADD_BOOK_TO_AUTHOR_URN = "/addBookToAuthor";
    public static final String REMOVE_BOOK_FROM_AUTHOR_URN = "/removeBookFromAuthor";
    public static final String ADD_GENRE_TO_BOOK_URN = "/addGenreToBook";
    public static final String REMOVE_GENRE_FROM_BOOK_URN = "/removeGenreFromBook";
    public static final String ADD_AUTHOR_TO_BOOK_URN = "/addAuthorToBook";
    public static final String DELETE_AUTHOR_FROM_BOOK = "/removeAuthorFromBook";

    //review
    public static final String ADD_REVIEW_TO_BOOK_URN = "/addReviewToBook";
}
