package edu.epam.bookshop.service;

import edu.epam.bookshop.entity.Author;
import edu.epam.bookshop.entity.Genre;
import edu.epam.bookshop.entity.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {


    void addGenre(Genre genre);

    void deleteGenreById(Long genreId);

    void updateGenreById(Long genreId);

    boolean genreExistsByTitle(String genreTitle);

    Page<Genre> findGenresByPage(int page);

    List<Genre> findGenresByKeyword(String keyWord);

    List<Genre> findAllGenres();

    void addPublisher(Publisher publisher, MultipartFile image);

    void updatePublisherInfo(Publisher publisher, MultipartFile image);

    void deletePublisherById(Long publisherId);

    boolean publisherExistsByName(String publisherName);

    Publisher findPublisherInfoByName(String publisherName);

    List<Publisher> findPublishersByKeyword(String keyWord);

    Page<Publisher> findPublishersByPage(int page);

    void addAuthor(Author author, MultipartFile image);

    void updateAuthorInfo(Author author, MultipartFile image);

    void deleteAuthorById(Long authorId);

    Author findAuthorInfoByAuthorId(Long authorId);

    List<Author> findAuthorsByKeyword(String keyWord);

    Page<Author> findAuthorsByPage(int page);
}
