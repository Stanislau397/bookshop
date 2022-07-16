package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import java.util.List;

import static edu.epam.bookshop.repository.SqlQuery.CHECK_IF_GENRE_EXISTS_FOR_BOOK;
import static edu.epam.bookshop.repository.SqlQuery.DELETE_GENRE_FROM_BOOK;
import static edu.epam.bookshop.repository.SqlQuery.INSERT_GENRE_TO_BOOK;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_GENRE_BY_BOOK_ID;
import static edu.epam.bookshop.repository.SqlQuery.UPDATE_GENRE_TITLE_BY_ID;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Transactional
    @Modifying
    @Query(value = UPDATE_GENRE_TITLE_BY_ID)
    void updateGenreTitleById(String genreTitle, Long genreId);

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

    @Query(
            value = CHECK_IF_GENRE_EXISTS_FOR_BOOK,
            nativeQuery = true
    )
    boolean genreExistsForBook(Long genreId, Long bookId);

    boolean existsByTitle(String genreTitle);

    @Transactional
    @Modifying
    @Query(value = SELECT_GENRE_BY_BOOK_ID)
    List<Genre> findByBookId(Long bookId);
}
