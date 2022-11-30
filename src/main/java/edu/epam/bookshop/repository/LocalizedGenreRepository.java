package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Language;
import edu.epam.bookshop.entity.LocalizedGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static edu.epam.bookshop.repository.SqlQuery.CHECK_IF_LOCALIZED_GENRE_EXISTS_BY_TITLE_AND_LANGUAGE;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_LOCALIZED_GENRES_BY_BOOK_ID_AND_LOCALE;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_LOCALIZED_GENRES_BY_KEYWORD_AND_LANGUAGE;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_LOCALIZED_GENRES_BY_LANGUAGE;
import static edu.epam.bookshop.repository.SqlQuery.UPDATE_LOCALIZED_GENRE_BY_GENRE_ID_AND_LANGUAGE;

@Repository
public interface LocalizedGenreRepository extends JpaRepository<LocalizedGenre, Long> {

    @Transactional
    @Modifying
    @Query(UPDATE_LOCALIZED_GENRE_BY_GENRE_ID_AND_LANGUAGE)
    void updateByGenreIdAndLanguageId(String newGenreTitle, Long genreId, Long languageId);

    @Query(CHECK_IF_LOCALIZED_GENRE_EXISTS_BY_TITLE_AND_LANGUAGE)
    boolean existsByTitleAndLanguage(String title, Language language);

    @Query(SELECT_LOCALIZED_GENRES_BY_KEYWORD_AND_LANGUAGE)
    List<LocalizedGenre> selectByKeywordAndLanguageName(String keyword, String languageName);

    @Query(value = SELECT_LOCALIZED_GENRES_BY_LANGUAGE)
    List<LocalizedGenre> selectLocalizedGenresByLanguage(String language);

    @Query(value = SELECT_LOCALIZED_GENRES_BY_BOOK_ID_AND_LOCALE)
    List<LocalizedGenre> selectLocalizedGenresByBookIdAndLocale(Long bookId, Long languageId);
}
