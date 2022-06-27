package edu.epam.bookshop.service;

import edu.epam.bookshop.entity.Author;
import edu.epam.bookshop.entity.Genre;
import edu.epam.bookshop.entity.Publisher;
import edu.epam.bookshop.exception.EntityAlreadyExistsException;
import edu.epam.bookshop.exception.EntityNotFoundException;
import edu.epam.bookshop.exception.InvalidInputException;
import edu.epam.bookshop.exception.NothingFoundException;
import edu.epam.bookshop.repository.AuthorRepository;
import edu.epam.bookshop.repository.GenreRepository;
import edu.epam.bookshop.repository.PublisherRepository;
import edu.epam.bookshop.service.impl.BookServiceImpl;
import edu.epam.bookshop.validator.AuthorValidator;
import edu.epam.bookshop.validator.GenreValidator;
import edu.epam.bookshop.validator.ImageValidator;
import edu.epam.bookshop.validator.PublisherValidator;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.epam.bookshop.constant.ExceptionMessage.*;
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
    private AuthorValidator authorValidator;

    @Mock
    private ImageValidator imageValidator;

    @Mock
    private GenreValidator genreValidator;

    @Mock
    private PublisherValidator publisherValidator;

    @Mock
    private GenreRepository genreRepository;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(authorRepository,
                publisherRepository,
                genreRepository,
                genreValidator,
                publisherValidator,
                authorValidator,
                imageValidator);
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
        Publisher publisher = Publisher.builder().build();
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


//    @Test
//    void willAddAuthorWithDefaultImage() {
//        //given
//        String firstName = "Stanislau";
//        String lastname = "Kachan";
//        MultipartFile mockFile = null;
//        Author author = Author.builder()
//                .firstName(firstName)
//                .lastName(lastname)
//                .birthDate(LocalDate.of(1995, 11, 12))
//                .imagePath(DEFAULT_AUTHOR_IMAGE_PATH)
//                .biography("asd")
//                .build();
//        //when
//        when(authorValidator.isFirstnameValid(firstName)).thenReturn(true);
//        when(authorValidator.isLastnameValid(lastname)).thenReturn(true);
//        bookService.addAuthor(author, mockFile);
//        //then
//        ArgumentCaptor<Author> authorArgumentCaptor =
//                ArgumentCaptor.forClass(Author.class);
//        verify(authorRepository).save(authorArgumentCaptor.capture());
//
//        Author capturedUser = authorArgumentCaptor.getValue();
//        assertThat(capturedUser).isEqualTo(author);
//    }

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
    void willUpdateActorInfo() {
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