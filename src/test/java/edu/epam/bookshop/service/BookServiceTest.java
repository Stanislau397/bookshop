package edu.epam.bookshop.service;

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
import edu.epam.bookshop.repository.BookReviewRepository;
import edu.epam.bookshop.repository.GenreRepository;
import edu.epam.bookshop.repository.PublisherRepository;
import edu.epam.bookshop.repository.UserRepository;
import edu.epam.bookshop.service.impl.BookServiceImpl;
import edu.epam.bookshop.validator.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.epam.bookshop.constant.ExceptionMessage.*;
import static edu.epam.bookshop.constant.ImageStoragePath.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private BookValidator bookValidator;

    @Mock
    private AuthorValidator authorValidator;

    @Mock
    private ImageValidator imageValidator;

    @Mock
    private GenreValidator genreValidator;

    @Mock
    private PublisherValidator publisherValidator;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookReviewRepository reviewRepository;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(
                bookRepository,
                reviewRepository,
                authorRepository,
                publisherRepository,
                genreRepository,
                userRepository,
                bookValidator,
                genreValidator,
                publisherValidator,
                authorValidator,
                imageValidator);
    }

    @Test
    void willAddBookWithDefaultImage() {
        //given
        String title = "book";
        String description = "add";
        int pages = 123;
        String isbn = "123-123";
        Book book = Book.builder()
                .title(title)
                .description(description)
                .pages(pages)
                .isbn(isbn)
                .imagePath(DEFAULT_BOOK_IMAGE_PATH)
                .price(new BigDecimal("12.1"))
                .build();
        //when
        when(bookValidator.isBookTitleValid(title))
                .thenReturn(true);
        when(bookValidator.isDescriptionValid(description))
                .thenReturn(true);
        when(bookValidator.isPagesValid(pages))
                .thenReturn(true);
        when(bookValidator.isIsbnValid(isbn))
                .thenReturn(true);
        when(bookValidator.isPriceValid(new BigDecimal("12.1")))
                .thenReturn(true);
        bookService.addBook(book, null);
        //then
        verify(bookRepository, times(1)).save(book);
    }


    @Test
    void addBookWillThrowExceptionWhenTitleIsNotValid() {
        //given
        String title = "!book";
        Book book = Book.builder()
                .title(title)
                .build();
        //when
        when(bookValidator.isBookTitleValid(title))
                .thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.addBook(book, null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(BOOK_TITLE_IS_NOT_VALID);
    }

    @Test
    void addBookWillThrowExceptionWhenDescriptionIsNotValid() {
        //given
        String title = "book";
        String description = "add";
        Book book = Book.builder()
                .title(title)
                .description(description)
                .build();
        //when
        when(bookValidator.isBookTitleValid(title))
                .thenReturn(true);
        when(bookValidator.isDescriptionValid(description))
                .thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.addBook(book, null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(BOOK_DESCRIPTION_IS_NOT_VALID);
    }

    @Test
    void addBookWillThrowExceptionWhenPagesIsNotValid() {
        //given
        String title = "book";
        String description = "add";
        Book book = Book.builder()
                .title(title)
                .description(description)
                .build();
        //when
        when(bookValidator.isBookTitleValid(title))
                .thenReturn(true);
        when(bookValidator.isDescriptionValid(description))
                .thenReturn(true);
        when(bookValidator.isPagesValid(-123))
                .thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.addBook(book, null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(BOOK_PAGES_FIELD_IS_NOT_VALID);
    }

    @Test
    void addBookWillThrowExceptionWhenIsbnIsNotValid() {
        //given
        String title = "book";
        String description = "add";
        int pages = 123;
        String isbn = "123-123";
        Book book = Book.builder()
                .title(title)
                .description(description)
                .pages(pages)
                .isbn(isbn)
                .build();
        //when
        when(bookValidator.isBookTitleValid(title))
                .thenReturn(true);
        when(bookValidator.isDescriptionValid(description))
                .thenReturn(true);
        when(bookValidator.isPagesValid(pages))
                .thenReturn(true);
        when(bookValidator.isIsbnValid(isbn))
                .thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.addBook(book, null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(BOOK_ISBN_IS_NOT_VALID);
    }

    @Test
    void addBookWillThrowExceptionWhenPriceIsNotValid() {
        //given
        String title = "book";
        String description = "add";
        int pages = 123;
        String isbn = "123-123";
        Book book = Book.builder()
                .title(title)
                .description(description)
                .pages(pages)
                .isbn(isbn)
                .build();
        //when
        when(bookValidator.isBookTitleValid(title))
                .thenReturn(true);
        when(bookValidator.isDescriptionValid(description))
                .thenReturn(true);
        when(bookValidator.isPagesValid(pages))
                .thenReturn(true);
        when(bookValidator.isIsbnValid(isbn))
                .thenReturn(true);
        when(bookValidator.isPriceValid(new BigDecimal("po")))
                .thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.addBook(book, null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(BOOK_PRICE_IS_NOT_VALID);
    }

    @Test
    void willAddBookForAuthor() {
        //given
        long bookId = 1;
        long authorId = 1;
        //when
        when(bookRepository.existsById(bookId))
                .thenReturn(true);
        when(authorRepository.existsById(authorId))
                .thenReturn(true);
        when(bookRepository.bookExistsForAuthor(authorId, bookId))
                .thenReturn(false);
        bookService.addAuthorToBook(bookId, authorId);
        //then
        verify(authorRepository, times(1)).
                insertAuthorToBookByBookIdAndAuthorId(bookId, authorId);
    }

    @Test
    void addBookForAuthorWillThrowExceptionWhenBookIdIsInvalid() {
        //given
        long bookId = 1;
        //when
        when(bookRepository.existsById(bookId))
                .thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.addAuthorToBook(bookId, 1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(
                        String.format(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId)
                );
    }

    @Test
    void addBookForAuthorWillThrowExceptionWhenAuthorIdIsInvalid() {
        //given
        long bookId = 1;
        long authorId = 1;
        //when
        when(bookRepository.existsById(bookId))
                .thenReturn(true);
        when(authorRepository.existsById(authorId))
                .thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.addAuthorToBook(bookId, authorId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(
                        String.format(AUTHOR_WITH_GIVEN_ID_NOT_FOUND, authorId)
                );
    }

    @Test
    void addBookForAuthorWillThrowExceptionWhenBookAlreadyExistsForAuthor() {
        //given
        long bookId = 1;
        long authorId = 1;
        //when
        when(bookRepository.existsById(bookId))
                .thenReturn(true);
        when(authorRepository.existsById(authorId))
                .thenReturn(true);
        when(bookRepository.bookExistsForAuthor(authorId, bookId))
                .thenReturn(true);
        //then
        assertThatThrownBy(() -> bookService.addAuthorToBook(bookId, authorId))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContaining(AUTHOR_ALREADY_EXISTS_IN_GIVEN_BOOK);
    }

    @Test
    void willRemoveBookFromAuthor() {
        //given
        long authorId = 1;
        long bookId = 1;
        //when
        when(bookRepository.bookExistsForAuthor(authorId, bookId))
                .thenReturn(true);
        bookService.removeBookForAuthorByAuthorIdAndBookId(authorId, bookId);
        //then
        verify(bookRepository, times(1))
                .deleteBookFromAuthorByAuthorIdAndBookId(authorId, bookId);
    }

    @Test
    void removeBookFromAuthorWillThrowExceptionWhenAuthorDoesNotExistForBook() {
        //given
        long authorId = 1;
        long bookId = 1;
        //when
        when(bookRepository.bookExistsForAuthor(authorId, bookId))
                .thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.removeBookForAuthorByAuthorIdAndBookId(authorId, bookId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(
                        String.format(BOOK_DOES_NOT_EXIST_FOR_AUTHOR, bookId, authorId)
                );
    }

    @Test
    void willFindBookDetails() {
        //given
        String bookTitle = "Harry";
        Book book = Book.builder()
                .title(bookTitle)
                .build();
        //when
        when(bookRepository.findByTitle(bookTitle))
                .thenReturn(Optional.of(book));
        Book expectedBook = bookService.findBookDetailsByTitle(bookTitle);
        //then
        assertThat(book).isEqualTo(expectedBook);
    }

    @Test
    void findBookDetailsWillThrowExceptionWhenNothingFound() {
        //given
        String bookTitle = "Harry";
        //when
        when(bookRepository.findByTitle(bookTitle))
                .thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> bookService.findBookDetailsByTitle(bookTitle))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(
                        String.format(BOOK_WITH_GIVEN_TITLE_NOT_FOUND, bookTitle)
                );
    }

    @Test
    void willFindBooksByKeyWord() {
        //given
        String keyWordForBooks = "Harry";
        Book firstBook = Book.builder()
                .title("Harry 1")
                .build();
        Book secondBook = Book.builder()
                .title("Harry 2")
                .build();
        List<Book> books = List.of(firstBook, secondBook);
        //when
        when(bookRepository.findAll())
                .thenReturn(books);
        List<Book> expectedBooks = bookService.findBooksByKeyWord(keyWordForBooks);
        //then
        assertThat(expectedBooks).isNotNull();
        assertThat(expectedBooks.size()).isEqualTo(2);
    }

    @Test
    void findBooksByKeyWordWillThrowExceptionWhenNothingFound() {
        //given
        String keyWordForBooks = "Fantasy";
        Book firstBook = Book.builder()
                .title("Harry 1")
                .build();
        Book secondBook = Book.builder()
                .title("Harry 2")
                .build();
        List<Book> books = List.of(firstBook, secondBook);
        //when
        when(bookRepository.findAll())
                .thenReturn(books);
        //then
        assertThatThrownBy(() -> bookService.findBooksByKeyWord(keyWordForBooks))
                .isInstanceOf(NothingFoundException.class)
                .hasMessageContaining(
                        String.format(BOOKS_WITH_GIVEN_KEYWORD_NOT_FOUND, keyWordForBooks)
                );
    }

    @Test
    void willFindBooksByPage() {
        //given
        Pageable pageWithBooks = PageRequest.of(0, 20);
        Book book = Book.builder()
                .build();
        List<Book> books = List.of(book);
        Page<Book> booksByPage = new PageImpl<>(books);
        //when
        when(bookRepository.findAll(pageWithBooks))
                .thenReturn(booksByPage);
        Page<Book> expectedBooksByPage = bookService.findBooksByPage(1);
        //then
        assertThat(booksByPage).isEqualTo(expectedBooksByPage);
    }

    @Test
    void findBooksByPageWillThrowExceptionWhenNothingFound() {
        //given
        Pageable pageWithBooks = PageRequest.of(0, 20);
        Page<Book> booksByPage = new PageImpl<>(new ArrayList<>());
        //when
        when(bookRepository.findAll(pageWithBooks))
                .thenReturn(booksByPage);
        //then
        assertThatThrownBy(() -> bookService.findBooksByPage(1))
                .isInstanceOf(NothingFoundException.class)
                .hasMessageContaining(NOTHING_WAS_FOUND_MSG);
    }

    @Test
    void willAddGenre() {
        //given
        String genreTitle = "Sci-Fi";
        Genre genre = Genre.builder()
                .title(genreTitle)
                .build();
        //when
        when(genreValidator.isTitleValid(genreTitle)).thenReturn(true);
        when(genreRepository.existsByTitle(genreTitle)).thenReturn(false);
        bookService.addGenre(genre);
        //then
        verify(genreRepository, times(1)).save(genre);
    }

    @Test
    void addGenreWillThrowExceptionWhenTitleIsNotValid() {
        //given
        String invalidGenreTitle = "1Sci-Fi";
        Genre genre = Genre.builder()
                .title(invalidGenreTitle)
                .build();
        //when
        when(genreValidator.isTitleValid(invalidGenreTitle)).thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.addGenre(genre))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(GENRE_TITLE_IS_NOT_VALID);
    }

    @Test
    void addGenreWillThrowExceptionWhenGenreWithGivenTitleExists() {
        //given
        String existingGenreTitle = "Sci-Fi";
        Genre genre = Genre.builder()
                .title(existingGenreTitle)
                .build();
        //when
        when(genreValidator.isTitleValid(existingGenreTitle)).thenReturn(true);
        when(genreRepository.existsByTitle(existingGenreTitle)).thenReturn(true);
        //then
        assertThatThrownBy(() -> bookService.addGenre(genre))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContaining(
                        String.format(GENRE_WITH_GIVEN_TITLE_EXISTS, existingGenreTitle)
                );
    }

    @Test
    void willAddGenreToBook() {
        //given
        long genreId = 1;
        long bookId = 1;
        //when
        when(genreRepository.existsById(genreId))
                .thenReturn(true);
        when(bookRepository.existsById(bookId))
                .thenReturn(true);
        bookService.addGenreToBook(genreId, bookId);
        //then
        verify(genreRepository, times(1))
                .insertGenreToBookByGenreIdAndBookId(genreId, bookId);
    }

    @Test
    void addGenreToBookWillThrowExceptionWhenGenreIdIsInvalid() {
        //given
        long genreId = 1;
        long bookId = 1;
        //when
        when(genreRepository.existsById(genreId))
                .thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.addGenreToBook(genreId, bookId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(
                        String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, genreId)
                );
    }

    @Test
    void addGenreToBookWillThrowExceptionWhenBookIdIsInvalid() {
        //given
        long genreId = 1;
        long bookId = 1;
        //when
        when(genreRepository.existsById(genreId))
                .thenReturn(true);
        when(bookRepository.existsById(bookId))
                .thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.addGenreToBook(genreId, bookId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(
                        String.format(BOOK_WITH_GIVEN_ID_NOT_FOUND, genreId)
                );
    }

    @Test
    void willRemoveGenreFormBook() {
        //given
        long genreId = 1;
        long bookId = 1;
        //when
        when(genreRepository.genreExistsForBook(genreId, bookId))
                .thenReturn(true);
        bookService.removeGenreFromBook(genreId, bookId);
        //then
        verify(genreRepository, times(1))
                .deleteGenreFromBookByGenreIdAndBookId(genreId, bookId);

    }

    @Test
    void removeGenreFromBookWillThrowExceptionWhenGenreNotFoundForBook() {
        //given
        long genreId = 1;
        long bookId = 1;
        //when
        when(genreRepository.genreExistsForBook(genreId, bookId))
                .thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.removeGenreFromBook(genreId, bookId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(
                        String.format(GENRE_NOT_FOUND_FOR_GIVEN_BOOK, genreId, bookId)
                );
    }

    @Test
    void willDeleteGenreById() {
        //given
        Long genreId = 1L;
        //when
        when(genreRepository.existsById(genreId)).thenReturn(true);
        bookService.deleteGenreById(genreId);
        //then
        verify(genreRepository, times(1)).deleteById(genreId);
    }

    @Test
    void deleteGenreByIdWillThrowExceptionWhenGenreWithGivenIdNotFound() {
        //given
        Long notExistingGenreId = 1L;
        //when
        when(genreRepository.existsById(notExistingGenreId)).thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.deleteGenreById(notExistingGenreId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(
                        String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, notExistingGenreId)
                );
    }

    @Test
    void willUpdateGenreTitle() {
        //given
        Long genreId = 1L;
        String genreTitle = "Adventure";
        Genre genreToUpdate = Genre
                .builder()
                .genreId(genreId)
                .title(genreTitle)
                .build();
        //when
        when(genreRepository.existsById(genreId)).thenReturn(true);
        when(genreValidator.isTitleValid(genreTitle)).thenReturn(true);
        bookService.updateGenreTitle(genreToUpdate);
        //then
        verify(genreRepository, times(1)).updateGenreTitleById(genreTitle, genreId);
    }

    @Test
    void updateGenreTitleWillThrowExceptionWhenGenreWithGivenIdNotFound() {
        //given
        Long genreId = 1L;
        String genreTitle = "Adventure";
        Genre genreToUpdate = Genre
                .builder()
                .genreId(genreId)
                .title(genreTitle)
                .build();
        //when
        when(genreRepository.existsById(genreId)).thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.updateGenreTitle(genreToUpdate))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(
                        String.format(GENRE_WITH_GIVEN_ID_NOT_FOUND, genreId)
                );
    }

    @Test
    void updateGenreTitleWillThrowExceptionWhenGivenTitleNotValid() {
        //given
        Long genreId = 1L;
        String genreTitle = "Adventure";
        Genre genreToUpdate = Genre
                .builder()
                .genreId(genreId)
                .title(genreTitle)
                .build();
        //when
        when(genreRepository.existsById(genreId)).thenReturn(true);
        when(genreValidator.isTitleValid(genreTitle)).thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.updateGenreTitle(genreToUpdate))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(GENRE_TITLE_IS_NOT_VALID);
    }

    @Test
    void isGenreExistsByTitleWillReturnTrue() {
        //given
        String genreTitle = "Sci-fi";
        //when
        when(genreRepository.existsByTitle(genreTitle)).thenReturn(true);
        boolean condition = bookService.isGenreExistsByTitle(genreTitle);
        //then
        assertThat(condition).isTrue();
    }

    @Test
    void isGenreExistsByTitleWillReturnFalse() {
        //given
        String genreTitle = "Sci-fi";
        //when
        when(genreRepository.existsByTitle(genreTitle)).thenReturn(false);
        boolean condition = bookService.isGenreExistsByTitle(genreTitle);
        //then
        assertThat(condition).isFalse();
    }

    @Test
    void willReturnGenresByPage() {
        //given
        Pageable pageWithGenres = PageRequest.of(0, 6);
        Genre genre = Genre.builder().build();
        Page<Genre> genresByPage = new PageImpl<>(List.of(genre));
        //when
        when(genreRepository.findAll(pageWithGenres)).thenReturn(genresByPage);
        Page<Genre> expectedGenresByPage = bookService.findGenresByPage(1);
        //then
        assertThat(expectedGenresByPage.getTotalPages()).isEqualTo(1);
        assertThat(expectedGenresByPage.getSize()).isEqualTo(1);
        assertThat(expectedGenresByPage).isEqualTo(genresByPage);
    }

    @Test
    void findGenresByPageWillThorExceptionWhenNothingWasFound() {
        //given
        Pageable pageWithGenres = PageRequest.of(0, 6);
        Page<Genre> genresByPage = new PageImpl<>(new ArrayList<>());
        //when
        when(genreRepository.findAll(pageWithGenres)).thenReturn(genresByPage);
        //then
        assertThatThrownBy(() -> bookService.findGenresByPage(1))
                .isInstanceOf(NothingFoundException.class)
                .hasMessageContaining(NOTHING_WAS_FOUND_MSG);
    }

    @Test
    void willFindGenresByKeyWord() {
        //given
        String keyWord = "Sci";
        Genre firstGenre = Genre.builder().title("Sci-fi").build();
        Genre secondGenre = Genre.builder().title("Sci").build();
        List<Genre> genres = List.of(firstGenre, secondGenre);
        //when
        when(genreRepository.findAll()).thenReturn(genres);
        List<Genre> expectedGenresByKeyWord = bookService.findGenresByKeyword(keyWord);
        //then
        assertThat(expectedGenresByKeyWord).isNotNull();
        assertThat(expectedGenresByKeyWord.size()).isEqualTo(2);
        assertThat(expectedGenresByKeyWord).isEqualTo(genres);
    }

    @Test
    void findGenresByKeyWordWillThrowExceptionWhenNothingWasFound() {
        //given
        String keyWord = "Sci";
        List<Genre> genres = new ArrayList<>();
        //when
        when(genreRepository.findAll()).thenReturn(genres);
        //then
        assertThatThrownBy(() -> bookService.findGenresByKeyword(keyWord))
                .isInstanceOf(NothingFoundException.class)
                .hasMessageContaining(
                        String.format(GENRES_WITH_GIVEN_KEYWORD_NOT_FOUND, keyWord)
                );
    }

    @Test
    void willFindAllGenres() {
        //given
        Genre genre = Genre.builder().build();
        List<Genre> genres = List.of(genre);
        //when
        when(genreRepository.findAll()).thenReturn(genres);
        List<Genre> expectedGenres = bookService.findAllGenres();
        //then
        assertThat(expectedGenres).isNotNull();
        assertThat(expectedGenres.size()).isEqualTo(1);
        assertThat(expectedGenres).isEqualTo(genres);
    }

    @Test
    void findAllGenresWillThrowExceptionWhenNothingFound() {
        //given
        List<Genre> emptyGenres = new ArrayList<>();
        //when
        when(genreRepository.findAll()).thenReturn(emptyGenres);
        //then
        assertThatThrownBy(() -> bookService.findAllGenres())
                .isInstanceOf(NothingFoundException.class)
                .hasMessageContaining(NOTHING_WAS_FOUND_MSG);
    }

    @Test
    void willAddPublisherWithDefaultImage() {
        //given
        String publisherName = "Harvest";
        Publisher publisherToAdd = Publisher
                .builder()
                .name(publisherName)
                .description(anyString())
                .imagePath(DEFAULT_PUBLISHER_IMAGE_PATH)
                .build();
        //when
        when(publisherRepository.existsByName(publisherName)).thenReturn(false);
        when(publisherValidator.isNameValid(publisherName)).thenReturn(true);
        when(publisherValidator.isDescriptionValid(anyString())).thenReturn(true);
        bookService.addPublisher(publisherToAdd, null);
        //then
        verify(publisherRepository, times(1)).save(publisherToAdd);
    }

    @SneakyThrows(IOException.class)
    @Test
    void willAddPublisherWithProvidedImage() {
        //given
        MultipartFile publisherImageToSave = mock(MultipartFile.class);
        byte[] data = new byte[]{1, 2, 3, 4};
        String publisherName = "Harvest";
        String imageName = "123.jpg";
        Publisher publisherToAdd = Publisher
                .builder()
                .name(publisherName)
                .description(anyString())
                .imagePath(PUBLISHER_LOCALHOST_PATH.concat(imageName))
                .build();
        //when
        when(publisherRepository.existsByName(publisherName))
                .thenReturn(false);
        when(publisherValidator.isNameValid(publisherName))
                .thenReturn(true);
        when(publisherValidator.isDescriptionValid(anyString()))
                .thenReturn(true);
        when(publisherImageToSave.getInputStream())
                .thenReturn(new ByteArrayInputStream(data));
        when(publisherImageToSave.getOriginalFilename())
                .thenReturn(imageName);
        when(imageValidator.isImageValid(imageName))
                .thenReturn(true);
        bookService.addPublisher(publisherToAdd, publisherImageToSave);
        //then
        verify(publisherRepository, times(1)).save(publisherToAdd);
    }

    @Test
    void addPublisherWillThrowExceptionWhenNameAlreadyTaken() {
        //given
        String publisherName = "Harvest";
        Publisher existingPublisher = Publisher
                .builder()
                .name(publisherName)
                .build();
        //when
        when(publisherRepository.existsByName(publisherName)).thenReturn(true);
        //then
        assertThatThrownBy(() -> bookService.addPublisher(existingPublisher, null))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContaining(
                        String.format(PUBLISHER_WITH_GIVEN_NAME_ALREADY_EXISTS, publisherName)
                );
    }

    @Test
    void addPublisherWillThrowExceptionWhenGivenNameIsNotValid() {
        //given
        String publisherName = "Harvest";
        Publisher publisherToAdd = Publisher
                .builder()
                .name(publisherName)
                .build();
        //when
        when(publisherRepository.existsByName(publisherName)).thenReturn(false);
        when(publisherValidator.isNameValid(publisherName)).thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.addPublisher(publisherToAdd, null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(PUBLISHER_NAME_IS_INVALID);
    }

    @Test
    void addPublisherWillThrowExceptionWhenGivenDescriptionIsNotValid() {
        //given
        String publisherName = "Harvest";
        Publisher publisherToAdd = Publisher
                .builder()
                .name(publisherName)
                .description(anyString())
                .build();
        //when
        when(publisherRepository.existsByName(publisherName)).thenReturn(false);
        when(publisherValidator.isNameValid(publisherName)).thenReturn(true);
        when(publisherValidator.isDescriptionValid(anyString())).thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.addPublisher(publisherToAdd, null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(PUBLISHER_DESCRIPTION_IS_INVALID);
    }

    @Test
    void addPublisherWillThrowExceptionWhenGivenImageIsNotValid() {
        //given
        MultipartFile imageToSave = mock(MultipartFile.class);
        String publisherName = "Harvest";
        String imageName = "123.xml";
        Publisher existingPublisher = Publisher
                .builder()
                .name(publisherName)
                .description(anyString())
                .imagePath(imageName)
                .build();
        //when
        when(publisherRepository.existsByName(publisherName)).thenReturn(false);
        when(publisherValidator.isNameValid(publisherName)).thenReturn(true);
        when(publisherValidator.isDescriptionValid(anyString())).thenReturn(true);
        when(imageToSave.getOriginalFilename()).thenReturn(imageName);
        when(imageValidator.isImageValid(imageName)).thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.addPublisher(existingPublisher, imageToSave))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(IMAGE_IS_NOT_VALID_MSG);
    }

    @Test
    void willUpdatePublisherInfoWithOldImage() {
        Long publisherId = 1L;
        String publisherName = "Harvest";
        String description = "asd";
        String imagePath = "123.jpg";
        Publisher givenPublisher = Publisher.builder()
                .publisherId(publisherId)
                .name(publisherName)
                .description(description)
                .imagePath(imagePath)
                .build();
        //when
        when(publisherValidator.isNameValid(publisherName)).thenReturn(true);
        when(publisherValidator.isDescriptionValid(description)).thenReturn(true);
        bookService.updatePublisherInfo(givenPublisher, null);
        //then
        verify(publisherRepository, times(1))
                .updateInfoById(
                        publisherName,
                        description,
                        imagePath,
                        publisherId);
    }

    @SneakyThrows(IOException.class)
    @Test
    void willUpdatePublisherInfoWithNewImage() {
        MultipartFile newPublisherImage = mock(MultipartFile.class);
        byte[] data = new byte[]{1, 2, 3, 4};
        Long publisherId = 1L;
        String publisherName = "Harvest";
        String description = "asd";
        String imageName = "123.jpg";
        Publisher givenPublisher = Publisher.builder()
                .publisherId(publisherId)
                .name(publisherName)
                .description(description)
                .imagePath(PUBLISHER_LOCALHOST_PATH.concat(imageName))
                .build();
        //when
        when(publisherValidator.isNameValid(publisherName)).thenReturn(true);
        when(publisherValidator.isDescriptionValid(description)).thenReturn(true);
        when(newPublisherImage.getInputStream()).thenReturn(new ByteArrayInputStream(data));
        when(newPublisherImage.getOriginalFilename()).thenReturn(imageName);
        when(imageValidator.isImageValid(imageName)).thenReturn(true);
        bookService.updatePublisherInfo(givenPublisher, newPublisherImage);
        //then
        verify(publisherRepository, times(1))
                .updateInfoById(
                        givenPublisher.getName(),
                        givenPublisher.getDescription(),
                        givenPublisher.getImagePath(),
                        givenPublisher.getPublisherId());
    }

    @Test
    void updatePublisherInfoWillThrowExceptionWhenImageIsNotValid() {
        MultipartFile newPublisherImage = mock(MultipartFile.class);
        Long publisherId = 1L;
        String publisherName = "Harvest";
        String description = "asd";
        String imageName = "123.jpg";
        Publisher givenPublisher = Publisher.builder()
                .publisherId(publisherId)
                .name(publisherName)
                .description(description)
                .imagePath(PUBLISHER_LOCALHOST_PATH.concat(imageName))
                .build();
        //when
        when(publisherValidator.isNameValid(publisherName)).thenReturn(true);
        when(publisherValidator.isDescriptionValid(description)).thenReturn(true);
        when(newPublisherImage.getOriginalFilename()).thenReturn(imageName);
        when(imageValidator.isImageValid(imageName)).thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.updatePublisherInfo(givenPublisher, newPublisherImage))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(IMAGE_IS_NOT_VALID_MSG);
    }

    @Test
    void updatePublisherInfoWillThrowExceptionWhenNameIsNotValid() {
        //given
        String invalidPublisherName = "!Harvest";
        Publisher givenPublisher = Publisher.builder()
                .name(invalidPublisherName)
                .build();
        //when
        when(publisherValidator.isNameValid(invalidPublisherName)).thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.updatePublisherInfo(givenPublisher, null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(PUBLISHER_NAME_IS_INVALID);
    }

    @Test
    void updatePublisherInfoWillThrowExceptionWhenDescriptionIsNotValid() {
        //given
        String publisherName = "Harvest";
        String invalidDescription = "asd";
        Publisher givenPublisher = Publisher.builder()
                .name(publisherName)
                .description(invalidDescription)
                .build();
        //when
        when(publisherValidator.isNameValid(publisherName)).thenReturn(true);
        when(publisherValidator.isDescriptionValid(invalidDescription)).thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.updatePublisherInfo(givenPublisher, null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(PUBLISHER_DESCRIPTION_IS_INVALID);
    }

    @Test
    void willDeletePublisherById() {
        //given
        Long publisherId = 1L;
        //when
        when(publisherRepository.existsById(publisherId)).thenReturn(true);
        bookService.deletePublisherById(publisherId);
        //then
        verify(publisherRepository, times(1)).deleteById(publisherId);
    }

    @Test
    void deletePublisherByIdWillThrowExceptionWhenPublisherWithGivenIdNotFound() {
        //given
        Long publisherId = 1L;
        //when
        when(publisherRepository.existsById(publisherId)).thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.deletePublisherById(publisherId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(
                        String.format(PUBLISHER_WITH_GIVEN_ID_NOT_FOUND, publisherId)
                );
    }

    @Test
    void isPublisherExistsByNameWillReturnTrue() {
        //given
        String publisherName = "Harvest";
        //when
        when(publisherRepository.existsByName(publisherName)).thenReturn(true);
        boolean publisherExists = bookService.isPublisherExistsByName(publisherName);
        //then
        assertThat(publisherExists).isTrue();
    }

    @Test
    void isPublisherExistsByNameWillReturnFalse() {
        //given
        String publisherName = "Harvest";
        //when
        when(publisherRepository.existsByName(publisherName)).thenReturn(false);
        boolean publisherExists = bookService.isPublisherExistsByName(publisherName);
        //then
        assertThat(publisherExists).isFalse();
    }

    @Test
    void willFindPublisherInfoByName() {
        //given
        String publisherName = "Harvest";
        Publisher givenPublisher = Publisher
                .builder()
                .name(publisherName)
                .build();
        //when
        when(publisherRepository.findByName(publisherName)).thenReturn(Optional.of(givenPublisher));
        Publisher expectedPublisher = bookService.findPublisherInfoByName(publisherName);
        //then
        assertThat(expectedPublisher).isNotNull();
        assertThat(expectedPublisher.getName()).isEqualTo(publisherName);
    }

    @Test
    void findPublisherInfoByNameWillThrowExceptionWhenGivenNameNotFound() {
        //given
        String publisherName = "Harvest";
        //when
        when(publisherRepository.findByName(publisherName))
                .thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> bookService.findPublisherInfoByName(publisherName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(
                        String.format(PUBLISHER_WITH_GIVEN_NAME_NOT_FOUND, publisherName)
                );
    }

    @Test
    void willFindPublishersByKeyWord() {
        //given
        String keyWord = "Harv";
        Publisher firstPublisher = Publisher
                .builder()
                .name("Harvest")
                .build();
        Publisher secondPublisher = Publisher
                .builder()
                .name("Harv")
                .build();
        //when
        when(publisherRepository.findAll())
                .thenReturn(List.of(firstPublisher, secondPublisher));
        List<Publisher> expectedPublishersByKeyWord = bookService.findPublishersByKeyword(keyWord);
        //then
        assertThat(expectedPublishersByKeyWord).isNotNull();
        assertThat(expectedPublishersByKeyWord.size()).isEqualTo(2);
    }

    @Test
    void findPublishersByKeyWordWillThrowExceptionWhenNothingWasFound() {
        //given
        String keyWord = "Stat";
        Publisher firstPublisher = Publisher
                .builder()
                .name("Harvest")
                .build();
        Publisher secondPublisher = Publisher
                .builder()
                .name("Harv")
                .build();
        //when
        when(publisherRepository.findAll())
                .thenReturn(List.of(firstPublisher, secondPublisher));
        //then
        assertThatThrownBy(() -> bookService.findPublishersByKeyword(keyWord))
                .isInstanceOf(NothingFoundException.class)
                .hasMessageContaining(NOTHING_WAS_FOUND_MSG);
    }

    @Test
    void willFindPublishersByPage() {
        //given
        Pageable pageWithPublishers = PageRequest.of(0, 6);
        Publisher publisher = Publisher.builder().build();
        List<Publisher> publishers = List.of(publisher);
        Page<Publisher> publishersByPage = new PageImpl<>(publishers);
        //when
        when(publisherRepository.findAll(pageWithPublishers)).thenReturn(publishersByPage);
        Page<Publisher> expectedPublishersByPage = bookService.findPublishersByPage(1);
        //then
        assertThat(expectedPublishersByPage).isNotNull();
        assertThat(expectedPublishersByPage.getSize()).isEqualTo(1);
        assertThat(expectedPublishersByPage.getTotalPages()).isEqualTo(1);
    }

    @Test
    void findPublishersByPageWillThrowExceptionWhenNothingWasFound() {
        //given
        Pageable pageWithPublishers = PageRequest.of(0, 6);
        List<Publisher> publishers = new ArrayList<>();
        Page<Publisher> publishersByPage = new PageImpl<>(publishers);
        //when
        when(publisherRepository.findAll(pageWithPublishers)).thenReturn(publishersByPage);
        //then
        assertThatThrownBy(() -> bookService.findPublishersByPage(1))
                .isInstanceOf(NothingFoundException.class)
                .hasMessageContaining(NOTHING_WAS_FOUND_MSG);
    }

    @Test
    void willFindAllPublishers() {
        //given
        Publisher publisher = Publisher.builder().build();
        List<Publisher> publishers = List.of(publisher);
        //when
        when(publisherRepository.findAll()).thenReturn(publishers);
        List<Publisher> expectedPublishers = bookService.findAllPublishers();
        //then
        assertThat(expectedPublishers).isNotNull();
        assertThat(expectedPublishers).isEqualTo(publishers);
    }

    @Test
    void findAllPublishersWillThrowExceptionWhenNothingWasFound() {
        //given
        List<Publisher> publishers = new ArrayList<>();
        //when
        when(publisherRepository.findAll()).thenReturn(publishers);
        //then
        assertThatThrownBy(() -> bookService.findAllPublishers())
                .isInstanceOf(NothingFoundException.class)
                .hasMessageContaining(NOTHING_WAS_FOUND_MSG);
    }


    @SneakyThrows(IOException.class)
    @Test
    void willAddAuthorWithNewImage() {
        //given
        MultipartFile imageToSave = mock(MultipartFile.class);
        String firstName = "Stanislau";
        String lastname = "Kachan";
        String imageName = "123.jpg";
        byte[] data = new byte[]{1, 2, 3, 4};
        Author author = Author.builder()
                .firstName(firstName)
                .lastName(lastname)
                .birthDate(LocalDate.of(1995, 11, 12))
                .imagePath(AUTHORS_LOCALHOST_PATH.concat(imageName))
                .biography("asd")
                .build();
        //when
        when(authorValidator.isFirstnameValid(firstName)).thenReturn(true);
        when(authorValidator.isLastnameValid(lastname)).thenReturn(true);
        when(imageToSave.getInputStream()).thenReturn(new ByteArrayInputStream(data));
        when(imageToSave.getOriginalFilename()).thenReturn(imageName);
        when(imageValidator.isImageValid(imageName)).thenReturn(true);
        bookService.addAuthor(author, imageToSave);
        //then
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void willAddAuthorWithDefaultImage() {
        //given
        String firstName = "Stanislau";
        String lastname = "Kachan";
        Author author = Author.builder()
                .firstName(firstName)
                .lastName(lastname)
                .birthDate(LocalDate.of(1995, 11, 12))
                .imagePath(DEFAULT_AUTHOR_IMAGE_PATH)
                .biography("asd")
                .build();
        //when
        when(authorValidator.isFirstnameValid(firstName)).thenReturn(true);
        when(authorValidator.isLastnameValid(lastname)).thenReturn(true);
        bookService.addAuthor(author, null);
        //then
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void addAuthorWillThrowExceptionWhenImageIsNotValid() {
        //given
        MultipartFile imageToSave = mock(MultipartFile.class);
        String firstName = "Stanislau";
        String lastname = "Kachan";
        String imageName = "123.xml";
        Author author = Author.builder()
                .firstName(firstName)
                .lastName(lastname)
                .birthDate(LocalDate.of(1995, 11, 12))
                .imagePath(AUTHORS_LOCALHOST_PATH.concat(imageName))
                .biography("asd")
                .build();
        //when
        when(authorValidator.isFirstnameValid(firstName)).thenReturn(true);
        when(authorValidator.isLastnameValid(lastname)).thenReturn(true);
        when(imageToSave.getOriginalFilename()).thenReturn(imageName);
        when(imageValidator.isImageValid(imageName)).thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.addAuthor(author, imageToSave))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(IMAGE_IS_NOT_VALID_MSG);
    }

    @Test
    void addAuthorWillThrowExceptionWhenFirstNameIsNotValid() {
        //given
        String firstName = "!Stanislau";
        MultipartFile mockFile = mock(MultipartFile.class);
        Author author = Author.builder()
                .authorId(1L)
                .firstName(firstName)
                .build();
        //when
        when(authorValidator.isFirstnameValid(firstName)).thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.addAuthor(author, mockFile))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(FIRST_NAME_IS_NOT_VALID_MSG);
    }

    @Test
    void addAuthorWillThrowExceptionWhenLastNameIsNotValid() {
        //given
        String firstName = "Stanislau";
        String lastname = "!Kachan";
        MultipartFile mockFile = mock(MultipartFile.class);
        Author author = Author.builder()
                .authorId(1L)
                .firstName(firstName)
                .lastName(lastname)
                .build();
        //when
        when(authorValidator.isFirstnameValid(firstName)).thenReturn(true);
        when(authorValidator.isLastnameValid(lastname)).thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.addAuthor(author, mockFile))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(LAST_NAME_IS_NOT_VALID_MSG);
    }

    @Test
    void willUpdateActorInfoWithOldImage() {
        //given
        String firstName = "Stanislau";
        String lastName = "Kachan";
        Author author = Author.builder()
                .authorId(1L)
                .firstName(firstName)
                .lastName(lastName)
                .biography("qwer")
                .birthDate(LocalDate.now())
                .imagePath("author.jpg")
                .build();
        //when
        when(authorRepository.existsById(author.getAuthorId()))
                .thenReturn(true);
        when(authorValidator.isFirstnameValid(firstName))
                .thenReturn(true);
        when(authorValidator.isLastnameValid(lastName))
                .thenReturn(true);
        bookService.updateAuthorInfo(author, null);
        //then
        verify(authorRepository, times(1))
                .updateInfoByAuthorFieldsAndId(
                        firstName,
                        lastName,
                        author.getBiography(),
                        author.getBirthDate(),
                        author.getImagePath(),
                        author.getAuthorId());
    }

    @SneakyThrows(IOException.class)
    @Test
    void willUpdateActorInfoWithNewImage() {
        //given
        MultipartFile imageToSave = mock(MultipartFile.class);
        String firstName = "Stanislau";
        String lastname = "Kachan";
        String imageName = "123.jpg";
        byte[] data = new byte[]{1, 2, 3, 4};
        Author author = Author.builder()
                .firstName(firstName)
                .lastName(lastname)
                .birthDate(LocalDate.of(1995, 11, 12))
                .imagePath(AUTHORS_LOCALHOST_PATH.concat(imageName))
                .biography("asd")
                .build();
        //when
        when(authorRepository.existsById(author.getAuthorId()))
                .thenReturn(true);
        when(authorValidator.isFirstnameValid(firstName))
                .thenReturn(true);
        when(authorValidator.isLastnameValid(lastname))
                .thenReturn(true);
        when(imageToSave.getInputStream())
                .thenReturn(new ByteArrayInputStream(data));
        when(imageToSave.getOriginalFilename())
                .thenReturn(imageName);
        when(imageValidator.isImageValid(imageName))
                .thenReturn(true);
        bookService.updateAuthorInfo(author, imageToSave);
        //then
        verify(authorRepository, times(1))
                .updateInfoByAuthorFieldsAndId(
                        firstName,
                        lastname,
                        author.getBiography(),
                        author.getBirthDate(),
                        author.getImagePath(),
                        author.getAuthorId());
    }

    @Test
    void updateAuthorInfoWillThrowExceptionWhenUserWithGivenIdNotFound() {
        //given
        String firstName = "!2345";
        MultipartFile mockFile = mock(MultipartFile.class);
        Author author = Author.builder()
                .authorId(1L)
                .firstName(firstName)
                .build();
        //when
        when(authorRepository.existsById(author.getAuthorId()))
                .thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.updateAuthorInfo(author, mockFile))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(
                        String.format(AUTHOR_WITH_GIVEN_ID_NOT_FOUND, author.getAuthorId())
                );
    }


    @Test
    void updateAuthorInfoWillThrowExceptionWhenFirstNameNotValid() {
        //given
        String firstName = "!2345";
        MultipartFile mockFile = mock(MultipartFile.class);
        Author author = Author.builder()
                .authorId(1L)
                .firstName(firstName)
                .build();
        //when
        when(authorRepository.existsById(author.getAuthorId()))
                .thenReturn(true);
        when(authorValidator.isFirstnameValid(firstName))
                .thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.updateAuthorInfo(author, mockFile))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(FIRST_NAME_IS_NOT_VALID_MSG);
    }

    @Test
    void updateAuthorInfoWillThrowExceptionWhenLastNameIsNotValid() {
        //given
        String firstName = "Stanislau";
        String lastName = "!Kachan";
        MultipartFile mockFile = mock(MultipartFile.class);
        Author author = Author.builder()
                .authorId(1L)
                .firstName(firstName)
                .lastName(lastName)
                .build();
        //when
        when(authorRepository.existsById(author.getAuthorId()))
                .thenReturn(true);
        when(authorValidator.isFirstnameValid(firstName))
                .thenReturn(true);
        when(authorValidator.isLastnameValid(lastName))
                .thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.updateAuthorInfo(author, mockFile))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(LAST_NAME_IS_NOT_VALID_MSG);
    }

    @Test
    void updateAuthorInfoWillThrowExceptionWhenImageIsNotValid() {
        //given
        String firstName = "Stanislau";
        String lastName = "Kachan";
        MultipartFile mockFile = mock(MultipartFile.class);
        Author author = Author.builder()
                .authorId(1L)
                .firstName(firstName)
                .lastName(lastName)
                .build();
        //when
        when(authorRepository.existsById(author.getAuthorId()))
                .thenReturn(true);
        when(authorValidator.isFirstnameValid(firstName))
                .thenReturn(true);
        when(authorValidator.isLastnameValid(lastName))
                .thenReturn(true);
        when(mockFile.getOriginalFilename()).thenReturn("file.txt");
        when(imageValidator.isImageValid("file.txt")).thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.updateAuthorInfo(author, mockFile))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(IMAGE_IS_NOT_VALID_MSG);
    }

    @Test
    void willFindAuthorsByKeyword() {
        //given
        String keyWord = "Stanislau";
        Author author = Author.builder()
                .authorId(1L)
                .firstName("Stanislau")
                .build();
        List<Author> authors = List.of(author);
        //when
        when(authorRepository.findAll()).thenReturn(authors);
        List<Author> expectedAuthors = bookService.findAuthorsByKeyword(keyWord);
        //then
        assertThat(expectedAuthors).isNotNull();
        assertThat(expectedAuthors).isEqualTo(authors);
        assertThat(expectedAuthors.size()).isEqualTo(1);
    }

    @Test
    void findAuthorsByKeywordWillThrowExceptionWhenNothingFound() {
        //given
        String keyWord = "Stanislau";
        Author author = Author.builder()
                .authorId(1L)
                .firstName("Stas")
                .lastName("Kachan")
                .build();
        List<Author> authors = List.of(author);
        //when
        when(authorRepository.findAll()).thenReturn(authors);
        //then
        assertThatThrownBy(() -> bookService.findAuthorsByKeyword(keyWord))
                .isInstanceOf(NothingFoundException.class)
                .hasMessageContaining(
                        String.format(AUTHOR_WITH_GIVEN_KEYWORD_NOT_FOUND, keyWord)
                );
    }

    @Test
    void willFindAuthorInfoById() {
        //given
        long authorId = 2;
        Author givenAuthor = Author.builder()
                .authorId(authorId)
                .build();
        //when
        when(authorRepository.findByAuthorId(authorId)).thenReturn(Optional.of(givenAuthor));
        Author expectedAuthor = bookService.findAuthorInfoByAuthorId(authorId);
        //then
        assertThat(expectedAuthor).isEqualTo(givenAuthor);
    }

    @Test
    void findAuthorInfoByIdWillThrowExceptionWhenUserNotFound() {
        //given
        long authorId = 2;
        //when
        when(authorRepository.findByAuthorId(authorId)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> bookService.findAuthorInfoByAuthorId(authorId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(
                        String.format(AUTHOR_WITH_GIVEN_ID_NOT_FOUND, authorId)
                );
    }

    @Test
    void willDeleteUserById() {
        //given
        long authorId = 2;
        //when
        when(authorRepository.existsById(authorId)).thenReturn(true);
        bookService.deleteAuthorById(authorId);
        //then
        verify(authorRepository, times(1)).deleteById(authorId);
    }

    @Test
    void deleteAuthorByIdWillThrowExceptionWhenUserNotFound() {
        //given
        long authorId = 2;
        //when
        when(authorRepository.existsById(authorId)).thenReturn(false);
        //then
        assertThatThrownBy(() -> bookService.deleteAuthorById(authorId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(
                        String.format(AUTHOR_WITH_GIVEN_ID_NOT_FOUND, authorId)
                );
    }

    @Test
    void willFindAuthorsByPage() {
        //given
        Pageable pageWithAuthors = PageRequest.of(0, 6);
        Author firstAuthor = Author.builder().build();
        Author secondAuthor = Author.builder().build();
        List<Author> authors = List.of(firstAuthor, secondAuthor);
        Page<Author> authorsByPage = new PageImpl<>(authors);
        //when
        when(authorRepository.findAll(pageWithAuthors)).thenReturn(authorsByPage);
        Page<Author> expectedAuthorsByPage = bookService.findAuthorsByPage(1);
        //then
        assertThat(authorsByPage).isNotNull();
        assertThat(authorsByPage.getTotalPages()).isEqualTo(1);
        assertThat(authorsByPage.getSize()).isEqualTo(2);
        assertThat(authorsByPage).isEqualTo(expectedAuthorsByPage);
    }

    @Test
    void findAuthorsByPageWillThrowExceptionWhenNothingFound() {
        //given
        Pageable pageWithAuthors = PageRequest.of(0, 6);
        List<Author> authors = new ArrayList<>();
        Page<Author> authorsByPage = new PageImpl<>(authors);
        //when
        when(authorRepository.findAll(pageWithAuthors)).thenReturn(authorsByPage);
        //then
        assertThatThrownBy(() -> bookService.findAuthorsByPage(1))
                .isInstanceOf(NothingFoundException.class)
                .hasMessageContaining(NOTHING_WAS_FOUND_MSG);
    }


    @AfterEach
    void tearDown() {
        authorRepository.deleteAll();
        publisherRepository.deleteAll();
        publisherValidator = null;
        genreValidator = null;
        authorValidator = null;
        imageValidator = null;
        bookService = null;
    }
}