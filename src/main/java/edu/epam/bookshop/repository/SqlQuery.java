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

    public static final String SELECT_USER_BY_REVIEW_ID =
            "SELECT u FROM BookReview br " +
                    "LEFT JOIN br.user u " +
                    "WHERE br.bookReviewId = :reviewId";

    //author
    public static final String INSERT_AUTHOR_TO_BOOK =
            "INSERT INTO author_books (book_id_fk, author_id_fk) " +
                    "VALUES (?, ?)";
    public static final String UPDATE_AUTHOR_INFO_BY_ID =
            "UPDATE Author a " +
                    "SET a.firstName = ?1, " +
                    "a.lastName = ?2, " +
                    "a.biography = ?3, " +
                    "a.birthDate = ?4, " +
                    "a.imagePath = ?5 " +
                    "WHERE a.authorId = ?6";
    public static final String DELETE_AUTHOR_FROM_BOOK =
            "DELETE FROM author_books " +
                    "WHERE author_id_fk = (?) AND book_id_fk = (?)";
    public static final String CHECK_IF_BOOK_EXISTS_FOR_AUTHOR =
            "SELECT IF(COUNT(author_id_fk) > 0, 'true', 'false') " +
                    "FROM author_books " +
                    "WHERE author_id_fk = (?) AND book_id_fk = (?)";
    public static final String SELECT_AUTHORS_BY_BOOK_ID =
            "SELECT a FROM Author a " +
                    "LEFT JOIN a.books b " +
                    "WHERE b.bookId = :bookId";

    //publisher
    public static final String INSERT_PUBLISHER_TO_BOOK =
            "INSERT INTO publisher_books (book_id_fk, publisher_id_fk) " +
                    "VALUES (?, ?)";
    public static final String UPDATE_PUBLISHER_INFO_BY_ID =
            "UPDATE Publisher p " +
                    "SET p.name = ?1, " +
                    "p.description = ?2, " +
                    "p.imagePath = ?3 " +
                    "WHERE p.publisherId = ?4";
    public static final String DELETE_PUBLISHER_FROM_BOOK =
            "DELETE FROM publisher_books " +
                    "WHERE book_id_fk = (?) AND publisher_id_fk = (?)";
    public static final String CHECK_IF_PUBLISHER_EXISTS_FOR_BOOK =
            "SELECT IF(COUNT(publisher_id_fk) > 0, 'true', 'false') " +
                    "FROM publisher_books " +
                    "WHERE book_id_fk = (?) AND publisher_id_fk = (?)";
    public static final String SELECT_PUBLISHERS_BY_BOOK_ID =
            "SELECT p FROM Publisher p " +
                    "LEFT JOIN p.publishedBooks b " +
                    "WHERE b.bookId = :bookId";

    //genre
    public static final String SELECT_LOCALIZED_GENRES_BY_BOOK_ID_AND_LOCALE =
            "SELECT COALESCE(lg1, lg2) FROM Genre g " +
                    "JOIN g.books b " +
                    "LEFT JOIN LocalizedGenre lg1 ON lg1.genre.genreId = g.genreId " +
                    "AND lg1.language.languageId = :languageId " +
                    "AND b.bookId = :bookId " +
                    "INNER JOIN LocalizedGenre lg2 ON lg2.genre.genreId = g.genreId " +
                    "AND lg2.language.languageId = 1 " +
                    "AND b.bookId = :bookId";
    public static final String SELECT_LOCALIZED_GENRE_BY_GENRE_ID_AND_LANGUAGE_ID =
            "SELECT DISTINCT COALESCE(lg1, lg2) FROM Genre g " +
                    "LEFT JOIN LocalizedGenre lg1 ON lg1.genre.genreId = g.genreId " +
                    "AND lg1.language.languageId = :languageId " +
                    "AND g.genreId = :genreId " +
                    "INNER JOIN LocalizedGenre lg2 ON lg2.genre.genreId = g.genreId " +
                    "AND lg2.language.languageId = 1 " +
                    "AND g.genreId = :genreId";
    public static final String SELECT_DISTINCT_GENRES_FOR_AUTHOR =
            "SELECT DISTINCT g FROM Genre g";
    public static final String SELECT_LOCALIZED_GENRES_BY_LANGUAGE =
            "SELECT lg FROM LocalizedGenre lg " +
                    "JOIN lg.language l " +
                    "WHERE l.name = :language";
    public static final String SELECT_LOCALIZED_GENRES_BY_LANGUAGE_ID =
            "SELECT COALESCE(lg1, lg2) FROM Genre g " +
                    "LEFT JOIN LocalizedGenre lg1 ON lg1.genre.genreId = g.genreId " +
                    "AND lg1.language.languageId = :languageId " +
                    "INNER JOIN LocalizedGenre lg2 ON lg2.genre.genreId = g.genreId " +
                    "AND lg2.language.languageId = 1";
    public static final String UPDATE_LOCALIZED_GENRE_BY_GENRE_ID_AND_LANGUAGE =
            "UPDATE LocalizedGenre lg " +
                    "SET lg.title = :newGenreTitle " +
                    "WHERE lg.genre.genreId = :genreId " +
                    "AND lg.language.languageId = :languageId";
    public static final String SELECT_LOCALIZED_GENRES_BY_KEYWORD_AND_LANGUAGE =
            "SELECT lg FROM LocalizedGenre lg " +
                    "JOIN lg.language l " +
                    "WHERE l.name = :languageName " +
                    "AND lg.title LIKE '%'||:keyword||'%' ";
    public static final String CHECK_IF_LOCALIZED_GENRE_EXISTS_BY_TITLE_AND_LANGUAGE =
            "SELECT CASE WHEN (COUNT(lg.localizedGenreId) > 0) THEN true ELSE false END " +
                    "FROM LocalizedGenre lg " +
                    "JOIN lg.language l " +
                    "WHERE lg.title = :title " +
                    "AND l = :language";

    //book
    public static final String UPDATE_BOOK_INFO =
            "UPDATE Book b " +
                    "SET b.price = :#{#updatedBook.price}, " +
                    "b.pages = :#{#updatedBook.pages}, " +
                    "b.isbn = :#{#updatedBook.isbn}, " +
                    "b.coverType = :#{#updatedBook.coverType}, " +
                    "b.publishDate = :#{#updatedBook.publishDate} " +
                    "WHERE b.bookId = :#{#updatedBook.bookId}";
    public static final String SELECT_BOOKS_BY_KEYWORD =
            "SELECT b FROM Book b " +
                    "WHERE b.title LIKE %:keyWord%";
    public static final String SELECT_BOOKS_BY_LOCALIZED_GENRE_TITLE =
            "SELECT b FROM Book b " +
                    "JOIN b.genres g " +
                    "JOIN g.localizedGenres lg " +
                    "WHERE lg.title = :genreTitle";
    public static final String SELECT_BOOKS_BY_YEAR =
            "SELECT b FROM Book b " +
                    "WHERE YEAR(b.publishDate) = :year";
    public static final String SELECT_EXISTING_YEARS_FOR_BOOKS =
            "SELECT DISTINCT YEAR(b.publishDate) FROM Book b " +
                    "ORDER BY YEAR(b.publishDate) ASC";
    public static final String COUNT_BOOKS_BY_KEYWORD =
            "SELECT COUNT(b.bookId) FROM Book b " +
                    "WHERE b.title LIKE %:keyWord%";
    public static final String COUNT_BOOKS_BY_GENRE_TITLE =
            "SELECT COUNT(g.title) FROM Book b " +
                    "LEFT JOIN b.genres g " +
                    "WHERE g.title = :genreTitle";
    public static final String COUNT_BOOKS_BY_YEAR =
            "SELECT COUNT(b.bookId) FROM Book b " +
                    "WHERE YEAR(b.publishDate) = :year";
    public static final String SELECT_BOOK_BY_LOCALIZED_BOOK_TITLE =
            "SELECT b FROM Book b " +
                    "JOIN b.localizedBooks lb " +
                    "WHERE lb.title = :title";
    public static final String SELECT_BOOKS_WITH_SCORE_GREATER_THAN =
            "SELECT b FROM Book b " +
                    "JOIN b.bookReviews br " +
                    "GROUP BY b.bookId " +
                    "HAVING AVG(br.score) > :score ORDER BY AVG(br.score) DESC";

    //localized_book
    public static final String SELECT_LOCALIZED_BOOK_BY_BOOK_ID_AND_LANGUAGE_ID =
            "SELECT COALESCE(lb1, lb2) " +
                    "FROM Book b, Genre g " +
                    "LEFT JOIN LocalizedBook lb1 ON lb1.book.bookId = b.bookId " +
                    "AND lb1.language.languageId = :languageId " +
                    "INNER JOIN LocalizedBook lb2 ON lb2.book.bookId = b.bookId " +
                    "AND lb2.language.languageId = 1 " +
                    "WHERE b.bookId = :bookId ";
    public static final String SELECT_LOCALIZED_BOOKS_BY_KEYWORD_AND_LANGUAGE_ID =
            "SELECT COALESCE(lb1, lb2) " +
                    "FROM Book b " +
                    "LEFT JOIN LocalizedBook lb1 ON lb1.book.bookId = b.bookId " +
                    "AND lb1.language.languageId = :languageId " +
                    "INNER JOIN LocalizedBook lb2 ON lb2.book.bookId = b.bookId " +
                    "AND lb2.language.languageId = 1 " +
                    "WHERE lb1.title LIKE %:keyword% OR lb2.title LIKE %:keyword%";
    public static final String SELECT_ALL_LOCALIZED_BOOKS_BY_LANGUAGE_ID_AND_PAGE =
            "SELECT COALESCE(lb1, lb2) " +
                    "FROM Book b " +
                    "LEFT JOIN LocalizedBook lb1 ON lb1.book.bookId = b.bookId " +
                    "AND lb1.language.languageId = :languageId " +
                    "INNER JOIN LocalizedBook lb2 ON lb2.book.bookId = b.bookId " +
                    "AND lb2.language.languageId = 1";
    public static final String UPDATE_LOCALIZED_BOOK_INFO =
            "UPDATE LocalizedBook lb " +
                    "SET lb.title = :#{#updatedLocalizedBook.title}, " +
                    "lb.imagePath = :#{#updatedLocalizedBook.imagePath}, " +
                    "lb.description = :#{#updatedLocalizedBook.description} " +
                    "WHERE lb.localizedBookId = :#{#updatedLocalizedBook.localizedBookId}";
    public static final String SELECT_LOCALIZED_BOOKS_BY_AVG_SCORE_GREATER_THAN =
            "SELECT COALESCE(lb1, lb2) " +
                    "FROM Book b " +
                    "LEFT JOIN LocalizedBook lb1 ON lb1.book.bookId = b.bookId " +
                    "AND lb1.language.languageId = :languageId " +
                    "INNER JOIN LocalizedBook lb2 ON lb2.book.bookId = b.bookId " +
                    "AND lb2.language.languageId = 1 " +
                    "JOIN b.bookReviews br " +
                    "GROUP BY b.bookId " +
                    "HAVING AVG(br.score) > :score ORDER BY AVG(br.score) DESC";
    public static final String SELECT_LOCALIZED_BOOKS_BY_YEAR =
            "SELECT COALESCE(lb1, lb2) " +
                    "FROM Book b " +
                    "LEFT JOIN LocalizedBook lb1 ON lb1.book.bookId = b.bookId " +
                    "AND lb1.language.languageId = :languageId " +
                    "INNER JOIN LocalizedBook lb2 ON lb2.book.bookId = b.bookId " +
                    "AND lb2.language.languageId = 1 " +
                    "WHERE YEAR(b.publishDate) = :bookYear";

    //book review
    public static final String CHECK_IF_USER_REVIEWED_GIVEN_BOOK =
            "SELECT CASE WHEN COUNT(br.bookReviewId) > 0 " +
                    "THEN 'true' ELSE 'false' END " +
                    "FROM BookReview br " +
                    "LEFT JOIN br.reviewedBook rb " +
                    "LEFT JOIN br.user u " +
                    "WHERE rb.bookId = :bookId AND u.userId = :userId";
    public static final String UPDATE_REVIEW_TEXT_AND_SCORE_BY_REVIEW_ID =
            "UPDATE BookReview br " +
                    "SET br.reviewText = :updatedText, " +
                    "br.score = :updatedScore " +
                    "WHERE br.bookReviewId = :reviewId";
    public static final String SELECT_REVIEWS_BY_BOOK_ID =
            "SELECT br FROM BookReview br " +
                    "LEFT JOIN br.reviewedBook rb " +
                    "WHERE rb.bookId = :bookId";
    public static final String COUNT_REVIEWS_BY_BOOK_ID =
            "SELECT COUNT(br.bookReviewId) FROM BookReview br " +
                    "LEFT JOIN br.reviewedBook rb " +
                    "WHERE rb.bookId = :bookId";
    public static final String SELECT_AVERAGE_SCORE_BY_BOOK_ID =
            "SELECT AVG(br.score) FROM BookReview br " +
                    "LEFT JOIN br.reviewedBook rb " +
                    "WHERE rb.bookId = :bookId";
    public static final String SELECT_BOOKS_COUNT_WITH_AVG_SCORE_GREATER_THAN =
            "SELECT COUNT(*) AS counter " +
                    "FROM (SELECT br.book_id_fk AS bookId " +
                    "FROM book_reviews br " +
                    "GROUP BY br.book_id_fk " +
                    "HAVING AVG(br.score) > (?)) as averageScore";
    public static final String SELECT_BOOK_SCORE_FOR_USER =
            "SELECT br.score FROM BookReview br " +
                    "JOIN br.user u " +
                    "JOIN br.reviewedBook rb " +
                    "WHERE u.userId = :userId " +
                    "AND rb.bookId = :bookId";

    //role
    public static final String SELECT_ROLES_BY_USERNAME =
            "SELECT r FROM Role r " +
                    "JOIN r.users u " +
                    "WHERE u.userName = :userName";

    //shelve
    public static final String INSERT_BOOK_TO_SHELVE =
            "INSERT INTO shelve_books (shelve_id_fk, book_id_fk, book_status) " +
                    "VALUES(?, ?, ?)";
    public static final String DELETE_BOOK_FROM_SHELVE =
            "DELETE FROM shelve_books " +
                    "WHERE shelve_id_fk = (?) " +
                    "AND book_id_fk = (?)";
    public static final String CHECK_IF_SHELVE_EXISTS_BY_ID =
            "SELECT CASE WHEN COUNT(bs.bookShelveId) > 0 " +
                    "THEN 'true' ELSE 'false' END " +
                    "FROM BookShelve bs " +
                    "WHERE bs.bookShelveId = :shelveId";
    public static final String CHECK_IF_BOOK_EXISTS_IN_BOOK_SHELVE =
            "SELECT CASE WHEN COUNT(bs.bookShelveId) > 0 " +
                    "THEN 'true' ELSE 'false' END " +
                    "FROM BookShelve bs " +
                    "LEFT JOIN bs.shelveBooks sb " +
                    "LEFT JOIN sb.book b " +
                    "WHERE bs.bookShelveId = :shelveId AND b.bookId = :bookId";
    public static final String COUNT_BOOKS_ON_SHELVE_BY_SHELVE_ID_AND_BOOK_STATUS =
            "SELECT COUNT(sb.book) FROM ShelveBook sb " +
                    "JOIN sb.bookShelve bs " +
                    "WHERE bs.bookShelveId = :shelveId " +
                    "AND sb.bookStatus = :bookStatus";
    public static final String UPDATE_BOOK_STATUS_ON_SHELVE =
            "UPDATE shelve_books sb SET sb.book_status = (?) " +
                    "WHERE sb.shelve_id_fk = (?) " +
                    "AND sb.book_id_fk = (?)";
    public static final String SELECT_BOOK_STATUS_ON_SHELVE =
            "SELECT sb.bookStatus FROM ShelveBook sb " +
                    "JOIN sb.bookShelve bs " +
                    "JOIN sb.book b " +
                    "WHERE bs.bookShelveId = :shelveId " +
                    "AND b.bookId = :bookId";

    public static final String SELECT =
            "SELECT DISTINCT sb FROM ShelveBook sb " +
                    "JOIN sb.bookShelve bs " +
                    "WHERE bs.bookShelveId = :shelveId " +
                    "AND sb.bookStatus = :bookStatus";

    //language
    public static final String SELECT_LANGUAGE_BY_LOCALIZED_BOOK_TITLE =
            "SELECT l FROM LocalizedBook lb " +
                    "JOIN lb.language l " +
                    "WHERE lb.title = :title";
}
