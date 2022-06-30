package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

import static edu.epam.bookshop.repository.SqlQuery.DELETE_BOOK_FROM_AUTHOR;
import static edu.epam.bookshop.repository.SqlQuery.INSERT_BOOK_TO_AUTHOR;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying
    @Transactional
    @Query(
            value = INSERT_BOOK_TO_AUTHOR,
            nativeQuery = true)
    void insertBookToAuthorByBookIdAndAuthorId(Long bookId, Long authorId);

    @Modifying
    @Transactional
    @Query(
            value = DELETE_BOOK_FROM_AUTHOR,
            nativeQuery = true)
    void deleteBookFromAuthorByAuthorIdAndBookId(Long authorId, Long bookId);

    Optional<Book> findByTitle(String bookTitle);
}
