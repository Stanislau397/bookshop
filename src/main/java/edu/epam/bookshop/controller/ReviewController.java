package edu.epam.bookshop.controller;

import edu.epam.bookshop.entity.BookReview;
import edu.epam.bookshop.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static edu.epam.bookshop.controller.constant.GetMappingURN.CHECK_IF_USER_REVIEWED_GIVEN_BOOK;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_AVERAGE_BOOK_REVIEW_SCORE_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_BOOK_REVIEWS_BY_BOOK_ID_AND_PAGE_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_REVIEW_TO_BOOK_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.EDIT_REVIEW_URN;

@RestController
@AllArgsConstructor
public class ReviewController {

    private final BookService bookService;

    @PostMapping(ADD_REVIEW_TO_BOOK_URN)
    public ResponseEntity<Void> addReview(BookReview review, Long bookId, Long userId) {
        bookService.addReviewToBook(review, bookId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(EDIT_REVIEW_URN)
    public ResponseEntity<Void> editReview(String newText, Double newScore,
                                           Long userId, Long reviewId) {
        bookService.editBookReview(newText, newScore, userId, reviewId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(CHECK_IF_USER_REVIEWED_GIVEN_BOOK)
    public ResponseEntity<Boolean> isUserReviewedGivenBook(Long bookId, Long userId) {
        Boolean userReviewedGivenBook =
                bookService.checkIfUserAlreadyReviewedGivenBook(bookId, userId);
        return ResponseEntity.ok(userReviewedGivenBook);
    }

    @GetMapping(FIND_BOOK_REVIEWS_BY_BOOK_ID_AND_PAGE_URN)
    public ResponseEntity<Page<BookReview>> displayBookReviewsForBook(Long bookId, Integer pageNumber) {
        Page<BookReview> bookReviewsForBookPerPage =
                bookService.findBookReviewsByBookIdAndPageNumber(bookId, pageNumber);
        return ResponseEntity.ok(bookReviewsForBookPerPage);
    }

    @GetMapping(FIND_AVERAGE_BOOK_REVIEW_SCORE_URN)
    public ResponseEntity<Double> displayAverageBookReviewScore(Long bookId) {
        Double averageBookReviewScore = bookService.findAverageBookReviewScoreByBookId(bookId);
        return ResponseEntity.ok(averageBookReviewScore);
    }
}
