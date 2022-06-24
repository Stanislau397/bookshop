package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    boolean existsByTitle(String genreTitle);
}
