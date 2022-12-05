package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.LocalizedBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static edu.epam.bookshop.repository.SqlQuery.SELECT_ALL_LOCALIZED_BOOKS_BY_LANGUAGE_ID_AND_PAGE;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_LOCALIZED_BOOKS_BY_AVG_SCORE_GREATER_THAN;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_LOCALIZED_BOOKS_BY_KEYWORD_AND_LANGUAGE_ID;
import static edu.epam.bookshop.repository.SqlQuery.SELECT_LOCALIZED_BOOK_BY_BOOK_ID_AND_LANGUAGE_ID;
import static edu.epam.bookshop.repository.SqlQuery.UPDATE_LOCALIZED_BOOK_INFO;

@Repository
public interface LocalizedBookRepository extends JpaRepository<LocalizedBook, Long> {

    @Transactional
    @Modifying
    @Query(UPDATE_LOCALIZED_BOOK_INFO)
    void updateLocalizedBookInfo(LocalizedBook updatedLocalizedBook);

    @Query(SELECT_LOCALIZED_BOOK_BY_BOOK_ID_AND_LANGUAGE_ID)
    Optional<LocalizedBook> selectLocalizedBookByBookIdAndLanguageId(Long bookId, Long languageId);

    @Query(SELECT_ALL_LOCALIZED_BOOKS_BY_LANGUAGE_ID_AND_PAGE)
    Page<LocalizedBook> selectAllLocalizedBooksByLanguageIdAndPage(Long languageId, Pageable pageable);

    @Query(SELECT_LOCALIZED_BOOKS_BY_KEYWORD_AND_LANGUAGE_ID)
    List<LocalizedBook> selectLocalizedBooksByKeywordAndLanguageId(String keyword, Long languageId);

    @Query(value = SELECT_LOCALIZED_BOOKS_BY_AVG_SCORE_GREATER_THAN)
    List<LocalizedBook> selectByLanguageIdAvgScoreGreaterThan(Long languageId, Double score);
}
