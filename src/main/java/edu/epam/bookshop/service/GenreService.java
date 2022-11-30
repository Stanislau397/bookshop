package edu.epam.bookshop.service;

import edu.epam.bookshop.entity.Genre;
import edu.epam.bookshop.entity.Language;
import edu.epam.bookshop.entity.LocalizedGenre;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GenreService {

    void addGenreByTitleAndLanguage(String genreTitle, String language);

    void addLocalizationToExistingGenre(String localizedGenreTitle, Long genreId, String languageName);

    void updateLocalizedGenreByGenreIdAndLanguageName(String newGenreTitle, Long genreId, String languageName);

    void deleteGenreById(Long genreId);

    void deleteLocalizedGenreById(Long localizedGenreId);

    boolean localizedGenreExistsByTitleAndLanguage(String genreTitle, Language language);

    Genre findGenreByGenreId(Long genreId);

    Page<LocalizedGenre> findLocalizedGenresByLanguageNameAndPageNumber(String languageName, int pageNumber);

    List<LocalizedGenre> findLocalizedGenresByKeywordAndLanguageName(String keyword, String languageName);

    List<LocalizedGenre> findLocalizedGenresByBookIdAndLanguage(Long bookId, String language);
}
