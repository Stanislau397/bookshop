package edu.epam.bookshop.controller;

import edu.epam.bookshop.entity.BookStatus;
import edu.epam.bookshop.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_BOOK_TO_SHELVE;
import static edu.epam.bookshop.controller.constant.PostMappingURN.CREATE_SHELVE_FOR_USER;

@RestController
@AllArgsConstructor
public class BookShelveController {

    private BookService bookService;

    @PostMapping(CREATE_SHELVE_FOR_USER)
    public ResponseEntity<Void> createShelve(String userName) {
        bookService.createBookShelveByUsername(userName);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ADD_BOOK_TO_SHELVE)
    public ResponseEntity<Void> saveBookToUserShelve(Long shelveId, Long bookId, BookStatus bookStatus) {
        bookService.addBookToShelve(shelveId, bookId, bookStatus);
        return ResponseEntity.ok().build();
    }
}
