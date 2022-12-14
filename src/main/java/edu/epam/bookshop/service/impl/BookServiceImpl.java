package edu.epam.bookshop.service.impl;

import edu.epam.bookshop.dto.BookDto;
import edu.epam.bookshop.dto.LocalizedBookDto;
import edu.epam.bookshop.dto.LocalizedGenreDto;
import edu.epam.bookshop.dto.mapper.BookMapper;
import edu.epam.bookshop.dto.mapper.LocalizedBookMapper;
import edu.epam.bookshop.dto.mapper.LocalizedGenreMapper;
import edu.epam.bookshop.entity.Author;
import edu.epam.bookshop.entity.Book;
import edu.epam.bookshop.entity.BookReview;
import edu.epam.bookshop.entity.BookShelve;
import edu.epam.bookshop.entity.BookStatus;
import edu.epam.bookshop.entity.CoverType;
import edu.epam.bookshop.entity.Genre;
import edu.epam.bookshop.entity.Language;
import edu.epam.bookshop.entity.LocalizedBook;
import edu.epam.bookshop.entity.LocalizedGenre;
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
import edu.epam.bookshop.repository.LocalizedBookRepository;
import edu.epam.bookshop.repository.LocalizedGenreRepository;
import edu.epam.bookshop.repository.PublisherRepository;
import edu.epam.bookshop.repository.UserRepository;
import edu.epam.bookshop.service.BookService;
import edu.epam.bookshop.service.LanguageService;
import edu.epam.bookshop.util.ImageUploaderUtil;
import edu.epam.bookshop.validator.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static edu.epam.bookshop.constant.ExceptionMessage.AUTHOR_ALREADY_EXISTS_IN_GIVEN_BOOK;
import static edu.epam.bookshop.constant.ExceptionMessage.AUTHORS_BY_GIVEN_BOOK_ID_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOKS_WITH_GIVEN_GENRE_TITLE_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOKS_WITH_SCORE_GREATER_THAN_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_DETAILS_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_DOES_NOT_EXIST_FOR_AUTHOR;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_NOT_FOUND_ON_SHELVE;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_WITH_GIVEN_ID_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.GENRE_WITH_GIVEN_ID_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.GENRE_WITH_GIVEN_TITLE_AND_LANGUAGE_EXISTS;
import static edu.epam.bookshop.constant.ExceptionMessage.IMAGE_IS_NOT_VALID_MSG;
import static edu.epam.bookshop.constant.ExceptionMessage.LOCALIZED_BOOK_WITH_GIVEN_ID_NOT_FOUND;
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

import static edu.epam.bookshop.service.ItemsLimit.EIGHT;
import static edu.epam.bookshop.service.ItemsLimit.FIFTEEN;
import static edu.epam.bookshop.service.ItemsLimit.SIX;
import static java.util.Objects.nonNull;

