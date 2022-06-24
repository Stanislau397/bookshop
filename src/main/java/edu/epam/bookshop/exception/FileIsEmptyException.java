package edu.epam.bookshop.exception;

public class FileIsEmptyException extends RuntimeException {

    public FileIsEmptyException() {
    }

    public FileIsEmptyException(String message) {
        super(message);
    }

    public FileIsEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileIsEmptyException(Throwable cause) {
        super(cause);
    }
}
