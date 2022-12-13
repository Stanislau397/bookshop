package edu.epam.bookshop.dto;

import edu.epam.bookshop.annotation.ValidateBookIsbn;
import edu.epam.bookshop.annotation.ValidateBookPages;
import edu.epam.bookshop.annotation.ValidateBookPrice;
import edu.epam.bookshop.annotation.ValidateDate;
import edu.epam.bookshop.entity.Author;
import edu.epam.bookshop.entity.Publisher;
import edu.epam.bookshop.annotation.ValidateCoverType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class BookDto {

    private Long bookId;
    @ValidateCoverType
    private String coverType;
    @ValidateDate
    private String publishDate;
    @ValidateBookIsbn
    private String isbn;
    @ValidateBookPages
    private String pages;
    @ValidateBookPrice
    private String price;
    @Valid
    private LocalizedBookDto localizedBook;
    private List<LocalizedGenreDto> localizedGenres;
    private List<Author> authors;
    private List<Publisher> publishers;
}
