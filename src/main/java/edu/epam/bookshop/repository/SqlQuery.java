package edu.epam.bookshop.repository;

public class SqlQuery {

    private SqlQuery() {

    }

    //user
    public static final String UPDATE_USER_STATUS_BY_USERNAME =
            "UPDATE User u " +
                    "SET u.locked = ?1 " +
                    "WHERE u.userName = ?2";

    public static final String UPDATE_USER_AVATAR_BY_USERNAME =
            "UPDATE User u " +
                    "SET u.avatarName = ?1 " +
                    "WHERE u.userName = ?2";

    public static final String UPDATE_USER_ROLES_BY_USERNAME =
            "UPDATE users_roles " +
                    "SET role_id_fk = ?2 " +
                    "WHERE user_id_fk = ?1";

    public static final String UPDATE_USER_PASSWORD_BY_USERNAME =
            "UPDATE User u " +
                    "SET u.password = ?1 " +
                    "WHERE u.userName = ?2";

    //author
    public static final String UPDATE_AUTHOR_INFO_BY_ID =
            "UPDATE Author a " +
                    "SET a.firstName = ?1, " +
                    "a.lastName = ?2, " +
                    "a.biography = ?3, " +
                    "a.birthDate = ?4, " +
                    "a.imagePath = ?5 " +
                    "WHERE a.authorId = ?6";
    public static final String CHECK_IF_BOOK_EXISTS_FOR_AUTHOR =
            "SELECT CASE WHEN COUNT(author_id_fk) > 0 " +
                    "THEN TRUE ELSE FALSE END " +
                    "FROM author_books " +
                    "WHERE author_id_fk = 1? AND book_id_fk = 2?";

    //publisher
    public static final String UPDATE_PUBLISHER_INFO_BY_ID =
            "UPDATE Publisher p " +
                    "SET p.name = ?1, " +
                    "p.description = ?2, " +
                    "p.imagePath = ?3 " +
                    "WHERE p.publisherId = ?4";

    //genre
    public static final String UPDATE_GENRE_TITLE_BY_ID =
            "UPDATE Genre g " +
                    "SET g.title = ?1 " +
                    "WHERE g.genreId = ?2";
    public static final String INSERT_GENRE_TO_BOOK =
            "INSERT INTO book_genres " +
                    "VALUES (genre_id_fk, book_id_fk) " +
                    "WHERE genre_id_fk = 1? AND book_id_fk = 2?";
    public static final String DELETE_GENRE_FROM_BOOK =
            "DELETE FROM book_genres " +
                    "WHERE genre_id_fk = 1? AND book_id_fk = 2?";
    public static final String CHECK_IF_GENRE_EXISTS_FOR_BOOK =
            "SELECT CASE WHEN COUNT(genre_id_fk) > 0 " +
                    "THEN TRUE ELSE FALSE END " +
                    "FROM book_genres " +
                    "WHERE genre_id_fk = 1? AND book_id_fk = 2?";

    //book
    public static final String INSERT_BOOK_TO_AUTHOR =
            "INSERT INTO author_books (book_id_fk, author_id_fk) " +
                    "VALUES (1?,2?)";
    public static final String DELETE_BOOK_FROM_AUTHOR =
            "DELETE FROM author_books " +
                    "WHERE author_id_fk = 1? AND book_id_fk = 2?";
    public static final String INSERT_AUTHOR_TO_BOOK =
            "INSERT INTO author_books (author_id_fk, book_id_fk) " +
                    "VALUES (1?,2?)";
    public static final String DELETE_AUTHOR_FROM_BOOK =
            "DELETE FROM author_books " +
                    "WHERE author_id_fk = 1?, book_id_fk = 2?";

    //book review
    public static final String CHECK_IF_USER_REVIEWED_GIVEN_BOOK =
            "SELECT CASE WHEN COUNT(book_id_fk) > 0 " +
                    "THEN TRUE ELSE FALSE END " +
                    "FROM book_reviews " +
                    "WHERE book_id_fk = 1? AND user_id_fk = 2?";
}
