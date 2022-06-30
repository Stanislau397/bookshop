package edu.epam.bookshop.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.regex.Pattern;

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
            Pattern.compile("^(.{1,1000})$");

    private static final Pattern PAGES_PATTERN =
            Pattern.compile("^(?:[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$");

    public boolean isBookTitleValid(String bookTitle) {
        boolean bookTitleValid = bookTitle.matches(BOOK_TITLE_PATTERN.pattern());
        if (!bookTitleValid) {
            log.info("Book title is not valid");
        }
        return bookTitleValid;
    }

    public boolean isPriceValid(String price) {
        BigDecimal bookPrice = new BigDecimal(price);
        boolean priceValid = bookPrice.signum() == 1;
        if (!priceValid) {
            log.info("Price is not valid");
        }
        return priceValid;
    }

    public boolean isDescriptionValid(String description) {
        boolean descriptionValid = description.matches(DESCRIPTION_PATTERN.pattern());
        if (!descriptionValid) {
            log.info("Description is not valid");
        }
        return descriptionValid;
    }

    public boolean isPagesValid(String pages) {
        boolean pagesValid = pages.matches(PAGES_PATTERN.pattern());
        if (!pagesValid) {
            log.info("Pages are not valid");
        }
        return pagesValid;
    }

    public boolean isIsbnValid(String isbn) {
        boolean isbnValid = isbn.matches(ISBN_PATTERN.pattern());
        if (!isbnValid) {
            log.info("Isbn is not valid");
        }
        return isbnValid;
    }
}
