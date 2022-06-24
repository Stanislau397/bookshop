package edu.epam.bookshop.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Slf4j
@Component
public class ImageValidator {


    private static final Pattern IMAGE_PATTERN =
            Pattern.compile("([^\\s]+(\\.(?i)(jpg|png|gif|bmp|jpeg))$)");

    private static final String INVALID_IMAGE_MSG = "Invalid image data!";

    public boolean isImageValid(String imageName) {
        boolean imageValid = imageName.matches(IMAGE_PATTERN.pattern());
        if (!imageValid) {
            log.info(INVALID_IMAGE_MSG);
        }
        return imageValid;
    }
}
