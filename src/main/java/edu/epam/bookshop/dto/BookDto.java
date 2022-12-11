package edu.epam.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class BookDto {

    private Long bookId;
    private String title;
    private String description;
    private String imagePath;
    private List<LocalizedGenreDto> localizedGenres;
}
