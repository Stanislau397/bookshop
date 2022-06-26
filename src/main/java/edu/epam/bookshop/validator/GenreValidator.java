package edu.epam.bookshop.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static edu.epam.bookshop.constant.ExceptionMessage.GENRE_TITLE_IS_NOT_VALID;

@Slf4j
@Component
public class GenreValidator { //todo test

    private static final Pattern GENRE_TITLE_PATTERN =
            Pattern.compile("^[A-Z][a-zA-Z -]{1,99}");

    public boolean isTitleValid(String genreTitle) {
        boolean genreTitleValid = genreTitle.matches(GENRE_TITLE_PATTERN.pattern());
        if (!genreTitleValid) {
            log.info(GENRE_TITLE_IS_NOT_VALID);
        }
        return genreTitleValid;
    }
}
