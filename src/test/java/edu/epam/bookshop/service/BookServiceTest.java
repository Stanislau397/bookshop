package edu.epam.bookshop.service;

import edu.epam.bookshop.entity.Author;
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
import lombok.ToString;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.epam.bookshop.constant.ExceptionMessage.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static edu.epam.bookshop.constant.ImageStoragePath.DEFAULT_AUTHOR_IMAGE_PATH;

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
    void willUpdateActorInfo() throws IOException {
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
        authorValidator = null;
        imageValidator = null;
        bookService = null;
    }
}