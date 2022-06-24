package edu.epam.bookshop.controller.constant;

public class PostMappingURN {

    private PostMappingURN() {

    }

    public static final String REGISTER_USER_URN = "/register_user";
    public static final String UPDATE_USER_STATUS_URN = "/updateUserStatusByUsername";
    public static final String UPDATE_USER_AVATAR_URN = "/updateUserAvatarByUsername";
    public static final String UPDATE_USER_PASSWORD_URN = "/updateUserPasswordByUsername";
    public static final String UPDATE_USER_ROLE_URN = "/updateUserRoleByUserId";

    public static final String ADD_AUTHOR_URN = "/addAuthor";
    public static final String UPDATE_AUTHOR_INFO_URN = "/updateAuthorInfo";
    public static final String DELETE_AUTHOR_URN = "/deleteAuthorById";
}
