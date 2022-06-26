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
    public static final String UPDATE_PUBLISHER_INFO_URN = "/updatePublisherInfo";
    public static final String DELETE_PUBLISHER_BY_ID = "/deletePublisherById";
}
