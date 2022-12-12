package edu.epam.bookshop.dto.mapper;

import edu.epam.bookshop.dto.BookDto;
import edu.epam.bookshop.dto.LocalizedBookDto;
import edu.epam.bookshop.dto.LocalizedGenreDto;
import edu.epam.bookshop.entity.Author;
import edu.epam.bookshop.entity.Book;
import edu.epam.bookshop.entity.CoverType;
import edu.epam.bookshop.entity.Genre;
import edu.epam.bookshop.entity.LocalizedBook;
import edu.epam.bookshop.entity.Publisher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BookMapper {

    private LocalizedGenreMapper localizedGenreMapper;

    public BookDto toDto(Book book) {
        Long bookId = book.getBookId();
        String isbn = book.getIsbn();
        String pages = String.valueOf(book.getPages());
        String coverType = String.valueOf(book.getCoverType()).toLowerCase();
        String publishDate = String.valueOf(book.getPublishDate());
        String price = String.valueOf(book.getPrice());

        String localizedTitle = book.getLocalizedBooks()
                .stream()
                .map(LocalizedBook::getTitle)
                .collect(Collectors.joining());

        String localizedDescription = book.getLocalizedBooks()
                .stream()
                .map(LocalizedBook::getDescription)
                .collect(Collectors.joining());

        String imagePath = book.getLocalizedBooks()
                .stream()
                .map(LocalizedBook::getImagePath)
                .collect(Collectors.joining());

        List<LocalizedGenreDto> localizedGenres = book.getGenres()
                .stream()
                .map(Genre::getLocalizedGenres)
                .flatMap(Collection::stream)
                .map(localizedGenreMapper::toDto)
                .toList();

        Long localizedBookId = book.getLocalizedBooks()
                .get(0)
                .getLocalizedBookId();

        List<Author> authors = book.getAuthors();
        List<Publisher> publishers = book.getPublishers();

        LocalizedBookDto localizedBookDto = LocalizedBookDto.builder()
                .localizedBookId(localizedBookId)
                .title(localizedTitle)
                .description(localizedDescription)
                .imagePath(imagePath)
                .build();

        BookDto bookDto = BookDto.builder()
                .bookId(bookId)
                .localizedBook(localizedBookDto)
                .price(price)
                .coverType(coverType)
                .isbn(isbn)
                .pages(pages)
                .publishDate(publishDate)
                .localizedGenres(localizedGenres)
                .authors(authors)
                .publishers(publishers)
                .build();
        return bookDto;
    }

    public Book toBook(BookDto bookDto) {
        double price = Double.parseDouble(bookDto.getPrice());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Book book = Book.builder()
                .bookId(bookDto.getBookId())
                .price(BigDecimal.valueOf(price))
                .coverType(CoverType.valueOf(bookDto.getCoverType()))
                .publishDate(LocalDate.parse(bookDto.getPublishDate(), formatter))
                .isbn(bookDto.getIsbn())
                .pages(Integer.parseInt(bookDto.getPages()))
                .build();
        return book;
    }
}
