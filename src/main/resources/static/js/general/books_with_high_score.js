let booksWithHighScore;

window.addEventListener('DOMContentLoaded', function () {
    let pageNumber = new URLSearchParams(window.location.search).get('page');
    booksWithHighScore = getBooksByPageWithHighScore(pageNumber);
    appendTotalElementsToH1(booksWithHighScore.totalElements);
});

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
            console.log(booksWithHighScore)
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
    let books_with_high_score_container = document.getElementById('high_score_books');
    let add_to_wishlist = document.getElementById('add_to_wishlist').value;
    books_with_high_score_container.innerHTML = '';
    let counter = 0;
    for (let book of booksWithHighScore) {
        counter = counter + 1;
        let book_price = '$' + book.price;
        let book_href = 'http://localhost:8070/bookshop/book?title=' + book.title;
        let book_href_with_under_scores = book_href.replace(/ /g, "_")
        books_with_high_score_container.innerHTML +=
            '<div class="book-container">' +
            '<div class="book-image-container">' +
            '<div class="book-score" id="book_score' + counter + '">' + '</div>' +
            '<a href="' + book_href_with_under_scores + '">' + '<img class="book-image" src="' + book.imagePath + '"/>' + '</a>' +
            '</div>' +
            '<div class="book-info">' +
            '<a class="book-title" href="' + book_href_with_under_scores + '">' + book.title + '</a>' +
            '<div class="book-author-name" id="author_name' + counter + '">' + '</div>' +
            '<p class="book-price">' + book_price + '</p>' +
            '</div>' +
            '<button type="button" ' +
            'class="add-to-wishlist-btn">' + add_to_wishlist + '</button>' +
            '</div>';
        getAuthorsForHighScoreBook(book.bookId, counter);
        getAverageBookScoreByBookId(book.bookId, counter);
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