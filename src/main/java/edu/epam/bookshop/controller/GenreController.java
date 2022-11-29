package edu.epam.bookshop.controller;

import edu.epam.bookshop.entity.Genre;
import edu.epam.bookshop.entity.Language;
import edu.epam.bookshop.entity.LocalizedGenre;
import edu.epam.bookshop.service.BookService;
import edu.epam.bookshop.service.GenreService;
import edu.epam.bookshop.service.LanguageService;
import lombok.AllArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

import static edu.epam.bookshop.constant.GetMappingURN.FIND_GENRES_BY_BOOK_ID_URN;
import static edu.epam.bookshop.constant.GetMappingURN.FIND_GENRES_BY_KEYWORD_URN;
import static edu.epam.bookshop.constant.GetMappingURN.FIND_GENRES_FOR_AUTHOR_URN;
import static edu.epam.bookshop.constant.GetMappingURN.FIND_LOCALIZED_GENRES_URN;
import static edu.epam.bookshop.constant.GetMappingURN.GENRE_EXISTS_BY_TITLE_URN;
import static edu.epam.bookshop.constant.GetMappingURN.FIND_GENRES_BY_PAGE_URN;
import static edu.epam.bookshop.constant.GetMappingURN.FIND_ALL_GENRES_URN;

import static edu.epam.bookshop.constant.PostMappingURN.ADD_GENRE_TO_BOOK_URN;
import static edu.epam.bookshop.constant.PostMappingURN.ADD_GENRE_URN;
import static edu.epam.bookshop.constant.PostMappingURN.REMOVE_GENRE_FROM_BOOK_URN;
import static edu.epam.bookshop.constant.PostMappingURN.DELETE_GENRE_BY_ID_URN;

@AllArgsConstructor
@RestController
public class GenreController {

    private static final String EMPTY_STRING = "";
    private static final String NULL_STRING = "null";

    private final BookService bookService;
    private final GenreService genreService;
    private final LanguageService languageService;

    @PostMapping(ADD_GENRE_URN)
    public ResponseEntity<Void> insertGenreByTitleAndLanguage(@RequestParam String genreTitle, @RequestParam String language) {
        genreService.addGenreByTitleAndLanguage(genreTitle, language);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addLocalizedGenreByTitleAndGenreIdAndLanguageName")
    public ResponseEntity<Void> addLocalizedGenre(String genreTitle, Long genreId, String languageName) {
        genreService.addLocalizationToExistingGenre(genreTitle, genreId, languageName);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ADD_GENRE_TO_BOOK_URN)
    public ResponseEntity<Void> addGenreToBook(Long genreId, Long bookId) {
        bookService.addGenreToBook(genreId, bookId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(REMOVE_GENRE_FROM_BOOK_URN)
    public ResponseEntity<Void> removeGenreFromBook(Long genreId, Long bookId) {
        bookService.removeGenreFromBook(genreId, bookId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updateLocalizedGenreByGenreIdAndLanguageName")
    public ResponseEntity<Void> updateLocalizedGenre(String genreTitle, Long genreId, String languageName) {
        genreService.updateLocalizedGenreByGenreIdAndLanguageName(genreTitle, genreId, languageName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/removeLocalizedGenreById")
    public ResponseEntity<Void> removeLocalizedGenreById(Long localizedGenreId) {
        genreService.deleteLocalizedGenreById(localizedGenreId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(DELETE_GENRE_BY_ID_URN)
    public ResponseEntity<Void> removeGenreById(Long genreId) {
        genreService.deleteGenreById(genreId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(GENRE_EXISTS_BY_TITLE_URN)
    public ResponseEntity<Boolean> checkIfGenreExistsByTitle(String genreTitle) {
        boolean genreExistsByTitle = bookService.isGenreExistsByTitle(genreTitle);
        return ResponseEntity.ok(genreExistsByTitle);
    }

    @GetMapping("/checkIfLocalizedGenreExistsByTitleAndLanguage")
    public ResponseEntity<Boolean> checkIfLocalizedGenreExistsByTitleAndLanguage(String title, String languageName) {
        System.out.println(languageName);
        System.out.println(title);
        Language language = languageService.findLanguageByName(languageName);
        boolean localizedGenreExistsByTitleAndLanguage =
                genreService.localizedGenreExistsByTitleAndLanguage(title, language);
        return ResponseEntity.ok(localizedGenreExistsByTitleAndLanguage);
    }

    @GetMapping(FIND_GENRES_BY_KEYWORD_URN)
    public ResponseEntity<List<Genre>> getGenresByKeyWord(String keyWord) {
        List<Genre> genresByKeyWord = bookService.findGenresByKeyword(keyWord);
        return ResponseEntity.ok(genresByKeyWord);
    }

    @GetMapping(FIND_GENRES_BY_PAGE_URN)
    public ResponseEntity<Page<Genre>> getGenresByPage(Integer page, String lang) {
        Page<Genre> genresByPage = bookService.findGenresByPage(page);
        return ResponseEntity.ok(genresByPage);
    }

    @GetMapping(FIND_ALL_GENRES_URN)
    public ResponseEntity<List<Genre>> getAllGenres() {
        List<Genre> allGenres = bookService.findAllGenres();
        return ResponseEntity.ok(allGenres);
    }

    @GetMapping(FIND_GENRES_BY_BOOK_ID_URN)
    public ResponseEntity<List<LocalizedGenre>> displayLocalizedGenresByBookId(Long bookId) {
        String currentLanguage = LocaleContextHolder
                .getLocale()
                .getLanguage();
        List<LocalizedGenre> genresByBookIdAndLanguage =
                genreService.findLocalizedGenresByBookIdAndLanguage(bookId, currentLanguage);
        return ResponseEntity.ok(genresByBookIdAndLanguage);
    }

    @GetMapping(FIND_GENRES_FOR_AUTHOR_URN)
    public ResponseEntity<List<Genre>> displayGenresForAuthor(Long authorId) {
        List<Genre> genresByAuthorId = bookService.findDistinctGenresForAuthorByAuthorId(authorId);
        return ResponseEntity.ok(genresByAuthorId);
    }

    @GetMapping("/displayLocalizedGenresByLanguageNameAndPageNumber")
    public ResponseEntity<Page<LocalizedGenre>> findShit(String languageName, int pageNumber) {
        String currentLanguage = LocaleContextHolder
                .getLocale()
                .getLanguage();
        Page<LocalizedGenre> localizedGenresByLanguageNameAndPageNumber;
        if (Objects.equals(languageName, EMPTY_STRING) || Objects.equals(languageName, NULL_STRING)) {
            localizedGenresByLanguageNameAndPageNumber =
                    genreService.findLocalizedGenresByLanguageNameAndPageNumber(currentLanguage, pageNumber);
        } else {
            localizedGenresByLanguageNameAndPageNumber =
                    genreService.findLocalizedGenresByLanguageNameAndPageNumber(languageName, pageNumber);
        }
        return ResponseEntity.ok(localizedGenresByLanguageNameAndPageNumber);
    }

    @GetMapping("/displayLocalizedGenresByKeywordAndLanguage")
    public ResponseEntity<List<LocalizedGenre>> displayLocalizedGenresByKeywordAndLanguage(String keyword, String languageName) {
        List<LocalizedGenre> localizedGenresByKeywordAndLanguage =
                genreService.findLocalizedGenresByKeywordAndLanguageName(keyword, languageName);
        return ResponseEntity.ok(localizedGenresByKeywordAndLanguage);
    }
}
