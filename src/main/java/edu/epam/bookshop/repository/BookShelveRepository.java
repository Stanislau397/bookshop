package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.BookShelve;
import edu.epam.bookshop.entity.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import java.util.Optional;

import static edu.epam.bookshop.repository.SqlQuery.CHECK_IF_BOOK_EXISTS_IN_BOOK_SHELVE;
import static edu.epam.bookshop.repository.SqlQuery.CHECK_IF_SHELVE_EXISTS_BY_ID;
import static edu.epam.bookshop.repository.SqlQuery.COUNT_BOOKS_ON_SHELVE;
import static edu.epam.bookshop.repository.SqlQuery.INSERT_BOOK_TO_SHELVE;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_BOOK_STATUS_ON_SHELVE;
import static edu.epam.bookshop.repository.SqlQuery.UPDATE_BOOK_STATUS_ON_SHELVE;

@Repository
public interface BookShelveRepository extends JpaRepository<BookShelve, Long> {

    @Transactional
    @Modifying
    @Query(value = INSERT_BOOK_TO_SHELVE,
            nativeQuery = true)
    void insertBookToShelveByShelveIdAndBookIdAndBookStatus(Long shelveId, Long bookId, String bookStatus);

    @Transactional
    @Modifying
    @Query(value = UPDATE_BOOK_STATUS_ON_SHELVE,
            nativeQuery = true)
    void updateBookStatusByShelveIdAndBookId(String newStatus, Long shelveId, Long bookId);

    @Query(value = CHECK_IF_SHELVE_EXISTS_BY_ID)
    boolean existsByShelveId(Long shelveId);

    @Query(value = CHECK_IF_BOOK_EXISTS_IN_BOOK_SHELVE)
    boolean bookExistsByShelveIdAndBookId(Long shelveId, Long bookId);

    @Query(value = COUNT_BOOKS_ON_SHELVE)
    Optional<Integer> selectCountBooksOnShelveByShelveIdAndBookStatus(Long shelveId, BookStatus bookStatus);

    @Query(value = SELECT_BOOK_STATUS_ON_SHELVE, nativeQuery = true)
    Optional<String> selectBookStatusByShelveIdAndBookId(Long shelveId, Long bookId);
}
