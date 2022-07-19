package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.BookReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static edu.epam.bookshop.repository.SqlQuery.CHECK_IF_USER_REVIEWED_GIVEN_BOOK;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_AVERAGE_SCORE_BY_BOOK_ID;
import static edu.epam.bookshop.repository.SqlQuery.COUNT_REVIEWS_BY_BOOK_ID;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_REVIEWS_BY_BOOK_ID;
import static edu.epam.bookshop.repository.SqlQuery.UPDATE_BOOK_REVIEW;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {

    @Query(CHECK_IF_USER_REVIEWED_GIVEN_BOOK)
    Boolean isUserReviewedBookByBookIdAndUserId(Long bookId, Long userId);

    @Transactional
    @Modifying
    @Query(UPDATE_BOOK_REVIEW)
    void updateByTextAndScoreAndReviewId(String updatedText, Double updatedScore, Long reviewId);

    @Query(SELECT_AVERAGE_SCORE_BY_BOOK_ID)
    Double selectAverageScoreByBookId(Long bookId);

    @Query(value = SELECT_REVIEWS_BY_BOOK_ID,
            countQuery = COUNT_REVIEWS_BY_BOOK_ID)
    Page<BookReview> selectByBookIdAndPage(Long bookId, Pageable page);
}
