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
    $.ajax({
        method: 'POST',
        url: '/updateBookStatusOnShelve',
        data: {
            bookStatus: new_book_status,
            bookId: book_id,
            shelveId: shelve_id
        },
        success: function () {

        }
    })
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