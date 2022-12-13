package edu.epam.bookshop.service;

import edu.epam.bookshop.dto.BookDto;
import edu.epam.bookshop.dto.LocalizedBookDto;
import edu.epam.bookshop.dto.LocalizedGenreDto;
import edu.epam.bookshop.entity.Author;
import edu.epam.bookshop.entity.Book;
import edu.epam.bookshop.entity.BookReview;
import edu.epam.bookshop.entity.BookStatus;
import edu.epam.bookshop.entity.CoverType;
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

    void addBook(BookDto bookDto, MultipartFile bookImage, String languageName);

    void addLocalizationToExistingBook(BookDto bookToTranslate, MultipartFile localizedImage, String languageName);

    boolean updateBookInfo(BookDto bookToUpdate, MultipartFile newBookImage);

    boolean bookExistsById(Long bookId);

    boolean localizedBookExistsById(Long localizedBookId);

    Book findBookById(Long bookId);

    LocalizedBookDto findLocalizedBookDtoByBookIdAndLanguageName(Long bookId, String languageName);

    BookDto findBookDetailsByBookIdAndLanguage(Long bookId, String languageName);

    Page<LocalizedBook> findAllLocalizedBooksByLanguageAndPageNumber(String languageName, Integer pageNumber);

    List<BookDto> findTop15BooksByLanguageAndAverageScoreGreaterThan(String languageName, Double score);

    List<Book> findBooksByScoreGreaterThan(Double score);

    List<LocalizedBook> findLocalizedBooksByKeywordAndLanguageNameLimit6(String keyword, String languageName);

    Page<LocalizedBook> findLocalizedBooksByKeywordAndPageNumberAndLanguage(String keyWord, Integer pageNumber, String languageName);

    Page<LocalizedBook> findLocalizedBooksByYearAndPageNumberAndLanguage(Integer year, Integer pageNumber, String languageName);

    Page<Book> findBooksByLocalizedGenreTitleAndPageNumber(String genreTitle, Integer pageNumber);

    Page<Book> findBooksByPageHavingAverageScoreGreaterThan(Double score, Integer pageNumber);

    Page<Book> findBooksByPageAndShelveIdAndBookStatus(Integer pageNumber, Long shelveId, String bookStatus);

    Integer findNumberOfBooksWithAverageScoreGreaterThan(Double score);

    void addGenreByTitleAndLanguage(String genreTitle, String language);

    void addLocalizationToExistingGenre(String localizedGenreTitle, Long genreId, String languageName);

    void updateLocalizedGenreByGenreIdAndLanguageName(String newGenreTitle, Long genreId, String languageName);

    void deleteGenreById(Long genreId);

    void deleteLocalizedGenreById(Long localizedGenreId);

    boolean localizedGenreExistsByTitleAndLanguage(String genreTitle, Language language);

    Genre findGenreByGenreId(Long genreId);

    Page<LocalizedGenre> findLocalizedGenresByLanguageNameAndPageNumber(String languageName, int pageNumber);

    List<LocalizedGenre> findLocalizedGenresByKeywordAndLanguageName(String keyword, String languageName);

    List<LocalizedGenreDto> findLocalizedGenresByBookIdAndLanguage(Long bookId, String language);

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

    List<CoverType> findAllCoverTypes();
}
