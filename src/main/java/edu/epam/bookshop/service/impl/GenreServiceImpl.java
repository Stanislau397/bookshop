package edu.epam.bookshop.service.impl;

import edu.epam.bookshop.entity.Genre;
import edu.epam.bookshop.entity.Language;
import edu.epam.bookshop.entity.LocalizedGenre;
import edu.epam.bookshop.exception.EntityAlreadyExistsException;
import edu.epam.bookshop.exception.EntityNotFoundException;
import edu.epam.bookshop.exception.NothingFoundException;
import edu.epam.bookshop.repository.GenreRepository;
import edu.epam.bookshop.repository.LocalizedGenreRepository;
import edu.epam.bookshop.service.BookService;
import edu.epam.bookshop.service.GenreService;
import edu.epam.bookshop.service.LanguageService;
import edu.epam.bookshop.validator.GenreValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static edu.epam.bookshop.constant.ExceptionMessage.GENRES_WITH_GIVEN_BOOK_ID_AND_LANGUAGE_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.GENRE_WITH_GIVEN_ID_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.GENRE_WITH_GIVEN_TITLE_AND_LANGUAGE_EXISTS;
import static edu.epam.bookshop.constant.ExceptionMessage.NOTHING_WAS_FOUND_MSG;

import static edu.epam.bookshop.service.ItemsLimit.EIGHT;

@Service
@Slf4j
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final LocalizedGenreRepository localizedGenreRepository;

    private final LanguageService languageService;
    private final BookService bookService;

    private GenreValidator genreValidator;

    @Override
    public void addGenreByTitleAndLanguage(String genreTitle, String language) {
        Language selectedLanguage = languageService.findLanguageByName(language);
        boolean genreTitleIsValid = genreValidator.isTitleValid(genreTitle);
        boolean genreExistsByTitleAndLanguage = localizedGenreExistsByTitleAndLanguage(genreTitle, selectedLanguage);
        if (genreTitleIsValid && !genreExistsByTitleAndLanguage) {
            Genre genre = Genre.builder()
                    .build();
            Genre savedGenre = genreRepository.save(genre);
            LocalizedGenre localizedGenre = LocalizedGenre.builder()
                    .genre(savedGenre)
                    .title(genreTitle)
                    .language(selectedLanguage)
                    .build();
            localizedGenreRepository.save(localizedGenre);
        }
    }

    @Override
    public void addLocalizationToExistingGenre(String localizedGenreTitle, Long genreId, String languageName) {
        Language existingLanguage = languageService.findLanguageByName(languageName);
        Genre existingGenre = findGenreByGenreId(genreId);
        boolean localizedGenreTitleValid = genreValidator.isTitleValid(localizedGenreTitle);
        boolean localizedGenreExists = localizedGenreExistsByTitleAndLanguage(localizedGenreTitle, existingLanguage);
        if (localizedGenreTitleValid && !localizedGenreExists) {
            LocalizedGenre localizedGenreToAdd = LocalizedGenre.builder()
                    .title(localizedGenreTitle)
                    .genre(existingGenre)
                    .language(existingLanguage)
                    .build();
            localizedGenreRepository.save(localizedGenreToAdd);
        }
    }

    @Override
    public void updateLocalizedGenreByGenreIdAndLanguageName(String newGenreTitle, Long genreId, String languageName) {
        Language selectedLanguage = languageService.findLanguageByName(languageName);
        boolean localizedGenreExists = localizedGenreExistsByTitleAndLanguage(newGenreTitle, selectedLanguage);
        if (!localizedGenreExists) {
            long languageId = selectedLanguage.getLanguageId();
            localizedGenreRepository.updateByGenreIdAndLanguageId(newGenreTitle, genreId, languageId);
        }
    }

    @Override
    public void deleteGenreById(Long genreId) {
        if (!genreRepository.existsById(genreId)) {
            log.info(String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, genreId));
            throw new EntityNotFoundException(
                    String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, genreId)
            );
        }
        genreRepository.deleteById(genreId);
    }

    @Override
    public void deleteLocalizedGenreById(Long localizedGenreId) {
        if (!localizedGenreRepository.existsById(localizedGenreId)) {
            log.info(String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, localizedGenreId));
            throw new EntityNotFoundException(
                    String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, localizedGenreId)
            );
        }
        localizedGenreRepository.deleteById(localizedGenreId);
    }

    @Override
    public boolean localizedGenreExistsByTitleAndLanguage(String genreTitle, Language language) {
        boolean genreExistsByTitleAndLanguage =
                localizedGenreRepository.existsByTitleAndLanguage(genreTitle, language);
        if (genreExistsByTitleAndLanguage) {
            log.info(
                    String.format(GENRE_WITH_GIVEN_TITLE_AND_LANGUAGE_EXISTS, genreTitle, language.getName()));
            throw new EntityAlreadyExistsException(
                    String.format(GENRE_WITH_GIVEN_TITLE_AND_LANGUAGE_EXISTS, genreTitle, language.getName())
            );
        }
        return genreExistsByTitleAndLanguage;
    }

    @Override
    public Genre findGenreByGenreId(Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> {
                    log.info(String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, genreId));
                    throw new EntityNotFoundException(
                            String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, genreId));
                });
    }

    @Override
    public Page<LocalizedGenre> findLocalizedGenresByLanguageNameAndPageNumber(String languageName, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, EIGHT);
        Language selectedLanguage = languageService.findLanguageByName(languageName);
        long languageId = selectedLanguage.getLanguageId();
        Page<LocalizedGenre> localizedGenres =
                genreRepository.selectLocalizedGenresByLanguageIdAndPage(languageId, pageable);
        if (localizedGenres.isEmpty()) {
            log.info(NOTHING_WAS_FOUND_MSG);
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return localizedGenres;
    }

    @Override
    public List<LocalizedGenre> findLocalizedGenresByKeywordAndLanguageName(String keyword, String languageName) {
        List<LocalizedGenre> localizedGenresByKeyWordAndLanguage =
                localizedGenreRepository.selectByKeywordAndLanguageName(keyword, languageName)
                        .stream()
                        .limit(EIGHT)
                        .collect(Collectors.toList());
        if (localizedGenresByKeyWordAndLanguage.isEmpty()) {
            log.info(NOTHING_WAS_FOUND_MSG);
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return localizedGenresByKeyWordAndLanguage;
    }

    @Override
    public List<LocalizedGenre> findLocalizedGenresByBookIdAndLanguage(Long bookId, String language) {
        List<LocalizedGenre> localizedGenresByBookIdAndLanguage = new ArrayList<>();
        boolean bookExistsById = bookService.bookExistsById(bookId);
        boolean languageExistsByName = languageService.languageExistsByName(language);
        Language givenLanguage = languageService.findLanguageByName(language);
        if (bookExistsById && languageExistsByName) {
            long languageId = givenLanguage.getLanguageId();
            localizedGenresByBookIdAndLanguage =
                    localizedGenreRepository.selectLocalizedGenresByBookIdAndLocale(bookId, languageId);
        }
        if (localizedGenresByBookIdAndLanguage.isEmpty()) {
            log.info(String.format(GENRES_WITH_GIVEN_BOOK_ID_AND_LANGUAGE_NOT_FOUND, bookId, language));
            throw new NothingFoundException(
                    String.format(GENRES_WITH_GIVEN_BOOK_ID_AND_LANGUAGE_NOT_FOUND, bookId, language)
            );
        }
        return localizedGenresByBookIdAndLanguage;
    }
}
