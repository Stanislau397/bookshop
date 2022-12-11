package edu.epam.bookshop.dto.mapper;

import edu.epam.bookshop.dto.LocalizedGenreDto;
import edu.epam.bookshop.entity.LocalizedGenre;
import org.springframework.stereotype.Component;

@Component
public class LocalizedGenreMapper {

    public LocalizedGenreDto toDto(LocalizedGenre localizedGenre) {
        LocalizedGenreDto localizedGenreDto = LocalizedGenreDto.builder()
                .title(localizedGenre.getTitle())
                .localizedGenreDtoId(localizedGenre.getLocalizedGenreId())
                .build();
        return localizedGenreDto;
    }
}
