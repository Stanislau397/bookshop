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
import static edu.epam.bookshop.repository.SqlQuery.SELECT_LOCALIZED_BOOK_BY_BOOK_ID_AND_LANGUAGE_ID;
import static edu.epam.bookshop.repository.SqlQuery.UPDATE_LOCALIZED_BOOK_INFO_BY_LOCALIZED_BOOK_ID;

@Repository
public interface LocalizedBookRepository extends JpaRepository<LocalizedBook, Long> {

    @Transactional
    @Modifying
    @Query(UPDATE_LOCALIZED_BOOK_INFO_BY_LOCALIZED_BOOK_ID)
    void updateByLocalizedBookId(String updatedTitle, String updatedImagePath, String updatedDescription, Long localizedBookId);

    @Query(SELECT_LOCALIZED_BOOK_BY_BOOK_ID_AND_LANGUAGE_ID)
    Optional<LocalizedBook> selectLocalizedBookByBookIdAndLanguageId(String title, Long languageId);

    @Query(SELECT_ALL_LOCALIZED_BOOKS_BY_LANGUAGE_ID_AND_PAGE)
    Page<LocalizedBook> selectAllLocalizedBooksByLanguageIdAndPage(Long languageId, Pageable pageable);

    @Query(value = SELECT_LOCALIZED_BOOKS_BY_AVG_SCORE_GREATER_THAN)
    List<LocalizedBook> selectByLanguageIdAvgScoreGreaterThan(Long languageId, Double score);
}
