package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReviewRepository extends JpaRepository<BookReview, Long> {


}
