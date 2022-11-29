package edu.epam.bookshop.validator;

import edu.epam.bookshop.annotation.ValidateImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

@Slf4j
@Component
public class ImageValidator implements ConstraintValidator<ValidateImage, String> {


    private static final Pattern IMAGE_PATTERN =
            Pattern.compile("([^\\s]+(\\.(?i)(jpg|png|gif|bmp|jpeg))$)");

    private static final String INVALID_IMAGE_MSG = "Invalid image data!";

    @Override
    public boolean isValid(String imageName, ConstraintValidatorContext constraintValidatorContext) {
        return imageName.matches(IMAGE_PATTERN.pattern());
    }

    public boolean isImageValid(String imageName) {
        boolean imageValid = imageName.matches(IMAGE_PATTERN.pattern());
        if (!imageValid) {
            log.info(INVALID_IMAGE_MSG);
        }
        return imageValid;
    }

    public boolean isImageNotNull(MultipartFile image) {
        return nonNull(image) && nonNull(image.getOriginalFilename());
    }
}
