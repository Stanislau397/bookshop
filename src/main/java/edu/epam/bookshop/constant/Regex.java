package edu.epam.bookshop.constant;

public class Regex {

    private Regex() {

    }

    public static final String BOOK_TITLE_REGEX =
            "^[\\p{L}\\s\\d++\\-_,\\.:;()''\"\"]+$";
    public static final String BOOK_DESCRIPTION_REGEX = "^[\\p{L}\\s\\d++\\-_,\\.:;()''\"\"]{1,1000}$";
    public static final String IMAGE_REGEX = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp|jpeg))$)";
    public static final String BOOK_ISBN_REGEX = "^(?:ISBN(?:-13)?:?\\ )?(?=[0-9]{13}$|(?=(?:[0-9]+[-\\ ]){4})" +
            "[-\\ 0-9]{17}$)97[89][-\\ ]?[0-9]{1,5}[-\\ ]?[0-9]+[-\\ ]?[0-9]+[-\\ ]?[0-9]$";
    public static final String BOOK_PAGES_REGEX = "^(?:[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$";
}
