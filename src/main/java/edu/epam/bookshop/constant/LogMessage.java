package edu.epam.bookshop.constant;

import java.util.ResourceBundle;

public class LogMessage {

    private LogMessage() {

    }

    public static final String TRYING_TO_FIND_TOP_15_BOOKS_WHERE_SCORE_GREATER_THAN =
            ResourceBundle.getBundle("messages")
                    .getString("before_find_top_15_books_with_score_greater_than");

    public static final String FOUND_TOP_15_BOOKS_WHERE_SCORE_GREATER_THAN =
            ResourceBundle.getBundle("messages")
                    .getString("after_find_top_15_books_with_score_greater_than");
}
