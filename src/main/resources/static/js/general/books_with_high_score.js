let booksWithHighScore;
let userForBooksWithHighScore;

window.addEventListener('DOMContentLoaded', function () {
    let pageNumber = new URLSearchParams(window.location.search).get('page');
    booksWithHighScore = getBooksByPageWithHighScore(pageNumber);
    appendTotalElementsToH1(booksWithHighScore.totalElements);
});

function addBookToShelve(book_id, book_status) {
    let shelve_id = document.getElementById('shelve_id').value;
    $.ajax({
        method: 'POST',
        url: '/addBookToShelve',
        data: {
            bookId: book_id,
            shelveId: shelve_id,
            bookStatus: book_status
        },
        success: function () {
            let pageNumber = new URLSearchParams(window.location.search).get('page');
            getBooksByPageWithHighScore(pageNumber);
        },
        error: function (error) {
            console.log(error.responseText);
        }
    })
}

function getUserByUsername(user_name) {
    let user_by_username;
    $.ajax({
        url: '/findUserByUsername',
        data: {username: user_name},
        async: false,
        success: function (userByUsername) {
            userForBooksWithHighScore = userByUsername;
            return user_by_username;
        },
        error: function (exception) {

        }
    });
    return userForBooksWithHighScore;
}

function getBooksByPageWithHighScore(page_number) {
    let url = window.location.href.split('?')[0] + "?page=" + page_number;
    history.pushState(undefined, '', url);
    let high_score = 4;
    $.ajax({
        url: '/findBooksByPageHavingAvgScoreGreaterThan',
        data: {
            score: high_score,
            pageNumber: page_number
        },
        async: false,
        success: function (highScoreBooks) {
            booksWithHighScore = highScoreBooks;
            displayBooksWithHighScore(booksWithHighScore.content);
            buildPaginationForBooksWithHighScore(booksWithHighScore.totalPages);
            return booksWithHighScore;
        }
    })
    return booksWithHighScore;
}

function getAverageBookScoreByBookId(book_id, counter) {
    $.ajax({
        url: '/findAverageBookReviewScore',
        data: {bookId: book_id},
        success: function (averageScore) {
            setAverageScoreForBook(averageScore, counter);
        }
    })
}

function getAuthorsForHighScoreBook(book_id, counter) {
    $.ajax({
        url: '/findAuthorsByBookId',
        data: {bookId: book_id},
        success: function (authors) {
            setAuthorsInBookInfo(authors, counter);
        }
    })
}

function setAuthorsInBookInfo(authors, counter) {
    let authors_div = document.getElementById('author_name' + counter + '')
    for (let author of authors) {
        let firstName = author.firstName;
        let lastName = author.lastName;
        let authorId = author.authorId;
        authors_div.innerHTML +=
            '<a class="book-author" href="author?authorId=' + authorId + '">' +
            firstName + ' ' + lastName +
            '</a>' +
            '</br>';
    }
    return authors_div;
}

function setAverageScoreForBook(averageScore, counter) {
    let score_div = document.getElementById('book_score' + counter + '');
    score_div.innerText = averageScore;
}

function displayBooksWithHighScore(booksWithHighScore) {
    if (document.getElementById('user-name-h1') !== null) {
        let user_name = document.getElementById('user-name-h1').innerText;
        userForBooksWithHighScore = getUserByUsername(user_name);
    }
    let books_with_high_score_container = document.getElementById('high_score_books');
    let add_btn = document.getElementById('add').value;
    books_with_high_score_container.innerHTML = '';
    let counter = 0;
    for (let book of booksWithHighScore) {
        counter = counter + 1;
        let localized_book = book.localizedBook;
        let book_price = '$' + book.price;
        let book_href = 'http://localhost:8070/bookshop/book?title=' + book.title;
        let book_href_with_under_scores = book_href.replace(/ /g, "_")
        books_with_high_score_container.innerHTML +=
            '<div class="book-container">' +
            '<div class="book-image-container">' +
            '<div class="book-score" id="book_score' + counter + '">' + '</div>' +
            '<a href="' + book_href_with_under_scores + '">' + '<img class="book-image" src="' + localized_book.imagePath + '"/>' + '</a>' +
            '</div>' +
            '<div class="book-info">' +
            '<a class="book-title" href="' + book_href_with_under_scores + '">' + localized_book.title + '</a>' +
            '<div class="book-author-name" id="author_name' + counter + '">' + '</div>' +
            '<p class="book-price">' + book_price + '</p>' + '</div>' +
            '<div class="button-container" id="button_container' + counter + '">' + '</div>' + '</div>';
        let button_container_div = document.getElementById('button_container' + counter);
        getAuthorsForHighScoreBook(book.bookId, counter);
        getAverageBookScoreByBookId(book.bookId, counter);
        if (userForBooksWithHighScore != null) {
            let shelveId = userForBooksWithHighScore.bookShelve.bookShelveId;
            displayButton(book.bookId, shelveId, book.title, button_container_div)
        } else {
            button_container_div.innerHTML =
                '<button type="button" ' +
                'class="book-shelve-btn">' +
                '<i class="fa fa-plus">' + '</i>' +
                '<a href="http://localhost:8070/bookshop/login">' + add_btn + '</a>' + '</button>'
        }
    }
}

function buildPaginationForBooksWithHighScore(pages) {
    let pagination_ul = document.getElementById('high_score_books_pagination');
    let pages_title = document.getElementById('pages_title').value;
    pagination_ul.innerHTML = '';
    pagination_ul.innerHTML += '<h2 class="pages-title">' + pages_title + '</h2>'
    if (pages > 1) {
        for (let i = 0; i < pages; i++) {
            let page = i + 1;
            pagination_ul.innerHTML +=
                '<li class="page-item">' +
                '<button class="page-link shadow-none" ' +
                'onclick="getBooksByPageWithHighScore(' + page + ')">' + page + '</button>' +
                '</li>';
        }
    }
}

function appendTotalElementsToH1(totalElements) {

    let h1_title = document.getElementById('books_with_high_score_title');
    h1_title.innerText += ' (' + totalElements + ')';
}