package edu.epam.bookshop.controller;

import edu.epam.bookshop.entity.Book;
import edu.epam.bookshop.entity.BookStatus;
import edu.epam.bookshop.entity.CoverType;
import edu.epam.bookshop.entity.LocalizedBook;
import edu.epam.bookshop.entity.ShelveBook;
import edu.epam.bookshop.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static edu.epam.bookshop.constant.GetMappingURN.COUNT_BOOKS_WITH_AVG_SCORE_GREATER_THAN;
import static edu.epam.bookshop.constant.GetMappingURN.FIND_BOOKS_BY_GENRE_TITLE_AND_PAGE_URN;
import static edu.epam.bookshop.constant.GetMappingURN.FIND_BOOKS_BY_KEYWORD_AND_PAGE;
import static edu.epam.bookshop.constant.GetMappingURN.FIND_BOOKS_BY_PAGE_HAVING_AVG_SCORE_GREATER_THAN;
import static edu.epam.bookshop.constant.GetMappingURN.FIND_BOOKS_BY_SHELVE_ID_AND_BOOK_STATUS;
import static edu.epam.bookshop.constant.GetMappingURN.FIND_BOOKS_BY_YEAR_AND_PAGE_URN;
import static edu.epam.bookshop.constant.GetMappingURN.FIND_BOOKS_WITH_HIGH_SCORE_LIMIT_15;
import static edu.epam.bookshop.constant.GetMappingURN.FIND_EXISTING_YEARS_IN_BOOKS_URN;
import static edu.epam.bookshop.constant.PostMappingURN.ADD_BOOK_TO_AUTHOR_URN;
import static edu.epam.bookshop.constant.PostMappingURN.ADD_BOOK_URN;
import static edu.epam.bookshop.constant.PostMappingURN.ADD_LOCALIZATION_TO_BOOK_URN;
import static edu.epam.bookshop.constant.PostMappingURN.UPDATE_BOOK_INFO_URN;

@AllArgsConstructor
@RestController
public class BookController {

    private static final String NULL_STRING = "null";
    private static final String EMPTY_STRING = "";
    private final BookService bookService;

    @RequestMapping(value = ADD_BOOK_URN, method = RequestMethod.POST,
            consumes = {"multipart/form-data"})
    public ResponseEntity<Void> insertBook(@RequestPart @Valid Book book,
                                           @RequestPart @Valid LocalizedBook localizedBook,
                                           @RequestPart(required = false) MultipartFile bookImage,
                                           @RequestPart String languageName) {
        bookService.addBook(book, localizedBook, bookImage, languageName);
        return ResponseEntity.ok().build();
    }

    @PostMapping(UPDATE_BOOK_INFO_URN)
    public ResponseEntity<Boolean> changeBookInfo(@RequestPart @Valid Book bookFromRequest,
                                                  @RequestPart @Valid LocalizedBook localizedBookFromRequest,
                                                  @RequestPart(required = false) MultipartFile imageFromRequest) {
        Boolean isBookUpdated = bookService.updateBookInfo(bookFromRequest, localizedBookFromRequest, imageFromRequest);
        return ResponseEntity.ok(isBookUpdated);
    }

