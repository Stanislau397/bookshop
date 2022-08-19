package edu.epam.bookshop.service.impl;

import edu.epam.bookshop.entity.BookStatus;
import edu.epam.bookshop.exception.EntityNotFoundException;
import edu.epam.bookshop.exception.NothingFoundException;
import edu.epam.bookshop.repository.BookRepository;
import edu.epam.bookshop.repository.BookShelveRepository;
import edu.epam.bookshop.service.BookShelveService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;

import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_NOT_FOUND_ON_SHELVE;
import static edu.epam.bookshop.constant.ExceptionMessage.BOOK_WITH_GIVEN_ID_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.ENUM_NOT_FOUND_MSG;
import static edu.epam.bookshop.constant.ExceptionMessage.SHELVE_WITH_GIVEN_ID_NOT_FOUND;

@Service
@AllArgsConstructor
@Slf4j
public class BookShelveServiceImpl implements BookShelveService {

    private BookShelveRepository bookShelveRepository;
    private BookRepository bookRepository;

    @Override
    public boolean checkIfBookExistsInBookShelve(Long shelveId, Long bookId) {
        if (!bookShelveRepository.existsByShelveId(shelveId)) {
            log.info(SHELVE_WITH_GIVEN_ID_NOT_FOUND, shelveId);
            throw new EntityNotFoundException(
                    String.format(SHELVE_WITH_GIVEN_ID_NOT_FOUND, shelveId)
            );
        }
        if (!bookRepository.existsById(bookId)) {
            log.info(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId);
            throw new EntityNotFoundException(
                    String.format(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId)
            );
        }
        return bookShelveRepository.bookExistsByShelveIdAndBookId(shelveId, bookId);
    }

    @Override
    public void addBookToShelve(Long shelveId, Long bookId) {
        if (!bookShelveRepository.existsByShelveId(shelveId)) {
            log.info(SHELVE_WITH_GIVEN_ID_NOT_FOUND, shelveId);
            throw new EntityNotFoundException(
                    String.format(SHELVE_WITH_GIVEN_ID_NOT_FOUND, shelveId)
            );
        }
        if (!bookRepository.existsById(bookId)) {
            log.info(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId);
            throw new EntityNotFoundException(
                    String.format(BOOK_WITH_GIVEN_ID_NOT_FOUND, bookId)
            );
        }
        BookStatus wantToRead = BookStatus.WANT_TO_READ;
        bookShelveRepository.insertBookToShelveByShelveIdAndBookIdAndBookStatus(
                shelveId,
                bookId,
                String.valueOf(wantToRead)
        );
    }

    @Override
    public void changeBookStatusByBookIdAndShelveId(String newStatus, Long bookId, Long shelveId) {
        if (!bookShelveRepository.bookExistsByShelveIdAndBookId(shelveId, bookId)) {
            log.info(BOOK_NOT_FOUND_ON_SHELVE, bookId, shelveId);
            throw new NothingFoundException(
                    String.format(BOOK_NOT_FOUND_ON_SHELVE, bookId, shelveId)
            );
        }
        if (!EnumUtils.isValidEnum(BookStatus.class, newStatus)) {
            log.info(ENUM_NOT_FOUND_MSG, newStatus);
            throw new EntityNotFoundException(String.format(ENUM_NOT_FOUND_MSG, newStatus));
        }
        bookShelveRepository.updateBookStatusByShelveIdAndBookId(newStatus, shelveId, bookId);
    }

    @Override
    public void removeShelveBookFromBookShelve(Long shelveId, Long bookId) {
        if (!bookShelveRepository.bookExistsByShelveIdAndBookId(shelveId, bookId)) {
            log.info(BOOK_NOT_FOUND_ON_SHELVE, bookId, shelveId);
            throw new NothingFoundException(
                    String.format(BOOK_NOT_FOUND_ON_SHELVE, bookId, shelveId)
            );
        }
        bookShelveRepository.deleteShelveBookByShelveIdAndBookId(shelveId, bookId);
    }

    @Override
    public Integer findNumberOfBooksOnShelveByShelveIdAndBookStatus(Long shelveId, String bookStatus) {
        if (!bookShelveRepository.existsByShelveId(shelveId)) {
            log.info(SHELVE_WITH_GIVEN_ID_NOT_FOUND, shelveId);
            throw new EntityNotFoundException(
                    String.format(SHELVE_WITH_GIVEN_ID_NOT_FOUND, shelveId)
            );
        }
        if (!EnumUtils.isValidEnum(BookStatus.class, bookStatus)) {
            log.info(ENUM_NOT_FOUND_MSG, bookStatus);
            throw new EntityNotFoundException(String.format(ENUM_NOT_FOUND_MSG, bookStatus));
        }
        BookStatus givenStatus = BookStatus.valueOf(bookStatus);
        return bookShelveRepository
                .selectCountBooksOnShelveByShelveIdAndBookStatus(shelveId, givenStatus)
                .orElse(0);
    }
}
