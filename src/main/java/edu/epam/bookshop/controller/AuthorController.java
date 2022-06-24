package edu.epam.bookshop.controller;

import edu.epam.bookshop.entity.Author;
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

import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_AUTHOR_INFO_BY_ID;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_AUTHORS_BY_KEYWORD;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_AUTHORS_BY_PAGE;

import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_AUTHOR_URN;
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

    @PostMapping(UPDATE_AUTHOR_INFO_URN)
    public ResponseEntity<Void> changeAuthorInfo(Author author, MultipartFile image) {
        bookService.updateAuthorInfo(author, image);
        return ResponseEntity.ok().build();
    }

    @PostMapping(DELETE_AUTHOR_URN)
    public ResponseEntity<Void> removeAuthorById(@RequestParam Long authorId) {
        bookService.deleteAuthorById(authorId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(FIND_AUTHOR_INFO_BY_ID)
    public ResponseEntity<Author> displayAuthorInfoById(@RequestParam Long authorId) {
        Author authorInfoById = bookService.findAuthorInfoByAuthorId(authorId);
        return ResponseEntity.ok(authorInfoById);
    }

    @GetMapping(FIND_AUTHORS_BY_KEYWORD)
    public ResponseEntity<List<Author>> displayAuthorsByKeyword(@RequestParam String keyWord) {
        List<Author> authorsByKeyword = bookService.findAuthorsByKeyword(keyWord);
        return ResponseEntity.ok(authorsByKeyword);
    }

    @GetMapping(FIND_AUTHORS_BY_PAGE)
    public ResponseEntity<Page<Author>> displayAuthorsByPage(@RequestParam int page) {
        Page<Author> authorsByPage = bookService.findAuthorsByPage(page);
        return ResponseEntity.ok(authorsByPage);
    }
}
