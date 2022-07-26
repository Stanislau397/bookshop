package edu.epam.bookshop.controller;

import edu.epam.bookshop.entity.Book;
import edu.epam.bookshop.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static edu.epam.bookshop.controller.constant.GetMappingURN.COUNT_BOOKS_WITH_AVG_SCORE_GREATER_THAN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_BOOKS_BY_GENRE_TITLE_AND_PAGE_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_BOOKS_BY_KEYWORD;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_BOOKS_BY_KEYWORD_AND_PAGE;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_BOOKS_BY_PAGE;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_BOOKS_BY_PAGE_HAVING_AVG_SCORE_GREATER_THAN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_BOOKS_BY_YEAR_AND_PAGE_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_BOOKS_WITH_HIGH_SCORE_LIMIT_15;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_BOOK_DETAILS;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_EXISTING_YEARS_IN_BOOKS_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_BOOK_TO_AUTHOR_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_BOOK_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.UPDATE_BOOK_INFO_URN;

@AllArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    @PostMapping(ADD_BOOK_URN)
    public ResponseEntity<Void> insertBook(Book book, MultipartFile bookImage) {
        bookService.addBook(book, bookImage);
        return ResponseEntity.ok().build();
    }

    @PostMapping(UPDATE_BOOK_INFO_URN)
    public ResponseEntity<Boolean> changeBookInfo(Book book, MultipartFile newBookImage) {
        Boolean isBookUpdated = bookService.updateBookInfo(book, newBookImage);
        return ResponseEntity.ok(isBookUpdated);
    }

    @PostMapping(ADD_BOOK_TO_AUTHOR_URN)
    public ResponseEntity<Void> addBookToAuthor(Long authorId, Long bookId) {
        bookService.addAuthorToBook(bookId, authorId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(FIND_BOOK_DETAILS)
    public ResponseEntity<Book> displayBookDetails(String title) {
        Book foundBookByTitle = bookService.findBookDetailsByTitle(title);
        return ResponseEntity.ok(foundBookByTitle);
    }

    @GetMapping(FIND_BOOKS_BY_KEYWORD)
    public ResponseEntity<List<Book>> displayBooksByKeyword(String keyWord) {
        List<Book> booksByKeyWord = bookService.findBooksByKeyWord(keyWord);
        return ResponseEntity.ok(booksByKeyWord);
    }

    @GetMapping(FIND_BOOKS_BY_PAGE)
    public ResponseEntity<Page<Book>> displayBooksByPage(Integer page) {
        Page<Book> booksByPage = bookService.findBooksByPage(page);
        return ResponseEntity.ok(booksByPage);
    }

    @GetMapping(FIND_BOOKS_BY_GENRE_TITLE_AND_PAGE_URN)
    public ResponseEntity<Page<Book>> displayBooksByGenreTitleAndPageNumber(String genreTitle, Integer page) {
        Page<Book> booksByGenreTitleAndPage =
                bookService.findBooksByGenreTitleAndPageNumber(genreTitle, page);
        return ResponseEntity.ok(booksByGenreTitleAndPage);
    }

    @GetMapping(FIND_BOOKS_BY_YEAR_AND_PAGE_URN)
    public ResponseEntity<Page<Book>> displayBooksByYearAndPageNumber(Integer year, Integer page) {
        Page<Book> booksByYearAndPage = bookService.findBooksByYearAndPageNumber(year, page);
        return ResponseEntity.ok(booksByYearAndPage);
    }

    @GetMapping(FIND_BOOKS_BY_KEYWORD_AND_PAGE)
    public ResponseEntity<Page<Book>> displayBooksByKeyWordAndPage(String keyWord, Integer page) {
        Page<Book> booksByKeyWordAndPage = bookService.findBooksByKeyWordAndPageNumber(keyWord, page);
        return ResponseEntity.ok(booksByKeyWordAndPage);
    }

    @GetMapping(FIND_BOOKS_WITH_HIGH_SCORE_LIMIT_15)
    public ResponseEntity<List<Book>> displayBooksWithHighScoreLimit15(Double score) {
        List<Book> booksWithHighScoreLimit15 =
                bookService.findTop15BooksHavingAverageScoreGreaterThan(score);
        return ResponseEntity.ok(booksWithHighScoreLimit15);
    }

    @GetMapping(FIND_BOOKS_BY_PAGE_HAVING_AVG_SCORE_GREATER_THAN)
    public ResponseEntity<Page<Book>> displayBooksByPageHavingAvgScoreGreaterThan(Double score, Integer pageNumber) {
        Page<Book> booksWithAvgScoreGreaterThan =
                bookService.findBooksByPageHavingAverageScoreGreaterThan(score, pageNumber);
        return ResponseEntity.ok(booksWithAvgScoreGreaterThan);
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
}
