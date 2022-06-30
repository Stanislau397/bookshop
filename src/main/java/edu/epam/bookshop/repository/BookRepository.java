package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

import static edu.epam.bookshop.repository.SqlQuery.INSERT_BOOK_TO_AUTHOR;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying
    @Transactional
    @Query(INSERT_BOOK_TO_AUTHOR)
    void insertBookToAuthorByBookIdAndAuthorId(Long bookId, Long authorId);

    Optional<Book> findByTitle(String bookTitle);
}
