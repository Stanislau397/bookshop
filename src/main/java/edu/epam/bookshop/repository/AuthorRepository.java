package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static edu.epam.bookshop.repository.SqlQuery.DELETE_AUTHOR_FROM_BOOK;
import static edu.epam.bookshop.repository.SqlQuery.INSERT_AUTHOR_TO_BOOK;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_AUTHORS_BY_BOOK_ID;
import static edu.epam.bookshop.repository.SqlQuery.UPDATE_AUTHOR_INFO_BY_ID;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Transactional
    @Modifying
    @Query(value = UPDATE_AUTHOR_INFO_BY_ID)
    void updateInfoByAuthorFieldsAndId(String firstName,
                                       String lastName,
                                       String biography,
                                       LocalDate birthDate,
                                       String imagePath,
                                       Long authorId);

    @Transactional
    @Modifying
    @Query(
            value = INSERT_AUTHOR_TO_BOOK,
            nativeQuery = true)
    void insertAuthorToBookByBookIdAndAuthorId(Long bookId, Long authorId);

    @Transactional
    @Modifying
    @Query(
            value = DELETE_AUTHOR_FROM_BOOK,
            nativeQuery = true
    )
    void deleteAuthorFromBookByAuthorIdAndBookId(Long authorId, Long bookId);

    Optional<Author> findByAuthorId(Long authorId);

    @Query(value = SELECT_AUTHORS_BY_BOOK_ID)
    List<Author> findByBookId(Long bookId);
}