@Slf4j
@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private static final int ELEMENTS_PER_PAGE = 6;
    private static final int BOOKS_PER_PAGE = 10;

    private final BookRepository bookRepository;
    private final BookReviewRepository reviewRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final UserRepository userRepository;
    private final BookShelveRepository shelveRepository;
    private final LocalizedBookRepository localizedBookRepository;
    private final GenreRepository genreRepository;
    private final LocalizedGenreRepository localizedGenreRepository;

    private final LanguageService languageService;

    private BookMapper bookMapper;
    private LocalizedBookMapper localizedBookMapper;
    private LocalizedGenreMapper genreMapper;

    private PublisherValidator publisherValidator;
    private AuthorValidator authorValidator;
    private ImageValidator imageValidator;


    @Override
    public void addBook(BookDto bookDto, MultipartFile bookImage, String languageName) { //todo test
        Language givenLanguage = languageService.findLanguageByName(languageName);
        String imagePathForBook = DEFAULT_BOOK_IMAGE_PATH;
        if (nonNull(bookImage)) {
            imagePathForBook = BOOK_LOCALHOST_PATH
                    .concat(ImageUploaderUtil.save(bookImage, BOOK_DIRECTORY_PATH));
        }
        Book createBook = bookMapper.toBook(bookDto);
        Book savedBook = bookRepository.save(createBook);
        String title = bookDto.getLocalizedBook().getTitle();
        String description = bookDto.getLocalizedBook().getDescription();
        LocalizedBook localizedBookToSave = LocalizedBook.builder()
                .title(title)
                .description(description)
                .imagePath(imagePathForBook)
                .book(savedBook)
                .language(givenLanguage)
                .build();
        localizedBookRepository.save(localizedBookToSave);
    }

    @Override
    public void addLocalizationToExistingBook(BookDto bookToTranslate, MultipartFile localizedImage,
                                              String languageName) {
        Language givenLanguage = languageService.findLanguageByName(languageName);
        Book givenBook = findBookById(bookToTranslate.getBookId());
        String imagePathForBook = bookToTranslate.getLocalizedBook().getImagePath();
        String localizedTitle = bookToTranslate.getLocalizedBook().getTitle();
        String localizedDescription = bookToTranslate.getLocalizedBook().getDescription();
        if (nonNull(localizedImage)) {
            imagePathForBook = BOOK_LOCALHOST_PATH
                    .concat(ImageUploaderUtil.save(localizedImage, BOOK_DIRECTORY_PATH));
        }
        LocalizedBook localizedBookToSave = LocalizedBook.builder()
                .title(localizedTitle)
                .description(localizedDescription)
                .imagePath(imagePathForBook)
                .language(givenLanguage)
                .book(givenBook)
                .build();
        localizedBookRepository.save(localizedBookToSave);
    }

    @Override
    public boolean updateBookInfo(BookDto bookToUpdateDto, MultipartFile updatedImage) { //todo test
        boolean isBookUpdated = false;
        LocalizedBookDto localizedBookDto = bookToUpdateDto.getLocalizedBook();
        if (nonNull(updatedImage)) {
            String imagePathForBook = BOOK_LOCALHOST_PATH
                    .concat(ImageUploaderUtil.save(updatedImage, BOOK_DIRECTORY_PATH));
            localizedBookDto.setImagePath(imagePathForBook);
        }
        if (bookExistsById(bookToUpdateDto.getBookId())
                && localizedBookExistsById(localizedBookDto.getLocalizedBookId())) {
            LocalizedBook localizedBookToUpdate = localizedBookMapper.toLocalizedBook(localizedBookDto);
            Book bookToUpdated = bookMapper.toBook(bookToUpdateDto);
            bookRepository.updateBookInfoById(bookToUpdated);
            localizedBookRepository.updateLocalizedBookInfo(localizedBookToUpdate);
            isBookUpdated = true;
        }
        return isBookUpdated;
    }

    @Override
    public boolean bookExistsById(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException(
                    String.format(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId));
        }
        return true;
    }

    @Override
    public boolean localizedBookExistsById(Long localizedBookId) {
        if (!localizedBookRepository.existsById(localizedBookId)) {
            throw new EntityNotFoundException(
                    String.format(LOCALIZED_BOOK_WITH_GIVEN_ID_NOT_FOUND, localizedBookId));
        }
        return true;
    }

    @Override
    public Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId)
                ));
    }

    @Override
    public LocalizedBookDto findLocalizedBookDtoByBookIdAndLanguageName(Long bookId, String languageName) {
        Language language = languageService.findLanguageByName(languageName);
        LocalizedBookDto localizedBookDto = localizedBookRepository
                .selectByBookIdAndLanguageId(bookId, language.getLanguageId())
                .map(localizedBookMapper::toLocalizedBookDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(BOOK_DETAILS_NOT_FOUND, bookId, languageName)
                ));
        return localizedBookDto;
    }

    @Override
    public BookDto findBookDetailsByBookIdAndLanguage(Long bookId, String languageName) {
        BookDto bookDtoDetails = bookRepository.findById(bookId)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(BOOK_DETAILS_NOT_FOUND, bookId, languageName)
                ));
        LocalizedBookDto localizedBookDto = findLocalizedBookDtoByBookIdAndLanguageName(bookId, languageName);
        List<LocalizedGenreDto> localizedGenresDto = findLocalizedGenresByBookIdAndLanguage(bookId, languageName);
        bookDtoDetails.setLocalizedBook(localizedBookDto);
        bookDtoDetails.setLocalizedGenres(localizedGenresDto);
        return bookDtoDetails;
    }

    @Override
    public Page<BookDto> findBooksByLanguageNameAndPage(String languageName, Integer pageNumber) {
        Pageable pageWithLocalizedBooks = PageRequest.of(pageNumber - 1, SIX);
        Page<BookDto> booksByPage = bookRepository
                .findAll(pageWithLocalizedBooks)
                .map(bookMapper::toDto);
        if (booksByPage.isEmpty()) {
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        booksByPage.forEach(bookDto -> {
            Long bookId = bookDto.getBookId();
            LocalizedBookDto localizedBookFields =
                    findLocalizedBookDtoByBookIdAndLanguageName(bookId, languageName);
            bookDto.setLocalizedBook(localizedBookFields);
        });
        return booksByPage;
    }

    @Override
    public List<BookDto> findTop15BooksByLanguageAndAverageScoreGreaterThan(String languageName, Double score) {
        Language language = languageService.findLanguageByName(languageName);
        List<BookDto> top15BooksWithScoreGreaterThan = bookRepository
                .selectByScoreGreaterThan(score)
                .stream()
                .map(bookMapper::toDto)
                .limit(FIFTEEN)
                .toList();
        if (top15BooksWithScoreGreaterThan.isEmpty()) {
            throw new NothingFoundException(String.format(BOOKS_WITH_SCORE_GREATER_THAN_NOT_FOUND, score));
        }
        top15BooksWithScoreGreaterThan.forEach(book -> {
            LocalizedBookDto localizedBookDto =
                    findLocalizedBookDtoByBookIdAndLanguageName(book.getBookId(), language.getName());
            book.setLocalizedBook(localizedBookDto);
        });
        return top15BooksWithScoreGreaterThan;
    }

    @Override
    public List<BookDto> findBooksByKeywordAndLanguageNameLimit6(String keyword, String languageName) {
        List<BookDto> booksByKeyword = bookRepository
                .selectByKeyword(keyword)
                .stream()
                .map(bookMapper::toDto)
                .limit(SIX)
                .toList();
        if (booksByKeyword.isEmpty()) {
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        booksByKeyword.forEach(bookDto -> {
            Long bookId = bookDto.getBookId();
            LocalizedBookDto localizedBookDto =
                    findLocalizedBookDtoByBookIdAndLanguageName(bookId, languageName);
            bookDto.setLocalizedBook(localizedBookDto);
        });
        return booksByKeyword;
    }

    @Override
    public Page<BookDto> findBooksByKeywordAndPageNumberAndLanguageName(String keyWord, Integer pageNumber, String languageName) { // todo test
        Pageable pageWithBooksByKeyword = PageRequest.of(pageNumber - 1, BOOKS_PER_PAGE);
        Page<BookDto> booksByKeywordAndPageNumber = bookRepository
                .selectByKeywordAndPage(keyWord, pageWithBooksByKeyword)
                .map(bookMapper::toDto);
        if (booksByKeywordAndPageNumber.isEmpty()) {
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        booksByKeywordAndPageNumber.forEach(bookDto -> {
            Long bookId = bookDto.getBookId();
            LocalizedBookDto localizedBookDto =
                    findLocalizedBookDtoByBookIdAndLanguageName(bookId, languageName);
            bookDto.setLocalizedBook(localizedBookDto);
        });
        return booksByKeywordAndPageNumber;
    }

    @Override
    public Page<BookDto> findBooksByYearAndPageNumberAndLanguageName(Integer year, Integer pageNumber, String languageName) { //todo test
        Pageable pageWithBooksByYear = PageRequest.of(pageNumber - 1, BOOKS_PER_PAGE);
        Page<BookDto> booksByYearAndPage = bookRepository
                .selectByYearAndPage(year, pageWithBooksByYear)
                .map(bookMapper::toDto);
        if (booksByYearAndPage.isEmpty()) {
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        booksByYearAndPage.forEach(bookDto -> {
            Long bookId = bookDto.getBookId();
            LocalizedBookDto localizedBookDto =
                    findLocalizedBookDtoByBookIdAndLanguageName(bookId, languageName);
            bookDto.setLocalizedBook(localizedBookDto);
        });
        return booksByYearAndPage;
    }

    @Override
    public Page<BookDto> findBooksByGenreTitleAndPageNumberAndLanguageName(String genreTitle, Integer pageNumber,
                                                                           String languageName) { //todo test
        Pageable pageWithBooksByGenreTitle = PageRequest.of(pageNumber - 1, BOOKS_PER_PAGE);
        Page<BookDto> booksByGenreTitleAndPage = bookRepository
                .selectByGenreTitleAndPage(genreTitle, pageWithBooksByGenreTitle)
                .map(bookMapper::toDto);
        if (booksByGenreTitleAndPage.isEmpty()) {
            throw new NothingFoundException(
                    String.format(BOOKS_WITH_GIVEN_GENRE_TITLE_NOT_FOUND, genreTitle));
        }
        booksByGenreTitleAndPage.forEach(bookDto -> {
            Long bookId = bookDto.getBookId();
            LocalizedBookDto localizedBookDto =
                    findLocalizedBookDtoByBookIdAndLanguageName(bookId, languageName);
            bookDto.setLocalizedBook(localizedBookDto);
        });
        return booksByGenreTitleAndPage;
    }

    @Override
    public Page<BookDto> findBooksByPageHavingAverageScoreGreaterThan(Double score, Integer pageNumber, String languageName) { //todo test
        Pageable pageWithBooks = PageRequest.of(pageNumber - 1, FIFTEEN);
        Page<BookDto> booksWithAvgScoreGreaterThan = bookRepository
                .selectByPageAndScoreGreaterThan(pageWithBooks, score)
                .map(bookMapper::toDto);
        if (booksWithAvgScoreGreaterThan.isEmpty()) {
            throw new NothingFoundException(
                    String.format(BOOKS_WITH_SCORE_GREATER_THAN_NOT_FOUND, score));
        }
        booksWithAvgScoreGreaterThan.forEach(bookDto -> {
            Long bookId = bookDto.getBookId();
            LocalizedBookDto localizedBookDto =
                    findLocalizedBookDtoByBookIdAndLanguageName(bookId, languageName);
            bookDto.setLocalizedBook(localizedBookDto);
        });
        return booksWithAvgScoreGreaterThan;
    }

    @Override
    public Integer findNumberOfBooksWithAverageScoreGreaterThan(Double score) { //todo test
        return bookRepository.selectBooksCountHavingAverageScoreGreaterThan(score)
                .orElseThrow(() -> new NothingFoundException(
                        String.format(BOOKS_WITH_SCORE_GREATER_THAN_NOT_FOUND, score)
                ));
    }

    @Override
    public void addGenreByTitleAndLanguage(String genreTitle, String language) {
        Language selectedLanguage = languageService.findLanguageByName(language);
        boolean genreExistsByTitleAndLanguage = localizedGenreExistsByTitleAndLanguage(genreTitle, selectedLanguage);
        if (!genreExistsByTitleAndLanguage) {
            Genre genre = Genre.builder()
                    .build();
            Genre savedGenre = genreRepository.save(genre);
            LocalizedGenre localizedGenre = LocalizedGenre.builder()
                    .genre(savedGenre)
                    .title(genreTitle)
                    .language(selectedLanguage)
                    .build();
            localizedGenreRepository.save(localizedGenre);
        }
    }

    @Override
    public void addLocalizationToExistingGenre(String localizedGenreTitle, Long genreId, String languageName) {
        Language existingLanguage = languageService.findLanguageByName(languageName);
        Genre existingGenre = findGenreByGenreId(genreId);
        boolean localizedGenreExists = localizedGenreExistsByTitleAndLanguage(localizedGenreTitle, existingLanguage);
        if (!localizedGenreExists) {
            LocalizedGenre localizedGenreToAdd = LocalizedGenre.builder()
                    .title(localizedGenreTitle)
                    .genre(existingGenre)
                    .language(existingLanguage)
                    .build();
            localizedGenreRepository.save(localizedGenreToAdd);
        }
    }

    @Override
    public void updateLocalizedGenreByGenreIdAndLanguageName(String newGenreTitle, Long genreId, String languageName) {
        Language selectedLanguage = languageService.findLanguageByName(languageName);
        boolean localizedGenreExists = localizedGenreExistsByTitleAndLanguage(newGenreTitle, selectedLanguage);
        if (!localizedGenreExists) {
            long languageId = selectedLanguage.getLanguageId();
            localizedGenreRepository.updateByGenreIdAndLanguageId(newGenreTitle, genreId, languageId);
        }
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
    public void deleteLocalizedGenreById(Long localizedGenreId) {
        if (!localizedGenreRepository.existsById(localizedGenreId)) {
            log.info(String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, localizedGenreId));
            throw new EntityNotFoundException(
                    String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, localizedGenreId)
            );
        }
        localizedGenreRepository.deleteById(localizedGenreId);
    }

    @Override
    public boolean localizedGenreExistsByTitleAndLanguage(String genreTitle, Language language) {
        boolean genreExistsByTitleAndLanguage =
                localizedGenreRepository.existsByTitleAndLanguage(genreTitle, language);
        if (genreExistsByTitleAndLanguage) {
            log.info(
                    String.format(GENRE_WITH_GIVEN_TITLE_AND_LANGUAGE_EXISTS, genreTitle, language.getName()));
            throw new EntityAlreadyExistsException(
                    String.format(GENRE_WITH_GIVEN_TITLE_AND_LANGUAGE_EXISTS, genreTitle, language.getName())
            );
        }
        return genreExistsByTitleAndLanguage;
    }

    @Override
    public Genre findGenreByGenreId(Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> {
                    log.info(String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, genreId));
                    throw new EntityNotFoundException(
                            String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, genreId));
                });
    }

    @Override
    public Page<LocalizedGenre> findLocalizedGenresByLanguageNameAndPageNumber(String languageName, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, EIGHT);
        Language selectedLanguage = languageService.findLanguageByName(languageName);
        long languageId = selectedLanguage.getLanguageId();
        Page<LocalizedGenre> localizedGenres =
                genreRepository.selectLocalizedGenresByLanguageIdAndPage(languageId, pageable);
        if (localizedGenres.isEmpty()) {
            log.info(NOTHING_WAS_FOUND_MSG);
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return localizedGenres;
    }

    @Override
    public List<LocalizedGenre> findLocalizedGenresByKeywordAndLanguageName(String keyword, String languageName) {
        List<LocalizedGenre> localizedGenresByKeyWordAndLanguage =
                localizedGenreRepository.selectByKeywordAndLanguageName(keyword, languageName)
                        .stream()
                        .limit(EIGHT)
                        .collect(Collectors.toList());
        if (localizedGenresByKeyWordAndLanguage.isEmpty()) {
            log.info(NOTHING_WAS_FOUND_MSG);
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return localizedGenresByKeyWordAndLanguage;
    }

    @Override
    public List<LocalizedGenreDto> findLocalizedGenresByBookIdAndLanguage(Long bookId, String language) {
        List<LocalizedGenreDto> localizedGenresByBookIdAndLanguage = new ArrayList<>();
        boolean bookExistsById = bookExistsById(bookId);
        boolean languageExistsByName = languageService.languageExistsByName(language);
        Language givenLanguage = languageService.findLanguageByName(language);
        if (bookExistsById && languageExistsByName) {
            long languageId = givenLanguage.getLanguageId();
            localizedGenresByBookIdAndLanguage = localizedGenreRepository
                    .selectLocalizedGenresByBookIdAndLocale(bookId, languageId)
                    .stream()
                    .map(genreMapper::toDto)
                    .toList();
        }
        if (localizedGenresByBookIdAndLanguage.isEmpty()) {
            return Collections.emptyList();
        }
        return localizedGenresByBookIdAndLanguage;
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
            return Collections.emptyList();
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
                bookRepository.selectExistingYearsForBooksOrderedByYearAsc();
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

    @Override
    public Language findLanguageByName(String name) {
        return null;
    }

    @Override
    public List<CoverType> findAllCoverTypes() {
        List<CoverType> allCoverTypes = Arrays.asList(CoverType.values());
        if (allCoverTypes.isEmpty()) {
            return Collections.emptyList();
        }
        return allCoverTypes;
    }
}
