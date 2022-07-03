package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

import static edu.epam.bookshop.repository.SqlQuery.CHECK_IF_USER_REVIEWED_GIVEN_BOOK;

public interface BookReviewRepository extends JpaRepository<BookReview, Long> {

    @Transactional
    @Modifying
    @Query(CHECK_IF_USER_REVIEWED_GIVEN_BOOK)
    boolean isUserReviewedBookByBookIdAndUserId(Long bookId, Long userId);
}
