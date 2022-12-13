package edu.epam.bookshop.controller;

import edu.epam.bookshop.entity.Language;
import edu.epam.bookshop.entity.LocalizedGenre;
import edu.epam.bookshop.service.BookService;
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
import static edu.epam.bookshop.constant.PostMappingURN.ADD_GENRE_URN;
import static edu.epam.bookshop.constant.PostMappingURN.DELETE_GENRE_BY_ID_URN;

@AllArgsConstructor
@RestController
public class GenreController {

    private static final String EMPTY_STRING = "";
    private static final String NULL_STRING = "null";

    private final BookService bookService;
    private final LanguageService languageService;

    @PostMapping(ADD_GENRE_URN)
    public ResponseEntity<Void> insertGenreByTitleAndLanguage(@RequestParam String genreTitle, @RequestParam String language) {
        bookService.addGenreByTitleAndLanguage(genreTitle, language);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addLocalizedGenreByTitleAndGenreIdAndLanguageName")
    public ResponseEntity<Void> addLocalizedGenre(String genreTitle, Long genreId, String languageName) {
        bookService.addLocalizationToExistingGenre(genreTitle, genreId, languageName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updateLocalizedGenreByGenreIdAndLanguageName")
    public ResponseEntity<Void> updateLocalizedGenre(String genreTitle, Long genreId, String languageName) {
        bookService.updateLocalizedGenreByGenreIdAndLanguageName(genreTitle, genreId, languageName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/removeLocalizedGenreById")
    public ResponseEntity<Void> removeLocalizedGenreById(Long localizedGenreId) {
        bookService.deleteLocalizedGenreById(localizedGenreId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(DELETE_GENRE_BY_ID_URN)
    public ResponseEntity<Void> removeGenreById(Long genreId) {
        bookService.deleteGenreById(genreId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/checkIfLocalizedGenreExistsByTitleAndLanguage")
    public ResponseEntity<Boolean> checkIfLocalizedGenreExistsByTitleAndLanguage(String title, String languageName) {
        System.out.println(languageName);
        System.out.println(title);
        Language language = languageService.findLanguageByName(languageName);
        boolean localizedGenreExistsByTitleAndLanguage =
                bookService.localizedGenreExistsByTitleAndLanguage(title, language);
        return ResponseEntity.ok(localizedGenreExistsByTitleAndLanguage);
    }

    @GetMapping(FIND_GENRES_BY_BOOK_ID_URN)
    public ResponseEntity<List<LocalizedGenre>> displayLocalizedGenresByBookId(Long bookId) {
        String currentLanguage = LocaleContextHolder
                .getLocale()
                .getLanguage();
        return null;
    }

    @GetMapping("/displayLocalizedGenresByLanguageNameAndPageNumber")
    public ResponseEntity<Page<LocalizedGenre>> findShit(String languageName, int pageNumber) {
        String currentLanguage = LocaleContextHolder
                .getLocale()
                .getLanguage();
        Page<LocalizedGenre> localizedGenresByLanguageNameAndPageNumber;
        if (Objects.equals(languageName, EMPTY_STRING) || Objects.equals(languageName, NULL_STRING)) {
            localizedGenresByLanguageNameAndPageNumber =
                    bookService.findLocalizedGenresByLanguageNameAndPageNumber(currentLanguage, pageNumber);
        } else {
            localizedGenresByLanguageNameAndPageNumber =
                    bookService.findLocalizedGenresByLanguageNameAndPageNumber(languageName, pageNumber);
        }
        return ResponseEntity.ok(localizedGenresByLanguageNameAndPageNumber);
    }

    @GetMapping("/displayLocalizedGenresByKeywordAndLanguage")
    public ResponseEntity<List<LocalizedGenre>> displayLocalizedGenresByKeywordAndLanguage(String keyword, String languageName) {
        List<LocalizedGenre> localizedGenresByKeywordAndLanguage =
                bookService.findLocalizedGenresByKeywordAndLanguageName(keyword, languageName);
        return ResponseEntity.ok(localizedGenresByKeywordAndLanguage);
    }
}
