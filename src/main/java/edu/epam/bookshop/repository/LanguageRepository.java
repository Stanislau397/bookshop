package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static edu.epam.bookshop.repository.SqlQuery.SELECT_LANGUAGE_BY_LOCALIZED_BOOK_TITLE;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

    Optional<Language> findByName(String name);

    boolean existsByName(String languageName);

    @Query(SELECT_LANGUAGE_BY_LOCALIZED_BOOK_TITLE)
    Optional<Language> selectLanguageByLocalizedBookTitle(String title);
}
