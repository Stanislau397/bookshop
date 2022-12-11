package edu.epam.bookshop.dto.mapper;

import edu.epam.bookshop.dto.BookDto;
import edu.epam.bookshop.dto.LocalizedGenreDto;
import edu.epam.bookshop.entity.Book;
import edu.epam.bookshop.entity.Genre;
import edu.epam.bookshop.entity.LocalizedBook;
import edu.epam.bookshop.entity.LocalizedGenre;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BookMapper {

    private LocalizedGenreMapper localizedGenreMapper;

    public BookDto toDto(Book book) {
        Long bookId = book.getBookId();
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

        BookDto bookDto = BookDto.builder()
                .bookId(bookId)
                .title(localizedTitle)
                .description(localizedDescription)
                .imagePath(imagePath)
                .localizedGenres(localizedGenres)
                .build();
        return bookDto;
    }
}
