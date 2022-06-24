package edu.epam.bookshop.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Slf4j
@Component
public class PublisherValidator {

    private static final Pattern PUBLISHER_NAME_PATTERN =
            Pattern.compile("(?i)[a-z]([ ',.a-z]{0,50}[a-z])?");
    private static final Pattern PUBLISHER_DESCRIPTION_PATTERN =
            Pattern.compile("^(.{1,1000})$");

    private static final String PUBLISHER_NAME_IS_INVALID_MSG =
            "publisher name is invalid!";
    private static final String PUBLISHER_DESCRIPTION_IS_INVALID =
            "publisher description is invalid!";

    public boolean isNameValid(String publisherName) {
        boolean publisherNameValid = publisherName.matches(PUBLISHER_NAME_PATTERN.pattern());
        if (!publisherNameValid) {
            log.info(PUBLISHER_NAME_IS_INVALID_MSG);
        }
        return publisherNameValid;
    }

    public boolean isDescriptionValid(String description) {
        boolean descriptionValid = description.matches(PUBLISHER_DESCRIPTION_PATTERN.pattern());
        if (!descriptionValid) {
            log.info(PUBLISHER_DESCRIPTION_IS_INVALID);
        }
        return descriptionValid;
    }
}
