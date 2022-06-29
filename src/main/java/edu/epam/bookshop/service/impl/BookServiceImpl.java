package edu.epam.bookshop.service.impl;

import edu.epam.bookshop.entity.Author;
import edu.epam.bookshop.entity.Book;
import edu.epam.bookshop.entity.Genre;
import edu.epam.bookshop.entity.Publisher;
import edu.epam.bookshop.exception.EntityAlreadyExistsException;
import edu.epam.bookshop.exception.EntityNotFoundException;
import edu.epam.bookshop.exception.InvalidInputException;
import edu.epam.bookshop.exception.NothingFoundException;
import edu.epam.bookshop.repository.AuthorRepository;
import edu.epam.bookshop.repository.BookRepository;
import edu.epam.bookshop.repository.GenreRepository;
import edu.epam.bookshop.repository.PublisherRepository;
import edu.epam.bookshop.service.BookService;
import edu.epam.bookshop.util.ImageUploaderUtil;
import edu.epam.bookshop.validator.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static edu.epam.bookshop.constant.ExceptionMessage.IMAGE_IS_NOT_VALID_MSG;
import static edu.epam.bookshop.constant.ExceptionMessage.NOTHING_WAS_FOUND_MSG;

import static edu.epam.bookshop.constant.ExceptionMessage.FIRST_NAME_IS_NOT_VALID_MSG;
import static edu.epam.bookshop.constant.ExceptionMessage.LAST_NAME_IS_NOT_VALID_MSG;
import static edu.epam.bookshop.constant.ExceptionMessage.AUTHOR_WITH_GIVEN_ID_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.AUTHOR_WITH_GIVEN_KEYWORD_NOT_FOUND;

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

import static edu.epam.bookshop.constant.ImageStoragePath.AUTHORS_LOCALHOST_PATH;
import static edu.epam.bookshop.constant.ImageStoragePath.AUTHORS_DIRECTORY_PATH;
import static edu.epam.bookshop.constant.ImageStoragePath.DEFAULT_AUTHOR_IMAGE_PATH;

import static edu.epam.bookshop.constant.ImageStoragePath.DEFAULT_PUBLISHER_IMAGE_PATH;
import static edu.epam.bookshop.constant.ImageStoragePath.PUBLISHER_LOCALHOST_PATH;
import static edu.epam.bookshop.constant.ImageStoragePath.PUBLISHERS_DIRECTORY_PATH;

import static edu.epam.bookshop.constant.ImageStoragePath.DEFAULT_BOOK_IMAGE_PATH;
import static edu.epam.bookshop.constant.ImageStoragePath.BOOK_DIRECTORY_PATH;
import static edu.epam.bookshop.constant.ImageStoragePath.BOOK_LOCALHOST_PATH;

@Slf4j
@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private static final int ELEMENTS_PER_PAGE = 6;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final GenreRepository genreRepository;

    private BookValidator bookValidator;
    private GenreValidator genreValidator;
    private PublisherValidator publisherValidator;
    private AuthorValidator authorValidator;
    private ImageValidator imageValidator;


    @Override
    public void addBook(Book book, MultipartFile bookImage) {
        if (!bookValidator.isBookTitleValid(book.getTitle())) {
            log.info(BOOK_TITLE_IS_NOT_VALID);
            throw new InvalidInputException(BOOK_TITLE_IS_NOT_VALID);
        }
        if (!bookValidator.isDescriptionValid(book.getDescription())) {
            log.info(BOOK_DESCRIPTION_IS_NOT_VALID);
            throw new InvalidInputException(BOOK_DESCRIPTION_IS_NOT_VALID);
        }
        if (!bookValidator.isIsbnValid(book.getIsbn())) {
            log.info(BOOK_ISBN_IS_NOT_VALID);
            throw new InvalidInputException(BOOK_ISBN_IS_NOT_VALID);
        }
        if (!bookValidator.isPagesValid(String.valueOf(book.getPages()))) {
            log.info(BOOK_PAGES_FIELD_IS_NOT_VALID);
            throw new InvalidInputException(BOOK_PAGES_FIELD_IS_NOT_VALID);

        }
        if (!bookValidator.isPriceValid(String.valueOf(book.getPrice()))) {
            log.info(BOOK_PRICE_IS_NOT_VALID);
            throw new InvalidInputException(BOOK_PRICE_IS_NOT_VALID);
        }
        String imagePathForBook = DEFAULT_BOOK_IMAGE_PATH;
        if (bookImage != null && bookImage.getOriginalFilename() != null) {
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
                .genres(book.getGenres())
                .authors(book.getAuthors())
                .build();
        bookRepository.save(bookToSave);
    }

    @Override
    public void addGenre(Genre genre) {
        String genreTitle = genre.getTitle();
        if (!genreValidator.isTitleValid(genreTitle)) {
            log.info(GENRE_TITLE_IS_NOT_VALID);
            throw new InvalidInputException(GENRE_TITLE_IS_NOT_VALID);
        }
        if (genreRepository.existsByTitle(genreTitle)) {
            log.info(
                    String.format(GENRE_WITH_GIVEN_TITLE_EXISTS, genreTitle));
            throw new EntityAlreadyExistsException(
                    String.format(GENRE_WITH_GIVEN_TITLE_EXISTS, genreTitle));
        }
        genreRepository.save(genre);
    }

    @Override
    public void deleteGenreById(Long genreId) {
        if (!genreRepository.existsById(genreId)) {
            log.info(
                    String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, genreId)
            );
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
            log.info(
                    String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, genreId)
            );
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
        if (image != null && image.getOriginalFilename() != null) {
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
        if (image != null && image.getOriginalFilename() != null) {
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
    public void addAuthor(Author author, MultipartFile image) {
        if (!authorValidator.isFirstnameValid(author.getFirstName())) {
            throw new InvalidInputException(FIRST_NAME_IS_NOT_VALID_MSG);
        }
        if (!authorValidator.isLastnameValid(author.getLastName())) {
            throw new InvalidInputException(LAST_NAME_IS_NOT_VALID_MSG);
        }
        String imagePath = DEFAULT_AUTHOR_IMAGE_PATH;
        if (image != null && image.getOriginalFilename() != null) {
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
        if (image != null && image.getOriginalFilename() != null) {
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
    public Page<Author> findAuthorsByPage(int page) {
        Pageable pageWithAuthors = PageRequest.of(page - 1, ELEMENTS_PER_PAGE);
        Page<Author> authorsByPage = authorRepository.findAll(pageWithAuthors);
        if (authorsByPage.isEmpty()) {
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return authorsByPage;
    }
}
