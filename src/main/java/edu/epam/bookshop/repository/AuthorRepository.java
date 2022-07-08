package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static edu.epam.bookshop.repository.SqlQuery.SELECT_AUTHOR_BY_BOOK_ID;
import static edu.epam.bookshop.repository.SqlQuery.UPDATE_AUTHOR_INFO_BY_ID;

import java.time.LocalDate;
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

    Optional<Author> findByAuthorId(Long authorId);

    @Query(value = SELECT_AUTHOR_BY_BOOK_ID)
    Optional<Author> findByBookId(Long bookId);
}
