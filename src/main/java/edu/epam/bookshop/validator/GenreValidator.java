package edu.epam.bookshop.validator;

import edu.epam.bookshop.exception.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static edu.epam.bookshop.constant.ExceptionMessage.GENRE_TITLE_IS_NOT_VALID;

@Slf4j
@Component
public class GenreValidator { //todo test

    private static final Pattern GENRE_TITLE_PATTERN =
            Pattern.compile("[\\p{L}+][\\p{L}+ -]{1,99}");

    public boolean isTitleValid(String genreTitle) throws InvalidInputException {
        if (!genreTitle.matches(GENRE_TITLE_PATTERN.pattern())) {
            log.info(GENRE_TITLE_IS_NOT_VALID);
            throw new InvalidInputException(GENRE_TITLE_IS_NOT_VALID);
        }
        return true;
    }
}
