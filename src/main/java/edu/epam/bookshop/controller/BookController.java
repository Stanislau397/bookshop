package edu.epam.bookshop.controller;

import edu.epam.bookshop.entity.Book;
import edu.epam.bookshop.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_BOOKS_BY_KEYWORD;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_BOOKS_BY_PAGE;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_BOOK_DETAILS;
import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_AUTHOR_TO_BOOK_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_BOOK_TO_AUTHOR_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_BOOK_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_GENRE_TO_BOOK_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.REMOVE_AUTHOR_FROM_BOOK;
import static edu.epam.bookshop.controller.constant.PostMappingURN.REMOVE_BOOK_FROM_AUTHOR_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.REMOVE_GENRE_FROM_BOOK_URN;
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
    public ResponseEntity<Void> changeBookInfo(Book book, MultipartFile newBookImage) {
        bookService.updateBookInfo(book, newBookImage);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ADD_BOOK_TO_AUTHOR_URN)
    public ResponseEntity<Void> addBookToAuthor(@RequestParam Long authorId,
                                                @RequestParam Long bookId) {
        bookService.addBookForAuthorByBookIdAndAuthorId(bookId, authorId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(REMOVE_BOOK_FROM_AUTHOR_URN)
    public ResponseEntity<Void> removeBookFromAuthor(@RequestParam Long authorId,
                                                     @RequestParam Long bookId) {
        bookService.removeBookForAuthorByAuthorIdAndBookId(authorId, bookId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ADD_AUTHOR_TO_BOOK_URN)
    public ResponseEntity<Void> addAuthorToBook(@RequestParam Long authorId,
                                                @RequestParam Long bookId) {
        bookService.addAuthorToBookByAuthorIdAndBookId(authorId, bookId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(REMOVE_AUTHOR_FROM_BOOK)
    public ResponseEntity<Void> removeAuthorFromBook(@RequestParam Long authorId,
                                                     @RequestParam Long bookId) {
        bookService.removeAuthorFromBookByAuthorIdAndBookId(authorId, bookId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ADD_GENRE_TO_BOOK_URN)
    public ResponseEntity<Void> addGenreToBook(@RequestParam Long genreId,
                                               @RequestParam Long bookId) {
        bookService.addGenreToBookByGenreIdAndBookId(genreId, bookId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(REMOVE_GENRE_FROM_BOOK_URN)
    public ResponseEntity<Void> removeGenreFromBook(@RequestParam Long genreId,
                                                    @RequestParam Long bookId) {
        bookService.removeGenreFromBookByGenreIdAndBookId(genreId, bookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(FIND_BOOK_DETAILS)
    public ResponseEntity<Book> displayBookDetails(@RequestParam String title) {
        Book foundBookByTitle = bookService.findBookDetailsByTitle(title);
        return ResponseEntity.ok(foundBookByTitle);
    }

    @GetMapping(FIND_BOOKS_BY_KEYWORD)
    public ResponseEntity<List<Book>> displayBooksByKeyword(@RequestParam String keyWord) {
        List<Book> booksByKeyWord = bookService.findBooksByKeyWord(keyWord);
        return ResponseEntity.ok(booksByKeyWord);
    }

    @GetMapping(FIND_BOOKS_BY_PAGE)
    public ResponseEntity<Page<Book>> displayBooksByPage(@RequestParam Integer page) {
        Page<Book> booksByPage = bookService.findBooksByPage(page);
        return ResponseEntity.ok(booksByPage);
    }
}
