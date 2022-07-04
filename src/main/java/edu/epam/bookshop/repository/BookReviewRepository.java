package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static edu.epam.bookshop.repository.SqlQuery.CHECK_IF_USER_REVIEWED_GIVEN_BOOK;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {

    @Transactional
    @Modifying
    @Query(
            value = CHECK_IF_USER_REVIEWED_GIVEN_BOOK,
            nativeQuery = true)
    boolean isUserReviewedBookByBookIdAndUserId(Long bookId, Long userId);
}
