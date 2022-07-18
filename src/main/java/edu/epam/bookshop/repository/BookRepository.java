package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Book;
import edu.epam.bookshop.entity.CoverType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static edu.epam.bookshop.repository.SqlQuery.CHECK_IF_BOOK_EXISTS_FOR_AUTHOR;
import static edu.epam.bookshop.repository.SqlQuery.COUNT_BOOKS_BY_GENRE_TITLE;
import static edu.epam.bookshop.repository.SqlQuery.COUNT_BOOKS_BY_KEYWORD;
import static edu.epam.bookshop.repository.SqlQuery.COUNT_BOOKS_BY_YEAR;
import static edu.epam.bookshop.repository.SqlQuery.DELETE_BOOK_FROM_AUTHOR;
import static edu.epam.bookshop.repository.SqlQuery.INSERT_AUTHOR_TO_BOOK;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_BOOKS_BY_GENRE_TITLE;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_BOOKS_BY_KEYWORD;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_BOOKS_BY_YEAR;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_EXISTING_YEARS_FOR_BOOKS;
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
    @Query(value = DELETE_BOOK_FROM_AUTHOR,
            nativeQuery = true)
    void deleteBookFromAuthorByAuthorIdAndBookId(Long authorId, Long bookId);

    @Query(value = CHECK_IF_BOOK_EXISTS_FOR_AUTHOR,
            nativeQuery = true)
    Boolean bookExistsForAuthor(Long authorId, Long bookId);

    @Transactional
    @Modifying
    @Query(value = INSERT_AUTHOR_TO_BOOK,
            nativeQuery = true)
    void insertAuthorToBookByAuthorIdAndBookId(Long authorId, Long bookId);

    @Query(value = SELECT_BOOKS_BY_KEYWORD,
            countQuery = COUNT_BOOKS_BY_KEYWORD)
    Page<Book> selectBooksByKeyWordAndPage(String keyWord, Pageable page);

    @Query(value = SELECT_BOOKS_BY_YEAR,
            countQuery = COUNT_BOOKS_BY_YEAR)
    Page<Book> selectBooksByYearAndPage(Integer year, Pageable page);

    @Query(value = SELECT_BOOKS_BY_GENRE_TITLE,
            countQuery = COUNT_BOOKS_BY_GENRE_TITLE)
    Page<Book> selectBooksByGenreTitleAndPage(String genreTitle, Pageable page);

    @Query(value = SELECT_EXISTING_YEARS_FOR_BOOKS)
    List<Integer> selectExistingYearsInBooksOrderedByYearAsc();

    Optional<Book> findByTitle(String bookTitle);
}
