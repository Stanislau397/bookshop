package edu.epam.bookshop.controller;

import edu.epam.bookshop.entity.Author;
import edu.epam.bookshop.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_ALL_AUTHORS_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_AUTHORS_BY_BOOK_ID_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_AUTHOR_INFO_BY_ID_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_AUTHORS_BY_KEYWORD_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_AUTHORS_BY_PAGE_URN;

import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_AUTHOR_TO_BOOK_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_AUTHOR_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.DELETE_AUTHOR_FROM_BOOK;
import static edu.epam.bookshop.controller.constant.PostMappingURN.UPDATE_AUTHOR_INFO_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.DELETE_AUTHOR_URN;

@RestController
@AllArgsConstructor
public class AuthorController {

    private final BookService bookService;

    @PostMapping(ADD_AUTHOR_URN)
    public ResponseEntity<Void> saveAuthor(Author author, MultipartFile image) {
        bookService.addAuthor(author, image);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ADD_AUTHOR_TO_BOOK_URN)
    public ResponseEntity<Void> addAuthorToBook(Long authorId, Long bookId) {
        bookService.addAuthorToBook(bookId, authorId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(UPDATE_AUTHOR_INFO_URN)
    public ResponseEntity<Void> changeAuthorInfo(Author author, MultipartFile image) {
        bookService.updateAuthorInfo(author, image);
        return ResponseEntity.ok().build();
    }

    @PostMapping(DELETE_AUTHOR_URN)
    public ResponseEntity<Void> removeAuthorById(Long authorId) {
        bookService.deleteAuthorById(authorId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(DELETE_AUTHOR_FROM_BOOK)
    public ResponseEntity<Void> removeAuthorFromBook(Long authorId, Long bookId) {
        bookService.removeAuthorFromBook(authorId, bookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(FIND_AUTHOR_INFO_BY_ID_URN)
    public ResponseEntity<Author> displayAuthorInfoById(Long authorId) {
        Author authorInfoById = bookService.findAuthorInfoByAuthorId(authorId);
        return ResponseEntity.ok(authorInfoById);
    }

    @GetMapping(FIND_AUTHORS_BY_BOOK_ID_URN)
    public ResponseEntity<List<Author>> displayAuthorInfoByBookId(Long bookId) {
        List<Author> authorsByBookId = bookService.findAuthorsByBookId(bookId);
        return ResponseEntity.ok(authorsByBookId);
    }

    @GetMapping(FIND_AUTHORS_BY_KEYWORD_URN)
    public ResponseEntity<List<Author>> displayAuthorsByKeyword(String keyWord) {
        List<Author> authorsByKeyword = bookService.findAuthorsByKeyword(keyWord);
        return ResponseEntity.ok(authorsByKeyword);
    }

    @GetMapping(FIND_AUTHORS_BY_PAGE_URN)
    public ResponseEntity<Page<Author>> displayAuthorsByPage(int page) {
        Page<Author> authorsByPage = bookService.findAuthorsByPage(page);
        return ResponseEntity.ok(authorsByPage);
    }

    @GetMapping(FIND_ALL_AUTHORS_URN)
    public ResponseEntity<List<Author>> displayAllAuthors() {
        List<Author> allAuthors = bookService.findAllAuthors();
        return ResponseEntity.ok(allAuthors);
    }
}