    @PostMapping(ADD_BOOK_TO_AUTHOR_URN)
    public ResponseEntity<Void> addBookToAuthor(Long authorId, Long bookId) {
        bookService.addAuthorToBook(bookId, authorId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = ADD_LOCALIZATION_TO_BOOK_URN, method = RequestMethod.POST,
            consumes = {"multipart/form-data"})
    public ResponseEntity<Void> addLocalization(@RequestPart @Valid LocalizedBook localizedBook,
                                                @RequestPart(required = false) MultipartFile localizedImage,
                                                @RequestPart String languageName) {
        bookService.addLocalizationToExistingBook(localizedBook, localizedImage, languageName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/findLocalizedBookByBookIdAndLanguage")
    public ResponseEntity<LocalizedBook> displayLocalizedBookByBookIdAndLanguage(Long bookId) {
        String currentLanguage = LocaleContextHolder
                .getLocale()
                .getLanguage();
        return ResponseEntity.ok(bookService.findLocalizedBookDetailsByBookIdAndLanguage(bookId, currentLanguage));
    }

    @GetMapping(FIND_BOOKS_BY_GENRE_TITLE_AND_PAGE_URN)
    public ResponseEntity<Page<Book>> displayBooksByGenreTitleAndPageNumber(String genreTitle, Integer page) {
        Page<Book> booksByGenreTitleAndPage =
                bookService.findBooksByLocalizedGenreTitleAndPageNumber(genreTitle, page);
        return ResponseEntity.ok(booksByGenreTitleAndPage);
    }

    @GetMapping(FIND_BOOKS_BY_YEAR_AND_PAGE_URN)
    public ResponseEntity<Page<Book>> displayBooksByYearAndPageNumber(Integer year, Integer page) {
        Page<Book> booksByYearAndPage = bookService.findBooksByYearAndPageNumber(year, page);
        return ResponseEntity.ok(booksByYearAndPage);
    }

    @GetMapping(FIND_BOOKS_BY_KEYWORD_AND_PAGE)
    public ResponseEntity<Page<LocalizedBook>> displayBooksByKeyWordAndPage(String keyWord, Integer page, String languageName) {
        Page<LocalizedBook> booksByKeyWordAndPage =
                bookService.findLocalizedBooksByKeywordAndPageNumberAndLanguage(keyWord, page, languageName);
        return ResponseEntity.ok(booksByKeyWordAndPage);
    }

    @GetMapping(FIND_BOOKS_WITH_HIGH_SCORE_LIMIT_15)
    public ResponseEntity<List<LocalizedBook>> displayBooksWithHighScoreLimit15(Double score) {
        String languageName = LocaleContextHolder
                .getLocale()
                .getLanguage();
        List<LocalizedBook> localizedBooksHavingAverageScoreGreaterThan =
                bookService.findTop15LocalizedBooksByLanguageNameHavingAverageScoreGreaterThan(languageName, score);
        return ResponseEntity.ok(localizedBooksHavingAverageScoreGreaterThan);
    }

    @GetMapping(FIND_BOOKS_BY_PAGE_HAVING_AVG_SCORE_GREATER_THAN)
    public ResponseEntity<Page<Book>> displayBooksByPageHavingAvgScoreGreaterThan(Double score, Integer pageNumber) {
        Page<Book> booksWithAvgScoreGreaterThan =
                bookService.findBooksByPageHavingAverageScoreGreaterThan(score, pageNumber);
        return ResponseEntity.ok(booksWithAvgScoreGreaterThan);
    }

    @GetMapping("/displayAllLocalizedBooksByLanguageAndPageNumber")
    public ResponseEntity<Page<LocalizedBook>> displayAllLocalizedBooksByLanguageAndPageNumber(String languageName, int pageNumber) {
        String currentLanguage = LocaleContextHolder
                .getLocale()
                .getLanguage();
        if (Objects.equals(languageName, EMPTY_STRING) || Objects.equals(languageName, NULL_STRING)) {
            return ResponseEntity.ok(bookService.findAllLocalizedBooksByLanguageAndPageNumber(currentLanguage, pageNumber));
        }
        return ResponseEntity.ok(bookService.findAllLocalizedBooksByLanguageAndPageNumber(languageName, pageNumber));
    }

    @GetMapping("/displayLocalizedBooksByKeyword")
    public ResponseEntity<List<LocalizedBook>> displayLocalizedBooksByKeyword(String keyword) {
        String currentLanguage = LocaleContextHolder.getLocale()
                .getLanguage();
        return ResponseEntity.ok(bookService.findLocalizedBooksByKeywordAndLanguageNameLimit6(keyword, currentLanguage));
    }

    @GetMapping(FIND_BOOKS_BY_SHELVE_ID_AND_BOOK_STATUS)
    public ResponseEntity<List<ShelveBook>> displayBooksByShelveIdAndStatus(Long shelveId, String bookStatus,
                                                                            Integer pageNumber) {
        List<ShelveBook> booksByShelveIdAndStatus =
                bookService.findShelveBooks(shelveId, BookStatus.valueOf(bookStatus));
        return ResponseEntity.ok(booksByShelveIdAndStatus);
    }

    @GetMapping(FIND_EXISTING_YEARS_IN_BOOKS_URN)
    public ResponseEntity<List<Integer>> displayExistingYears() {
        List<Integer> existingYears = bookService.findExistingYearsInBooks();
        return ResponseEntity.ok(existingYears);
    }

    @GetMapping(COUNT_BOOKS_WITH_AVG_SCORE_GREATER_THAN)
    public ResponseEntity<Integer> displayNumberOfBooksWithScoreGreaterThan(Double score) {
        Integer amountOfBooksWithHighScore =
                bookService.findNumberOfBooksWithAverageScoreGreaterThan(score);
        return ResponseEntity.ok(amountOfBooksWithHighScore);
    }

    @GetMapping("/findAllCoverTypes")
    public ResponseEntity<List<CoverType>> displayAllCoverTypes() {
        return ResponseEntity.ok(bookService.findAllCoverTypes());
    }
}
