package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {


}
