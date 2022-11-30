package edu.epam.bookshop.validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LanguageValidator {

    private static final String RUSSIAN_LANGUAGE = "ru";
    private static final String ENGLISH_LANGUAGE = "en";

    public boolean isLanguageValid(String language) {
        return language.equals(RUSSIAN_LANGUAGE) || language.equals(ENGLISH_LANGUAGE);
    }
}
