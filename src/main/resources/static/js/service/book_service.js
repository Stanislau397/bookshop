function getAuthorsByBookId(book_id) {
    let authors_by_book_id = [];
    $.ajax({
        url: '/findAuthorsByBookId',
        data: {bookId: book_id},
        async: false,
        success: function (authorsByBookId) {
            authors_by_book_id = authorsByBookId;
            return authors_by_book_id;
        }
    })
    return authors_by_book_id;
}

function getBookScoreByUserIdAndBookId(user_id, book_id) {
    let book_score_for_user = 0;
    $.ajax({
        url: '/findBookScoreForUser',
        data: {
            userId: user_id,
            bookId: book_id
        },
        async: false,
        success: function (bookScoreForUser) {
            book_score_for_user = bookScoreForUser;
            return book_score_for_user;
        }
    })
    return book_score_for_user;
}

function editBookStatusOnShelveByBookIdAndShelveId(new_book_status, book_id, shelve_id) {
    let shelveBookStatusChanged = false;
    $.ajax({
        method: 'POST',
        url: '/updateBookStatusOnShelve',
        data: {
            bookStatus: new_book_status,
            bookId: book_id,
            shelveId: shelve_id
        },
        async: false,
        success: function () {
            shelveBookStatusChanged = true;
            return shelveBookStatusChanged;
        }
    })
    return shelveBookStatusChanged;
}

function deleteShelveBookByShelveIdAndBookId(shelve_id, book_id) {
    let bookDeletedFromShelve = false;
    $.ajax({
        method: 'POST',
        url: '/deleteShelveBookFromBookShelve',
        data: {
            shelveId: shelve_id,
            bookId: book_id
        },
        async: false,
        success: function () {
            bookDeletedFromShelve = true;
            return bookDeletedFromShelve;
        }
    })
    return bookDeletedFromShelve;
}

function getAverageBookScoreByBookId(book_id) {
    let average_book_score = 0;
    $.ajax({
        url: '/findAverageBookReviewScore',
        data: {bookId: book_id},
        async: false,
        success: function (averageScore) {
            average_book_score = averageScore;
            return average_book_score;
        }
    })
    return average_book_score;
}

function getLocalizedBooksByLanguageAndPage(language_name, page_number) {
    let localized_books = '';
    $.ajax({
        url: '/displayAllLocalizedBooksByLanguageAndPageNumber',
        data: {
            languageName: language_name,
            pageNumber: page_number
        },
        async: false,
        success: function (localizedBooks) {
            localized_books = localizedBooks;
            return localized_books;
        }
    })
    return localized_books;
}

function getBookByLocalizedBookTitle(book_title) {
    let book = '';
    $.ajax({
        url: '/getBookByLocalizedBookTitle',
        data: {title: book_title},
        async: false,
        success: function (found_book) {
            book = found_book;
            return book;
        }
    })
    return book;
}

function getLocalizedBookByTitle(book_title) {
    let localized_book_details = '';
    $.ajax({
        url: '/findLocalizedBookByTitleAndLanguage',
        data: {
            title: book_title
        },
        async: false,
        success: function (foundLocalizedBookDetails) {
            localized_book_details = foundLocalizedBookDetails;
            return localized_book_details;
        }
    })
    return localized_book_details;
}