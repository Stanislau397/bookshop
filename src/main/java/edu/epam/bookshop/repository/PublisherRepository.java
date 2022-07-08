package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static edu.epam.bookshop.repository.SqlQuery.SELECT_PUBLISHERS_BY_BOOK_ID;
import static edu.epam.bookshop.repository.SqlQuery.UPDATE_PUBLISHER_INFO_BY_ID;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    @Transactional
    @Modifying
    @Query(value = UPDATE_PUBLISHER_INFO_BY_ID)
    void updateInfoById(String publisherName, String description, String imagePath, Long publisherId);

    boolean existsByName(String publisherName);

    Optional<Publisher> findByName(String publisherName);

    @Query(SELECT_PUBLISHERS_BY_BOOK_ID)
    List<Publisher> findByBookId(Long bookId);
}
