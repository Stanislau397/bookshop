package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Book;
import edu.epam.bookshop.entity.CoverType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static edu.epam.bookshop.repository.SqlQuery.CHECK_IF_BOOK_EXISTS_FOR_AUTHOR;
import static edu.epam.bookshop.repository.SqlQuery.CHECK_IF_GENRE_EXISTS_FOR_BOOK;
import static edu.epam.bookshop.repository.SqlQuery.DELETE_BOOK_FROM_AUTHOR;
import static edu.epam.bookshop.repository.SqlQuery.DELETE_GENRE_FROM_BOOK;
import static edu.epam.bookshop.repository.SqlQuery.INSERT_AUTHOR_TO_BOOK;
import static edu.epam.bookshop.repository.SqlQuery.INSERT_GENRE_TO_BOOK;
import static edu.epam.bookshop.repository.SqlQuery.UPDATE_BOOK_INFO_BY_ID;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying
    @Transactional
    @Query(value = UPDATE_BOOK_INFO_BY_ID)
    void updateInfoById(String title, BigDecimal price, String description,
                        Integer pages, String isbn, String imagePath,
                        CoverType coverType, LocalDate publishDate, Long bookId);

    @Modifying
    @Transactional
    @Query(
            value = DELETE_BOOK_FROM_AUTHOR,
            nativeQuery = true)
    void deleteBookFromAuthorByAuthorIdAndBookId(Long authorId, Long bookId);

    @Query(
            value = CHECK_IF_BOOK_EXISTS_FOR_AUTHOR,
            nativeQuery = true)
    Boolean bookExistsForAuthor(Long authorId, Long bookId);

    @Transactional
    @Modifying
    @Query(
            value = INSERT_GENRE_TO_BOOK,
            nativeQuery = true
    )
    void insertGenreToBookByGenreIdAndBookId(Long genreId, Long bookId);

    @Transactional
    @Modifying
    @Query(
            value = DELETE_GENRE_FROM_BOOK,
            nativeQuery = true
    )
    void deleteGenreFromBookByGenreIdAndBookId(Long genreId, Long bookId);

    @Transactional
    @Modifying
    @Query(
            value = CHECK_IF_GENRE_EXISTS_FOR_BOOK,
            nativeQuery = true
    )
    boolean genreExistsForBook(Long genreId, Long bookId);

    @Transactional
    @Modifying
    @Query(
            value = INSERT_AUTHOR_TO_BOOK,
            nativeQuery = true
    )
    void insertAuthorToBookByAuthorIdAndBookId(Long authorId, Long bookId);

    Optional<Book> findByTitle(String bookTitle);
}
