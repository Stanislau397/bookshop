package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Genre;
import edu.epam.bookshop.entity.LocalizedGenre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static edu.epam.bookshop.repository.SqlQuery.SELECT_LOCALIZED_GENRES_BY_LANGUAGE;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_DISTINCT_GENRES_FOR_AUTHOR;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_LOCALIZED_GENRES_BY_LANGUAGE_ID;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Query(value = SELECT_DISTINCT_GENRES_FOR_AUTHOR)
    List<Genre> selectDistinctGenresForAuthorByAuthorId(Long authorId);

    @Query(SELECT_LOCALIZED_GENRES_BY_LANGUAGE_ID)
    Page<LocalizedGenre> selectLocalizedGenresByLanguageIdAndPage(Long languageId, Pageable pageable);
}
