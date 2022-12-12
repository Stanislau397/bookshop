package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static edu.epam.bookshop.repository.SqlQuery.CHECK_IF_BOOK_EXISTS_FOR_AUTHOR;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_BOOKS_BY_LOCALIZED_GENRE_TITLE;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_BOOKS_WITH_SCORE_GREATER_THAN;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_LOCALIZED_BOOKS_BY_AVG_SCORE_GREATER_THAN;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_BOOKS_COUNT_WITH_AVG_SCORE_GREATER_THAN;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_EXISTING_YEARS_FOR_BOOKS;
import static edu.epam.bookshop.repository.SqlQuery.UPDATE_BOOK_INFO;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying
    @Transactional
    @Query(value = UPDATE_BOOK_INFO)
    void updateBookInfoById(Book updatedBook);

    @Query(value = CHECK_IF_BOOK_EXISTS_FOR_AUTHOR,
            nativeQuery = true)
    Boolean bookExistsForAuthor(Long authorId, Long bookId);

    @Query(value = SELECT_BOOKS_BY_LOCALIZED_GENRE_TITLE)
    Page<Book> selectByLocalizedGenreTitleAndPage(String genreTitle, Pageable page);

//    @Query(value = SELECT_BOOKS_BY_SHELVE_ID_AND_BOOK_STATUS)
//    Page<Book> selectBooksByPageAndShelveIdAndBookStatus(Long shelveId, BookStatus bookStatus, Pageable page);

    @Query(value = SELECT_LOCALIZED_BOOKS_BY_AVG_SCORE_GREATER_THAN)
    Page<Book> selectBooksByPageHavingAverageScoreGreaterThan(Double score, Pageable page);

    @Query(value = SELECT_EXISTING_YEARS_FOR_BOOKS)
    List<Integer> selectExistingYearsInBooksOrderedByYearAsc();

    @Query(value = SELECT_BOOKS_COUNT_WITH_AVG_SCORE_GREATER_THAN,
            nativeQuery = true)
    Integer selectBooksCountHavingAverageScoreGreaterThan(Double score);

    @Query(SELECT_BOOKS_WITH_SCORE_GREATER_THAN)
    List<Book> selectByScoreGreaterThan(Double score);
}
