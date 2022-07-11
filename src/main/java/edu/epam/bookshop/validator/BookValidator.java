package edu.epam.bookshop.validator;

import edu.epam.bookshop.exception.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_DESCRIPTION_IS_NOT_VALID;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_ISBN_IS_NOT_VALID;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_PAGES_FIELD_IS_NOT_VALID;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_PRICE_IS_NOT_VALID;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_TITLE_IS_NOT_VALID;

@Slf4j
@Component
public class BookValidator {

    private static final Pattern BOOK_TITLE_PATTERN =
            Pattern.compile("^[A-Za-z0-9\\s\\-_,\\.:;()''\"\"]+$");

    private static final Pattern ISBN_PATTERN =
            Pattern.compile("^(?:ISBN(?:-13)?:?\\ )?" +
                    "(?=[0-9]{13}$|(?=(?:[0-9]+[-\\ ]){4})" +
                    "[-\\ 0-9]{17}$)97[89][-\\ ]?[0-9]{1,5}" +
                    "[-\\ ]?[0-9]+[-\\ ]?[0-9]+[-\\ ]?[0-9]$");

    private static final Pattern DESCRIPTION_PATTERN =
            Pattern.compile("^.{1,1500}$");

    private static final Pattern PAGES_PATTERN =
            Pattern.compile("^(?:[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$");

    public boolean isBookDataValid(String title, BigDecimal price, String description,
                                   Integer pages, String isbn) {

        return isBookTitleValid(title)
                && isPriceValid(price)
                && isDescriptionValid(description)
                && isPagesValid(pages)
                && isIsbnValid(isbn);
    }

    public boolean isBookTitleValid(String bookTitle) {
        if (!bookTitle.matches(BOOK_TITLE_PATTERN.pattern())) {
            log.info("Book title is not valid");
            throw new InvalidInputException(BOOK_TITLE_IS_NOT_VALID);
        }
        return true;
    }

    public boolean isPriceValid(BigDecimal price) {
        boolean priceValid = price.signum() == 1;
        if (!priceValid) {
            log.info("Price is not valid");
            throw new InvalidInputException(BOOK_PRICE_IS_NOT_VALID);
        }
        return true;
    }

    public boolean isDescriptionValid(String description) {
//        if (!description.matches(DESCRIPTION_PATTERN.pattern())) {
//            log.info("Description is not valid");
//            throw new InvalidInputException(BOOK_DESCRIPTION_IS_NOT_VALID);
//        }
        return true;
    }

    public boolean isPagesValid(Integer pages) {
        String pagesAsString = String.valueOf(pages);
        if (!pagesAsString.matches(PAGES_PATTERN.pattern())) {
            log.info("Pages are not valid");
            throw new InvalidInputException(BOOK_PAGES_FIELD_IS_NOT_VALID);
        }
        return true;
    }

    public boolean isIsbnValid(String isbn) {
        if (!isbn.matches(ISBN_PATTERN.pattern())) {
            log.info("Isbn is not valid");
            throw new InvalidInputException(BOOK_ISBN_IS_NOT_VALID);
        }
        return true;
    }
}
