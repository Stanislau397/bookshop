package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.BookShelve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static edu.epam.bookshop.repository.SqlQuery.CHECK_IF_SHELVE_EXISTS_BY_ID;
import static edu.epam.bookshop.repository.SqlQuery.INSERT_BOOK_TO_SHELVE;

@Repository
public interface BookShelveRepository extends JpaRepository<BookShelve, Long> {

    @Transactional
    @Modifying
    @Query(value = INSERT_BOOK_TO_SHELVE, nativeQuery = true)
    void insertBookToShelveByShelveIdAndBookIdAndBookStatus(Long shelveId, Long bookId, String bookStatus);

    @Query(value = CHECK_IF_SHELVE_EXISTS_BY_ID)
    boolean existsByShelveId(Long shelveId);
}
