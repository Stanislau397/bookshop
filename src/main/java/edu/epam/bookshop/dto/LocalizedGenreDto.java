package edu.epam.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class LocalizedGenreDto {

    private Long localizedGenreDtoId;
    private String title;
}
