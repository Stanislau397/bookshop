package edu.epam.bookshop.service.impl;

import edu.epam.bookshop.entity.Author;
import edu.epam.bookshop.entity.Book;
import edu.epam.bookshop.entity.BookReview;
import edu.epam.bookshop.entity.BookShelve;
import edu.epam.bookshop.entity.BookStatus;
import edu.epam.bookshop.entity.CoverType;
import edu.epam.bookshop.entity.Genre;
import edu.epam.bookshop.entity.Publisher;
import edu.epam.bookshop.entity.ShelveBook;
import edu.epam.bookshop.entity.User;
import edu.epam.bookshop.exception.EntityAlreadyExistsException;
import edu.epam.bookshop.exception.EntityNotFoundException;
import edu.epam.bookshop.exception.InvalidInputException;
import edu.epam.bookshop.exception.NothingFoundException;
import edu.epam.bookshop.repository.AuthorRepository;
import edu.epam.bookshop.repository.BookRepository;
import edu.epam.bookshop.repository.BookReviewRepository;
import edu.epam.bookshop.repository.BookShelveRepository;
import edu.epam.bookshop.repository.GenreRepository;
import edu.epam.bookshop.repository.PublisherRepository;
import edu.epam.bookshop.repository.UserRepository;
import edu.epam.bookshop.service.BookService;
import edu.epam.bookshop.util.ImageUploaderUtil;
import edu.epam.bookshop.validator.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static edu.epam.bookshop.constant.ExceptionMessage.AUTHOR_ALREADY_EXISTS_IN_GIVEN_BOOK;
import static edu.epam.bookshop.constant.ExceptionMessage.AUTHORS_BY_GIVEN_BOOK_ID_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOKS_BY_GIVEN_YEAR_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOKS_WITH_GIVEN_GENRE_TITLE_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOKS_WITH_GIVEN_KEYWORD_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOKS_WITH_SCORE_GREATER_THAN_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_DOES_NOT_EXIST_FOR_AUTHOR;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_NOT_FOUND_ON_SHELVE;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_WITH_GIVEN_ID_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_WITH_GIVEN_TITLE_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.ENUM_NOT_FOUND_MSG;
import static edu.epam.bookshop.constant.ExceptionMessage.GENRES_BY_GIVEN_BOOK_ID_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.GENRE_ALREADY_EXISTS_FOR_GIVEN_BOOK;
import static edu.epam.bookshop.constant.ExceptionMessage.GENRE_NOT_FOUND_FOR_GIVEN_BOOK;
import static edu.epam.bookshop.constant.ExceptionMessage.IMAGE_IS_NOT_VALID_MSG;
import static edu.epam.bookshop.constant.ExceptionMessage.NOTHING_WAS_FOUND_MSG;

import static edu.epam.bookshop.constant.ExceptionMessage.FIRST_NAME_IS_NOT_VALID_MSG;
import static edu.epam.bookshop.constant.ExceptionMessage.LAST_NAME_IS_NOT_VALID_MSG;
import static edu.epam.bookshop.constant.ExceptionMessage.AUTHOR_WITH_GIVEN_ID_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.AUTHOR_WITH_GIVEN_KEYWORD_NOT_FOUND;

import static edu.epam.bookshop.constant.ExceptionMessage.PUBLISHERS_BY_GIVEN_BOOK_ID_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.PUBLISHER_ALREADY_EXISTS_FOR_GIVEN_BOOK;
import static edu.epam.bookshop.constant.ExceptionMessage.PUBLISHER_DOES_NOT_EXIST_FOR_BOOK;
import static edu.epam.bookshop.constant.ExceptionMessage.PUBLISHER_WITH_GIVEN_ID_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.PUBLISHER_WITH_GIVEN_NAME_ALREADY_EXISTS;
import static edu.epam.bookshop.constant.ExceptionMessage.PUBLISHER_WITH_GIVEN_NAME_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.PUBLISHER_DESCRIPTION_IS_INVALID;
import static edu.epam.bookshop.constant.ExceptionMessage.PUBLISHER_NAME_IS_INVALID;

import static edu.epam.bookshop.constant.ExceptionMessage.GENRE_TITLE_IS_NOT_VALID;
import static edu.epam.bookshop.constant.ExceptionMessage.GENRE_WITH_GIVEN_ID_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.GENRE_WITH_GIVEN_TITLE_EXISTS;
import static edu.epam.bookshop.constant.ExceptionMessage.GENRES_WITH_GIVEN_KEYWORD_NOT_FOUND;

import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_TITLE_IS_NOT_VALID;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_DESCRIPTION_IS_NOT_VALID;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_PAGES_FIELD_IS_NOT_VALID;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_PRICE_IS_NOT_VALID;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_ISBN_IS_NOT_VALID;

import static edu.epam.bookshop.constant.ExceptionMessage.REVIEWS_BY_GIVEN_BOOK_ID_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.SHELVE_WITH_GIVEN_ID_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.USER_WITH_GIVEN_ID_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.USER_WITH_GIVEN_NAME_NOT_FOUND_MSG;
import static edu.epam.bookshop.constant.ImageStoragePath.AUTHORS_LOCALHOST_PATH;
import static edu.epam.bookshop.constant.ImageStoragePath.AUTHORS_DIRECTORY_PATH;
import static edu.epam.bookshop.constant.ImageStoragePath.DEFAULT_AUTHOR_IMAGE_PATH;

