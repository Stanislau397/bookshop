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

import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_BOOKS_BY_KEYWORD;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_BOOKS_BY_PAGE;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_BOOK_DETAILS;
import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_BOOK_TO_AUTHOR_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_BOOK_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.REMOVE_BOOK_FROM_AUTHOR_URN;
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

    @PostMapping(REMOVE_BOOK_FROM_AUTHOR_URN)
    public ResponseEntity<Void> removeBookFromAuthor(Long authorId, Long bookId) {
        bookService.removeBookForAuthorByAuthorIdAndBookId(authorId, bookId);
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
}
