package edu.epam.bookshop.service;

public interface BookShelveService {

    boolean checkIfBookExistsInBookShelve(Long shelveId, Long bookId);

    void addBookToShelve(Long shelveId, Long bookId);

    void changeBookStatusByBookIdAndShelveId(String newStatus, Long bookId, Long shelveId);

    void removeShelveBookFromBookShelve(Long shelveId, Long bookId);

    Integer findNumberOfBooksOnShelveByShelveIdAndBookStatus(Long shelveId, String bookStatus);
}
