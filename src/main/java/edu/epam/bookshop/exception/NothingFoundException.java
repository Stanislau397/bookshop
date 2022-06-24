package edu.epam.bookshop.exception;

public class NothingFoundException extends RuntimeException {

    public NothingFoundException() {
    }

    public NothingFoundException(String message) {
        super(message);
    }

    public NothingFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NothingFoundException(Throwable cause) {
        super(cause);
    }
}