import static edu.epam.bookshop.constant.ImageStoragePath.DEFAULT_PUBLISHER_IMAGE_PATH;
import static edu.epam.bookshop.constant.ImageStoragePath.PUBLISHER_LOCALHOST_PATH;
import static edu.epam.bookshop.constant.ImageStoragePath.PUBLISHERS_DIRECTORY_PATH;

import static edu.epam.bookshop.constant.ImageStoragePath.DEFAULT_BOOK_IMAGE_PATH;
import static edu.epam.bookshop.constant.ImageStoragePath.BOOK_DIRECTORY_PATH;
import static edu.epam.bookshop.constant.ImageStoragePath.BOOK_LOCALHOST_PATH;

import static edu.epam.bookshop.service.ItemsLimit.FIFTEEN;
import static java.util.Objects.nonNull;

@Slf4j
@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private static final int ELEMENTS_PER_PAGE = 6;
    private static final int BOOKS_PER_PAGE = 20;
    private final BookRepository bookRepository;
    private final BookReviewRepository reviewRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final GenreRepository genreRepository;
    private final UserRepository userRepository;
    private final BookShelveRepository shelveRepository;

    private BookValidator bookValidator;
    private GenreValidator genreValidator;
    private PublisherValidator publisherValidator;
    private AuthorValidator authorValidator;
    private ImageValidator imageValidator;


    @Override
    public void addBook(Book book, MultipartFile bookImage) { //todo test
        if (!bookValidator.isBookTitleValid(book.getTitle())) {
            log.info(BOOK_TITLE_IS_NOT_VALID);
            throw new InvalidInputException(BOOK_TITLE_IS_NOT_VALID);
        }
        if (!bookValidator.isDescriptionValid(book.getDescription())) {
            log.info(BOOK_DESCRIPTION_IS_NOT_VALID);
            throw new InvalidInputException(BOOK_DESCRIPTION_IS_NOT_VALID);
        }
        if (!bookValidator.isPagesValid(book.getPages())) {
            log.info(BOOK_PAGES_FIELD_IS_NOT_VALID);
            throw new InvalidInputException(BOOK_PAGES_FIELD_IS_NOT_VALID);

        }
        if (!bookValidator.isIsbnValid(book.getIsbn())) {
            log.info(BOOK_ISBN_IS_NOT_VALID);
            throw new InvalidInputException(BOOK_ISBN_IS_NOT_VALID);
        }
        if (!bookValidator.isPriceValid(book.getPrice())) {
            log.info(BOOK_PRICE_IS_NOT_VALID);
            throw new InvalidInputException(BOOK_PRICE_IS_NOT_VALID);
        }
        String imagePathForBook = DEFAULT_BOOK_IMAGE_PATH;
        if (nonNull(bookImage) && nonNull(bookImage.getOriginalFilename())) {
            String imageName = bookImage.getOriginalFilename();
            if (!imageValidator.isImageValid(imageName)) {
                throw new InvalidInputException(IMAGE_IS_NOT_VALID_MSG);
            }
            imagePathForBook = BOOK_LOCALHOST_PATH
                    .concat(ImageUploaderUtil.save(bookImage, BOOK_DIRECTORY_PATH));
        }

        Book bookToSave = Book.builder()
                .title(book.getTitle())
                .description(book.getDescription())
                .publishDate(book.getPublishDate())
                .coverType(book.getCoverType())
                .isbn(book.getIsbn())
                .imagePath(imagePathForBook)
                .pages(book.getPages())
                .price(book.getPrice())
                .build();
        bookRepository.save(bookToSave);
    }

    @Override
    public boolean updateBookInfo(Book book, MultipartFile newBookImage) { //todo test
        boolean isBookUpdated = false;
        Long bookId = book.getBookId();
        String title = book.getTitle();
        BigDecimal price = book.getPrice();
        String description = book.getDescription();
        Integer pages = book.getPages();
        String isbn = book.getIsbn();
        CoverType coverType = book.getCoverType();
        LocalDate publishDate = book.getPublishDate();
        String imagePath = book.getImagePath();

        if (!bookRepository.existsById(bookId)) {
            log.info(String.format(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId));
            throw new EntityNotFoundException(
                    String.format(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId)
            );
        }
        if (bookValidator.isBookDataValid(title, price, description, pages, isbn)) {

            if (nonNull(newBookImage) && nonNull(newBookImage.getOriginalFilename())) {
                String bookImageName = newBookImage.getOriginalFilename();
                if (!imageValidator.isImageValid(bookImageName)) {
                    throw new InvalidInputException(IMAGE_IS_NOT_VALID_MSG);
                }
                imagePath = BOOK_LOCALHOST_PATH
                        .concat(ImageUploaderUtil.save(newBookImage, BOOK_DIRECTORY_PATH));
            }
            bookRepository.updateInfoById(title, price, description, pages,
                    isbn, imagePath, coverType, publishDate, bookId);
            isBookUpdated = true;
        }
        return isBookUpdated;
    }

    @Override
    public Book findBookDetailsByTitle(String bookTitle) {
        return bookRepository.findByTitle(bookTitle)
                .orElseThrow(() -> {
                    log.info(BOOK_WITH_GIVEN_TITLE_NOT_FOUND, bookTitle);
                    return new EntityNotFoundException(
                            String.format(BOOK_WITH_GIVEN_TITLE_NOT_FOUND, bookTitle)
                    );
                });
    }

    @Override
    public List<Book> findBooksByKeyWord(String keyWord) {
        List<Book> booksByKeyWord = bookRepository.findAll()
                .stream()
                .filter(o -> o.getTitle().toLowerCase().contains(keyWord.toLowerCase()))
                .limit(ELEMENTS_PER_PAGE)
                .toList();
        if (booksByKeyWord.isEmpty()) {
            log.info(BOOKS_WITH_GIVEN_KEYWORD_NOT_FOUND, keyWord);
            throw new NothingFoundException(
                    String.format(BOOKS_WITH_GIVEN_KEYWORD_NOT_FOUND, keyWord)
            );
        }
        return booksByKeyWord;
    }

    @Override
    public List<Book> findTop15BooksHavingAverageScoreGreaterThan(Double score) { //todo test
        List<Book> booksWithHighScore =
                bookRepository.selectBooksHavingAverageScoreGreaterThan(score)
                        .stream()
                        .limit(FIFTEEN)
                        .toList();
        if (booksWithHighScore.isEmpty()) {
            log.info(BOOKS_WITH_SCORE_GREATER_THAN_NOT_FOUND, score);
            throw new NothingFoundException(
                    String.format(BOOKS_WITH_SCORE_GREATER_THAN_NOT_FOUND, score)
            );
        }
        return booksWithHighScore;
    }

    @Override
    public Page<Book> findBooksByKeyWordAndPageNumber(String keyWord, Integer pageNumber) { // todo test
        Pageable pageWithBooksByKeyWord = PageRequest.of(pageNumber - 1, BOOKS_PER_PAGE);
        Page<Book> booksByKeyWordAndPage =
                bookRepository.selectBooksByKeyWordAndPage(keyWord, pageWithBooksByKeyWord);
        if (booksByKeyWordAndPage.isEmpty()) {
            log.info(String.format(BOOKS_WITH_GIVEN_KEYWORD_NOT_FOUND, keyWord));
            throw new NothingFoundException(
                    String.format(BOOKS_WITH_GIVEN_KEYWORD_NOT_FOUND, keyWord)
            );
        }
        return booksByKeyWordAndPage;
    }

    @Override
    public Page<Book> findBooksByPage(Integer page) {
        Pageable pageWithBooks = PageRequest.of(page - 1, BOOKS_PER_PAGE);
        Page<Book> booksByPage = bookRepository.findAll(pageWithBooks);
        if (booksByPage.isEmpty()) {
            log.info(NOTHING_WAS_FOUND_MSG);
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return booksByPage;
    }

    @Override
    public Page<Book> findBooksByYearAndPageNumber(Integer year, Integer pageNumber) { //todo test
        Pageable pageWithBooksByYear = PageRequest.of(pageNumber - 1, BOOKS_PER_PAGE);
        Page<Book> booksByYear = bookRepository.selectBooksByYearAndPage(year, pageWithBooksByYear);
        if (booksByYear.isEmpty()) {
            log.info(String.format(BOOKS_BY_GIVEN_YEAR_NOT_FOUND, year));
            throw new NothingFoundException(
                    String.format(BOOKS_BY_GIVEN_YEAR_NOT_FOUND, year)
            );
        }
        return booksByYear;
    }

    @Override
    public Page<Book> findBooksByGenreTitleAndPageNumber(String genreTitle, Integer pageNumber) { //todo test
        Pageable pageWithBooksByGenreTitle = PageRequest.of(pageNumber - 1, BOOKS_PER_PAGE);
        Page<Book> booksByGenreTitle =
                bookRepository.selectBooksByGenreTitleAndPage(genreTitle, pageWithBooksByGenreTitle);
        if (booksByGenreTitle.isEmpty()) {
            log.info(String.format(BOOKS_WITH_GIVEN_GENRE_TITLE_NOT_FOUND, genreTitle));
            throw new NothingFoundException(
                    String.format(BOOKS_WITH_GIVEN_GENRE_TITLE_NOT_FOUND, genreTitle)
            );
        }
        return booksByGenreTitle;
    }

    @Override
    public Page<Book> findBooksByPageHavingAverageScoreGreaterThan(Double score, Integer pageNumber) { //todo test
        Pageable pageWithBooks = PageRequest.of(pageNumber - 1, FIFTEEN);
        Page<Book> booksWithAvgScoreGreaterThan =
                bookRepository.selectBooksByPageHavingAverageScoreGreaterThan(score, pageWithBooks);
        if (booksWithAvgScoreGreaterThan.isEmpty()) {
            log.info(BOOKS_WITH_SCORE_GREATER_THAN_NOT_FOUND, score);
            throw new NothingFoundException(
                    String.format(BOOKS_WITH_SCORE_GREATER_THAN_NOT_FOUND, score)
            );
        }
        return booksWithAvgScoreGreaterThan;
    }

    @Override
    public Page<Book> findBooksByPageAndShelveIdAndBookStatus(Integer pageNumber, Long shelveId,
                                                              String status) { //todo test
//        if (!shelveRepository.existsByShelveId(shelveId)) {
//            log.info(SHELVE_WITH_GIVEN_ID_NOT_FOUND, shelveId);
//            throw new EntityNotFoundException(
//                    String.format(SHELVE_WITH_GIVEN_ID_NOT_FOUND, shelveId)
//            );
//        }
//        if (!EnumUtils.isValidEnum(BookStatus.class, status)) {
//            log.info(ENUM_NOT_FOUND_MSG, status);
//            throw new EntityNotFoundException(String.format(ENUM_NOT_FOUND_MSG, status));
//        }
//        BookStatus bookStatus = BookStatus.valueOf(status);
//        Pageable pageWithBooks = PageRequest.of(pageNumber - 1, TWENTY_FIVE);
//        Page<Book> booksByShelveIdAndStatus =
//                bookRepository.selectBooksByPageAndShelveIdAndBookStatus(shelveId, bookStatus, pageWithBooks);
//        if (booksByShelveIdAndStatus.isEmpty()) {
//            log.info(NOTHING_WAS_FOUND_MSG);
//            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
//        }
//        return booksByShelveIdAndStatus;
        return null;
    }

    @Override
    public Integer findNumberOfBooksWithAverageScoreGreaterThan(Double score) { //todo test
        int numberOfBooks =
                bookRepository.selectBooksCountHavingAverageScoreGreaterThan(score);
        if (numberOfBooks == 0) {
            log.info(BOOKS_WITH_SCORE_GREATER_THAN_NOT_FOUND, score);
            throw new NothingFoundException(
                    String.format(BOOKS_WITH_SCORE_GREATER_THAN_NOT_FOUND, score)
            );
        }
        return numberOfBooks;
    }

    @Override
    public void addGenre(Genre genre) {
        String genreTitle = genre.getTitle();
        if (!genreValidator.isTitleValid(genreTitle)) {
            log.info(GENRE_TITLE_IS_NOT_VALID);
            throw new InvalidInputException(GENRE_TITLE_IS_NOT_VALID);
        }
        if (genreRepository.existsByTitle(genreTitle)) {
            log.info(String.format(GENRE_WITH_GIVEN_TITLE_EXISTS, genreTitle));
            throw new EntityAlreadyExistsException(
                    String.format(GENRE_WITH_GIVEN_TITLE_EXISTS, genreTitle));
        }
        genreRepository.save(genre);
    }

    @Override
    public void addGenreToBook(Long genreId, Long bookId) {
        if (!genreRepository.existsById(genreId)) {
            log.info(String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, genreId));
            throw new EntityNotFoundException(
                    String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, genreId)
            );
        }
        if (!bookRepository.existsById(bookId)) {
            log.info(String.format(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId));
            throw new EntityNotFoundException(
                    String.format(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId)
            );
        }
        if (genreRepository.genreExistsForBook(genreId, bookId)) {
            log.info(GENRE_ALREADY_EXISTS_FOR_GIVEN_BOOK);
            throw new EntityAlreadyExistsException(
                    GENRE_ALREADY_EXISTS_FOR_GIVEN_BOOK
            );
        }
        genreRepository.insertGenreToBookByGenreIdAndBookId(genreId, bookId);
    }

    @Override
    public void removeGenreFromBook(Long genreId, Long bookId) {
        if (!genreRepository.genreExistsForBook(genreId, bookId)) {
            log.info(String.format(GENRE_NOT_FOUND_FOR_GIVEN_BOOK, genreId, bookId));
            throw new EntityNotFoundException(
                    String.format(GENRE_NOT_FOUND_FOR_GIVEN_BOOK, genreId, bookId)
            );
        }
        genreRepository.deleteGenreFromBookByGenreIdAndBookId(genreId, bookId);
    }

    @Override
    public void deleteGenreById(Long genreId) {
        if (!genreRepository.existsById(genreId)) {
            log.info(String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, genreId));
            throw new EntityNotFoundException(
                    String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, genreId)
            );
        }
        genreRepository.deleteById(genreId);
    }

    @Override
    public void updateGenreTitle(Genre genre) {
        long genreId = genre.getGenreId();
        String updatedGenreTitle = genre.getTitle();

        if (!genreRepository.existsById(genreId)) {
            log.info(String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, genreId));
            throw new EntityNotFoundException(
                    String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, genreId)
            );
        }
        if (!genreValidator.isTitleValid(updatedGenreTitle)) {
            log.info(GENRE_TITLE_IS_NOT_VALID);
            throw new InvalidInputException(GENRE_TITLE_IS_NOT_VALID);
        }
        genreRepository.updateGenreTitleById(updatedGenreTitle, genreId);
    }

    @Override
    public boolean isGenreExistsByTitle(String genreTitle) {
        boolean genreExists = genreRepository.existsByTitle(genreTitle);
        if (genreExists) {
            log.info(
                    String.format(GENRE_WITH_GIVEN_TITLE_EXISTS, genreTitle));
        }
        return genreExists;
    }

    @Override
    public Page<Genre> findGenresByPage(int page) {
        Pageable pageWithGenres = PageRequest.of(page - 1, ELEMENTS_PER_PAGE);
        Page<Genre> genresByPage = genreRepository.findAll(pageWithGenres);
        if (genresByPage.isEmpty()) {
            log.info(NOTHING_WAS_FOUND_MSG);
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return genresByPage;
    }

    @Override
    public List<Genre> findGenresByBookId(Long bookId) { //todo test
        List<Genre> genresByBookId = genreRepository.findByBookId(bookId);
        if (genresByBookId.isEmpty()) {
            log.info(String.format(GENRES_BY_GIVEN_BOOK_ID_NOT_FOUND, bookId));
            throw new NothingFoundException(
                    String.format(GENRES_BY_GIVEN_BOOK_ID_NOT_FOUND, bookId)
            );
        }
        return genresByBookId;
    }

    @Override
    public List<Genre> findGenresByKeyword(String keyWord) {
        List<Genre> genresByKeyword = genreRepository.findAll()
                .stream()
                .filter(o -> o.getTitle().toLowerCase()
                        .contains(keyWord.toLowerCase()))
                .limit(ELEMENTS_PER_PAGE)
                .toList();

        if (genresByKeyword.isEmpty()) {
            log.info(
                    String.format(GENRES_WITH_GIVEN_KEYWORD_NOT_FOUND, keyWord));
            throw new NothingFoundException(
                    String.format(GENRES_WITH_GIVEN_KEYWORD_NOT_FOUND, keyWord));
        }
        return genresByKeyword;
    }

    @Override
    public List<Genre> findDistinctGenresForAuthorByAuthorId(Long authorId) { //todo test
        List<Genre> genresByAuthorId =
                genreRepository.selectDistinctGenresForAuthorByAuthorId(authorId);
        if (genresByAuthorId.isEmpty()) {
            log.info(NOTHING_WAS_FOUND_MSG);
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return genresByAuthorId;
    }

    @Override
    public List<Genre> findAllGenres() {
        List<Genre> allGenres = genreRepository.findAll();
        if (allGenres.isEmpty()) {
            log.info(NOTHING_WAS_FOUND_MSG);
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return allGenres;
    }

    @Override
    public void addPublisher(Publisher publisher, MultipartFile image) {
        if (publisherRepository.existsByName(publisher.getName())) {
            throw new EntityAlreadyExistsException(
                    String.format(PUBLISHER_WITH_GIVEN_NAME_ALREADY_EXISTS, publisher.getName())
            );
        }
        if (!publisherValidator.isNameValid(publisher.getName())) {
            throw new InvalidInputException(PUBLISHER_NAME_IS_INVALID);
        }
        if (!publisherValidator.isDescriptionValid(publisher.getDescription())) {
            throw new InvalidInputException(PUBLISHER_DESCRIPTION_IS_INVALID);
        }

        String imagePath = DEFAULT_PUBLISHER_IMAGE_PATH;
        if (nonNull(image) && nonNull(image.getOriginalFilename())) {
            String imageName = image.getOriginalFilename();
            if (!imageValidator.isImageValid(imageName)) {
                throw new InvalidInputException(IMAGE_IS_NOT_VALID_MSG);
            }
            imagePath = PUBLISHER_LOCALHOST_PATH
                    .concat(ImageUploaderUtil.save(image, PUBLISHERS_DIRECTORY_PATH));
        }
        Publisher publisherToSave = Publisher.builder()
                .name(publisher.getName())
                .description(publisher.getDescription())
                .imagePath(imagePath)
                .build();
        publisherRepository.save(publisherToSave);
    }

    @Override
    public void updatePublisherInfo(Publisher publisher, MultipartFile image) {
        String updatedPublisherName = publisher.getName();
        String updatedPublisherDescription = publisher.getDescription();
        String imagePath = publisher.getImagePath();

        if (!publisherValidator.isNameValid(updatedPublisherName)) {
            throw new InvalidInputException(PUBLISHER_NAME_IS_INVALID);
        }
        if (!publisherValidator.isDescriptionValid(updatedPublisherDescription)) {
            throw new InvalidInputException(PUBLISHER_DESCRIPTION_IS_INVALID);
        }
        if (nonNull(image) && nonNull(image.getOriginalFilename())) {
            String imageName = image.getOriginalFilename();
            if (!imageValidator.isImageValid(imageName)) {
                throw new InvalidInputException(IMAGE_IS_NOT_VALID_MSG);
            }
            imagePath = PUBLISHER_LOCALHOST_PATH
                    .concat(ImageUploaderUtil.save(image, PUBLISHERS_DIRECTORY_PATH));
        }
        publisherRepository.updateInfoById(
                updatedPublisherName,
                updatedPublisherDescription,
                imagePath,
                publisher.getPublisherId()
        );
    }

    @Override
    public void deletePublisherById(Long publisherId) {
        if (!publisherRepository.existsById(publisherId)) {
            throw new EntityNotFoundException(
                    String.format(PUBLISHER_WITH_GIVEN_ID_NOT_FOUND, publisherId)
            );
        }
        publisherRepository.deleteById(publisherId);
    }

    @Override
    public void addPublisherToBook(Long bookId, Long publisherId) { //todo test
        if (!bookRepository.existsById(bookId)) {
            log.info(String.format(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId));
            throw new EntityNotFoundException(
                    String.format(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId)
            );
        }
        if (!publisherRepository.existsById(publisherId)) {
            log.info(String.format(PUBLISHER_WITH_GIVEN_ID_NOT_FOUND, publisherId));
            throw new EntityNotFoundException(
                    String.format(PUBLISHER_WITH_GIVEN_ID_NOT_FOUND, publisherId)
            );
        }
        if (publisherRepository.isPublisherExistsForBook(bookId, publisherId)) {
            log.info(PUBLISHER_ALREADY_EXISTS_FOR_GIVEN_BOOK);
            throw new EntityAlreadyExistsException(PUBLISHER_ALREADY_EXISTS_FOR_GIVEN_BOOK);
        }
        publisherRepository.insertPublisherToBookByBookIdAndPublisherId(bookId, publisherId);
    }

    @Override
    public void removePublisherFromBook(Long bookId, Long publisherId) { //todo test
        if (!publisherRepository.isPublisherExistsForBook(bookId, publisherId)) {
            log.info(String.format(PUBLISHER_DOES_NOT_EXIST_FOR_BOOK, publisherId, bookId));
            throw new EntityNotFoundException(
                    String.format(PUBLISHER_DOES_NOT_EXIST_FOR_BOOK, publisherId, bookId)
            );
        }
        publisherRepository.deletePublisherFromBookByBookIdAndPublisherId(bookId, publisherId);
    }

    @Override
    public boolean isPublisherExistsByName(String publisherName) {
        boolean publisherExistsByName = publisherRepository.existsByName(publisherName);
        if (publisherExistsByName) {
            log.info(
                    String.format(PUBLISHER_WITH_GIVEN_NAME_ALREADY_EXISTS, publisherName)
            );
        }
        return publisherExistsByName;
    }

    @Override
    public Publisher findPublisherInfoByName(String publisherName) {
        return publisherRepository.findByName(publisherName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(PUBLISHER_WITH_GIVEN_NAME_NOT_FOUND, publisherName)
                ));
    }

    @Override
    public List<Publisher> findPublishersByKeyword(String keyWord) {
        List<Publisher> publishersByKeyword = publisherRepository.findAll()
                .stream()
                .filter(o -> o.getName().toLowerCase().contains(keyWord.toLowerCase()))
                .limit(ELEMENTS_PER_PAGE)
                .toList();
        if (publishersByKeyword.isEmpty()) {
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return publishersByKeyword;
    }

    @Override
    public Page<Publisher> findPublishersByPage(int page) {
        Pageable pageWithPublishers = PageRequest.of(page - 1, ELEMENTS_PER_PAGE);
        Page<Publisher> publishersByPage = publisherRepository.findAll(pageWithPublishers);
        if (publishersByPage.isEmpty()) {
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return publishersByPage;
    }

    @Override
    public List<Publisher> findAllPublishers() {
        List<Publisher> allPublishers = publisherRepository.findAll();
        if (allPublishers.isEmpty()) {
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return allPublishers;
    }

    @Override
    public List<Publisher> findPublishersByBookId(Long bookId) { //todo test
        List<Publisher> publishersByBookId = publisherRepository.findByBookId(bookId);
        if (publishersByBookId.isEmpty()) {
            log.info(String.format(PUBLISHERS_BY_GIVEN_BOOK_ID_NOT_FOUND, bookId));
            throw new NothingFoundException(
                    String.format(PUBLISHERS_BY_GIVEN_BOOK_ID_NOT_FOUND, bookId)
            );
        }
        return publishersByBookId;
    }

    @Override
    public void addAuthor(Author author, MultipartFile image) {
        if (!authorValidator.isFirstnameValid(author.getFirstName())) {
            throw new InvalidInputException(FIRST_NAME_IS_NOT_VALID_MSG);
        }
        if (!authorValidator.isLastnameValid(author.getLastName())) {
            throw new InvalidInputException(LAST_NAME_IS_NOT_VALID_MSG);
        }
        String imagePath = DEFAULT_AUTHOR_IMAGE_PATH;
        if (nonNull(image) && nonNull(image.getOriginalFilename())) {
            String imageName = image.getOriginalFilename();
            if (!imageValidator.isImageValid(imageName)) {
                throw new InvalidInputException(IMAGE_IS_NOT_VALID_MSG);
            }
            imagePath = AUTHORS_LOCALHOST_PATH
                    .concat(ImageUploaderUtil.save(image, AUTHORS_DIRECTORY_PATH));
        }
        Author authorToSave = Author.builder()
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .birthDate(author.getBirthDate())
                .biography(author.getBiography())
                .imagePath(imagePath)
                .build();
        authorRepository.save(authorToSave);
    }

    @Override
    public void addAuthorToBook(Long bookId, Long authorId) {
        if (!bookRepository.existsById(bookId)) {
            log.info(String.format(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId));
            throw new EntityNotFoundException(
                    String.format(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId)
            );
        }
        if (!authorRepository.existsById(authorId)) {
            log.info(String.format(AUTHOR_WITH_GIVEN_ID_NOT_FOUND, authorId));
            throw new EntityNotFoundException(
                    String.format(AUTHOR_WITH_GIVEN_ID_NOT_FOUND, authorId)
            );
        }
        if (bookRepository.bookExistsForAuthor(authorId, bookId)) {
            log.info(AUTHOR_ALREADY_EXISTS_IN_GIVEN_BOOK);
            throw new EntityAlreadyExistsException(
                    AUTHOR_ALREADY_EXISTS_IN_GIVEN_BOOK
            );
        }
        authorRepository.insertAuthorToBookByBookIdAndAuthorId(bookId, authorId);
    }

    @Override
    public void removeAuthorFromBook(Long authorId, Long bookId) { //todo test and controller
        if (!bookRepository.bookExistsForAuthor(authorId, bookId)) {
            log.info(String.format(BOOK_DOES_NOT_EXIST_FOR_AUTHOR, bookId, authorId));
            throw new EntityAlreadyExistsException(
                    String.format(BOOK_DOES_NOT_EXIST_FOR_AUTHOR, bookId, authorId)
            );
        }
        authorRepository.deleteAuthorFromBookByAuthorIdAndBookId(authorId, bookId);
    }

    @Override
    public void updateAuthorInfo(Author author, MultipartFile image) {
        if (!authorRepository.existsById(author.getAuthorId())) {
            throw new EntityNotFoundException(
                    String.format(AUTHOR_WITH_GIVEN_ID_NOT_FOUND, author.getAuthorId())
            );
        }
        if (!authorValidator.isFirstnameValid(author.getFirstName())) {
            throw new InvalidInputException(FIRST_NAME_IS_NOT_VALID_MSG);
        }
        if (!authorValidator.isLastnameValid(author.getLastName())) {
            throw new InvalidInputException(LAST_NAME_IS_NOT_VALID_MSG);
        }
        String imagePath = author.getImagePath();
        if (nonNull(image) && nonNull(image.getOriginalFilename())) {
            String imageName = image.getOriginalFilename();
            if (!imageValidator.isImageValid(imageName)) {
                throw new InvalidInputException(IMAGE_IS_NOT_VALID_MSG);
            }
            imagePath = AUTHORS_LOCALHOST_PATH
                    .concat(ImageUploaderUtil.save(image, AUTHORS_DIRECTORY_PATH));
        }
        authorRepository.updateInfoByAuthorFieldsAndId(
                author.getFirstName(),
                author.getLastName(),
                author.getBiography(),
                author.getBirthDate(),
                imagePath,
                author.getAuthorId());
    }

    @Override
    public void deleteAuthorById(Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new EntityNotFoundException(
                    String.format(AUTHOR_WITH_GIVEN_ID_NOT_FOUND, authorId)
            );
        }
        authorRepository.deleteById(authorId);
    }

    @Override
    public Author findAuthorInfoByAuthorId(Long authorId) {
        return authorRepository.findByAuthorId(authorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(AUTHOR_WITH_GIVEN_ID_NOT_FOUND, authorId)
                ));
    }

    @Override
    public List<Author> findAuthorsByBookId(Long bookId) { //todo test
        List<Author> authorsByBookId = authorRepository.findByBookId(bookId);
        if (authorsByBookId.isEmpty()) {
            log.info(String.format(AUTHORS_BY_GIVEN_BOOK_ID_NOT_FOUND, bookId));
            throw new EntityNotFoundException(
                    String.format(AUTHORS_BY_GIVEN_BOOK_ID_NOT_FOUND, bookId));
        }
        return authorsByBookId;
    }

    @Override
    public List<Author> findAuthorsByKeyword(String keyWord) {
        List<Author> authorsByKeyword = authorRepository.findAll()
                .stream()
                .filter(o -> o.getFirstName().toLowerCase().contains(keyWord.toLowerCase())
                        || o.getLastName().toLowerCase().contains(keyWord.toLowerCase()))
                .limit(ELEMENTS_PER_PAGE)
                .toList();
        if (authorsByKeyword.isEmpty()) {
            throw new NothingFoundException(
                    String.format(AUTHOR_WITH_GIVEN_KEYWORD_NOT_FOUND, keyWord)
            );
        }
        return authorsByKeyword;
    }

    @Override
    public List<Author> findAllAuthors() {
        List<Author> allAuthors = authorRepository.findAll();
        if (allAuthors.isEmpty()) {
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return allAuthors;
    }

    @Override
    public Page<Author> findAuthorsByPage(int page) {
        Pageable pageWithAuthors = PageRequest.of(page - 1, ELEMENTS_PER_PAGE);
        Page<Author> authorsByPage = authorRepository.findAll(pageWithAuthors);
        if (authorsByPage.isEmpty()) {
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return authorsByPage;
    }

    @Override
    public List<Integer> findExistingYearsInBooks() { //todo test
        List<Integer> existingYears =
                bookRepository.selectExistingYearsInBooksOrderedByYearAsc();
        if (existingYears.isEmpty()) {
            log.info(NOTHING_WAS_FOUND_MSG);
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return existingYears;
    }

    @Override
    public void addReviewToBook(BookReview review, Long bookId, Long userId) { //todo test
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(USER_WITH_GIVEN_ID_NOT_FOUND, userId))
                );
        Book bookToReview = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId))
                );
        String text = review.getReviewText();
        LocalDate dateNow = LocalDate.now();
        Double reviewScore = review.getScore();

        BookReview bookReviewToSave = BookReview.builder()
                .reviewedBook(bookToReview)
                .user(user)
                .reviewText(text)
                .publishDate(dateNow)
                .score(reviewScore)
                .build();
        reviewRepository.save(bookReviewToSave);
    }

    @Override
    public void editBookReview(String newText, Double newScore, Long userId, Long reviewId) { //todo test
        BookReview oldReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("not found"));
        User userFromRequest = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(USER_WITH_GIVEN_ID_NOT_FOUND, userId)
                ));
        User userInOldReview = oldReview.getUser();
        if (userFromRequest.equals(userInOldReview)) {
            reviewRepository.updateTextAndScoreByReviewId(newText, newScore, reviewId);
        }
    }

    @Override
    public void removeReviewFromBookByReviewIdAndUserId(Long reviewId, Long userId) { //todo test
        BookReview bookReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("not found"));
        User userFromRequest = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(USER_WITH_GIVEN_ID_NOT_FOUND, userId))
                );
        User userFromReview = bookReview.getUser();
        if (userFromRequest.equals(userFromReview)) {
            reviewRepository.delete(bookReview);
        }
    }

    @Override
    public boolean checkIfUserAlreadyReviewedGivenBook(Long bookId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(
                    String.format(USER_WITH_GIVEN_ID_NOT_FOUND, userId)
            );
        }
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException(
                    String.format(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId)
            );
        }
        return reviewRepository.isUserReviewedBookByBookIdAndUserId(bookId, userId);
    }

    @Override
    public Double findAverageBookReviewScoreByBookId(Long bookId) { //todo test
        Double averageBookReviewScore = reviewRepository.selectAverageScoreByBookId(bookId);
        if (!nonNull(averageBookReviewScore)) {
            throw new NothingFoundException(
                    String.format(REVIEWS_BY_GIVEN_BOOK_ID_NOT_FOUND, bookId)
            );
        }
        return BigDecimal.valueOf(averageBookReviewScore)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }

    @Override
    public Double findBookScoreOfUser(Long userId, Long bookId) { //todo test
        Double bookScoreOfUser = reviewRepository
                .selectBookScoreByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new NothingFoundException(NOTHING_WAS_FOUND_MSG));

        return BigDecimal.valueOf(bookScoreOfUser)
                .setScale(1, RoundingMode.HALF_EVEN)
                .doubleValue();
    }

    @Override
    public Page<BookReview> findBookReviewsByBookIdAndPageNumber(Long bookId, Integer pageNumber) { //todo test
        Pageable pageWithReviews = PageRequest.of(pageNumber - 1, ELEMENTS_PER_PAGE);
        Page<BookReview> bookReviewsByBookIdAndPage =
                reviewRepository.selectByBookIdAndPage(bookId, pageWithReviews);
        if (bookReviewsByBookIdAndPage.isEmpty()) {
            throw new NothingFoundException(
                    String.format(REVIEWS_BY_GIVEN_BOOK_ID_NOT_FOUND, bookId)
            );
        }
        return bookReviewsByBookIdAndPage;
    }

    @Override
    public void createBookShelveByUsername(String userName) {
        User userForShelve = userRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(USER_WITH_GIVEN_NAME_NOT_FOUND_MSG, userName)
                ));
        BookShelve bookShelveForUser = BookShelve.builder()
                .userRelatedToShelve(userForShelve)
                .build();
        shelveRepository.save(bookShelveForUser);
    }

    @Override
    public void addBookToShelve(Long shelveId, Long bookId, BookStatus bookStatus) {
        if (!shelveRepository.existsByShelveId(shelveId)) {
            log.info(SHELVE_WITH_GIVEN_ID_NOT_FOUND, shelveId);
            throw new EntityNotFoundException(
                    String.format(SHELVE_WITH_GIVEN_ID_NOT_FOUND, shelveId)
            );
        }
        if (!bookRepository.existsById(bookId)) {
            log.info(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId);
            throw new EntityNotFoundException(
                    String.format(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId)
            );
        }
        shelveRepository.insertBookToShelveByShelveIdAndBookIdAndBookStatus(
                shelveId,
                bookId,
                String.valueOf(bookStatus)
        );
    }

    @Override
    public BookStatus findBookStatusOnBookShelve(Long shelveId, Long bookId) { //todo test
        return shelveRepository.selectBookStatusByShelveIdAndBookId(shelveId, bookId)
                .orElseThrow(() -> new NothingFoundException(
                        String.format(BOOK_NOT_FOUND_ON_SHELVE, bookId, shelveId)
                ));
    }

    @Override
    public List<ShelveBook> findShelveBooks(Long shelveId, BookStatus bookStatus) {
        return shelveRepository.selectShelveBooks(shelveId, bookStatus);
    }
}
