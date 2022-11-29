package edu.epam.bookshop.service;

import edu.epam.bookshop.entity.Author;
import edu.epam.bookshop.entity.Book;
import edu.epam.bookshop.entity.BookReview;
import edu.epam.bookshop.entity.BookStatus;
import edu.epam.bookshop.entity.Genre;
import edu.epam.bookshop.entity.Language;
import edu.epam.bookshop.entity.LocalizedBook;
import edu.epam.bookshop.entity.LocalizedGenre;
import edu.epam.bookshop.entity.Publisher;
import edu.epam.bookshop.entity.ShelveBook;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {

    void addBook(Book book, LocalizedBook localizedBook, MultipartFile bookImage, String languageName);

    void addLocalizationToExistingBook(LocalizedBook existingLocalizedBook, MultipartFile localizedImage, String languageName);

    boolean updateBookInfo(Book book, LocalizedBook localizedBook, MultipartFile newBookImage);

    boolean bookExistsById(Long bookId);

    Book findBookDetailsByTitle(String bookTitle);

    Book findBookByLocalizedBookTitle(String title);

    Book findBookById(Long bookId);

    LocalizedBook findLocalizedBookDetailsByTitleAndLanguage(String title, String languageName);

    Page<LocalizedBook> findAllLocalizedBooksByLanguageAndPageNumber(String languageName, int pageNumber);

    List<Book> findBooksByKeyWord(String keyWord);

    List<LocalizedBook> findTop15LocalizedBooksByLanguageNameHavingAverageScoreGreaterThan(String languageName, Double score);

    Page<Book> findBooksByKeyWordAndPageNumber(String keyWord, Integer pageNumber);

    Page<Book> findBooksByPage(Integer page);

    Page<Book> findBooksByYearAndPageNumber(Integer year, Integer pageNumber);

    Page<Book> findBooksByLocalizedGenreTitleAndPageNumber(String genreTitle, Integer pageNumber);

    Page<Book> findBooksByPageHavingAverageScoreGreaterThan(Double score, Integer pageNumber);

    Page<Book> findBooksByPageAndShelveIdAndBookStatus(Integer pageNumber, Long shelveId, String bookStatus);

    Integer findNumberOfBooksWithAverageScoreGreaterThan(Double score);

    void addGenre(Genre genre);

    void addGGG(List<LocalizedGenre> localizedGenres);

    void addGenreToBook(Long genreId, Long bookId);

    void removeGenreFromBook(Long genreId, Long bookId);

    void deleteGenreById(Long genreId);

    void updateGenreTitles(Genre genre);

    boolean isGenreExistsByTitle(String genreTitle);

    Page<Genre> findGenresByPage(int page);

    List<Genre> findGenresByKeyword(String keyWord);

    List<Genre> findDistinctGenresForAuthorByAuthorId(Long authorId);

    List<Genre> findAllGenres();

    void addPublisher(Publisher publisher, MultipartFile image);

    void updatePublisherInfo(Publisher publisher, MultipartFile image);

    void deletePublisherById(Long publisherId);

    void addPublisherToBook(Long bookId, Long publisherId);

    void removePublisherFromBook(Long bookId, Long publisherId);

    boolean isPublisherExistsByName(String publisherName);

    Publisher findPublisherInfoByName(String publisherName);

    List<Publisher> findPublishersByKeyword(String keyWord);

    Page<Publisher> findPublishersByPage(int page);

    List<Publisher> findAllPublishers();

    List<Publisher> findPublishersByBookId(Long bookId);

    void addAuthor(Author author, MultipartFile image);

    void addAuthorToBook(Long bookId, Long authorId);

    void removeAuthorFromBook(Long authorId, Long bookId);

    void updateAuthorInfo(Author author, MultipartFile image);

    void deleteAuthorById(Long authorId);

    Author findAuthorInfoByAuthorId(Long authorId);

    List<Author> findAuthorsByBookId(Long bookId);

    List<Author> findAuthorsByKeyword(String keyWord);

    List<Author> findAllAuthors();

    Page<Author> findAuthorsByPage(int page);

    List<Integer> findExistingYearsInBooks();

    void addReviewToBook(BookReview bookReview, Long bookId, Long userId);

    void editBookReview(String newText, Double newScore, Long userId, Long reviewId);

    void removeReviewFromBookByReviewIdAndUserId(Long reviewId, Long userId);

    boolean checkIfUserAlreadyReviewedGivenBook(Long bookId, Long userId);

    Double findAverageBookReviewScoreByBookId(Long bookId);

    Double findBookScoreOfUser(Long userId, Long bookId);

    Page<BookReview> findBookReviewsByBookIdAndPageNumber(Long bookId, Integer pageNumber);

    void createBookShelveByUsername(String userName);

    void addBookToShelve(Long shelveId, Long bookId, BookStatus bookStatus);

    BookStatus findBookStatusOnBookShelve(Long shelveId, Long bookId);

    List<ShelveBook> findShelveBooks(Long shelveId, BookStatus bookStatus);

    Language findLanguageByName(String name);
}
