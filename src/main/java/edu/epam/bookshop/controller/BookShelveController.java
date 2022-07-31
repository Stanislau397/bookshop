package edu.epam.bookshop.controller;

import edu.epam.bookshop.entity.BookStatus;
import edu.epam.bookshop.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static edu.epam.bookshop.controller.constant.GetMappingURN.CHECK_IF_BOOK_EXISTS_IN_SHELVE;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_BOOK_STATUS_ON_SHELVE;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_NUMBER_OF_BOOKS_ON_SHELVE;
import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_BOOK_TO_SHELVE;
import static edu.epam.bookshop.controller.constant.PostMappingURN.CREATE_SHELVE_FOR_USER;
import static edu.epam.bookshop.controller.constant.PostMappingURN.UPDATE_BOOK_STATUS_ON_SHELVE;

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

    @PostMapping(UPDATE_BOOK_STATUS_ON_SHELVE)
    public ResponseEntity<Void> changeBookStatus(String bookStatus, Long bookId, Long shelveId) {
        bookService.changeBookStatusOnShelve(bookStatus, shelveId, bookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(CHECK_IF_BOOK_EXISTS_IN_SHELVE)
    public ResponseEntity<Boolean> isBookExistsInShelve(Long shelveId, Long bookId) {
        Boolean bookExistsInShelve = bookService.checkIfBookExistsInBookShelve(shelveId, bookId);
        return ResponseEntity.ok(bookExistsInShelve);
    }

    @GetMapping(FIND_NUMBER_OF_BOOKS_ON_SHELVE)
    public ResponseEntity<Integer> displayNumberOfBooksOnShelve(Long shelveId, String bookStatus) {
        Integer numberOfBooksByStatus = bookService.findNumberOfBooksOnBookShelve(shelveId, bookStatus);
        return ResponseEntity.ok(numberOfBooksByStatus);
    }

    @GetMapping(FIND_BOOK_STATUS_ON_SHELVE)
    public ResponseEntity<BookStatus> displayBookStatusOnShelve(Long shelveId, Long bookId) {
        BookStatus bookStatus = bookService.findBookStatusOnBookShelve(shelveId, bookId);
        return ResponseEntity.ok(bookStatus);
    }
}
