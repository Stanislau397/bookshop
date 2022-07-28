window.addEventListener('DOMContentLoaded', function () {
    let keyword = new URLSearchParams(window.location.search).get('keyWord');
    let pageNumber = new URLSearchParams(window.location.search).get('page');
    getBooksByKeyWordAndPage(keyword, pageNumber);
});

function getBooksByKeyWordAndPage(key_word, page_number) {
    $.ajax({
        url: '/findBooksByKeyWordAndPage',
        data: {
            keyWord: key_word,
            page: page_number
        },
        success: function (booksByKeyWordAndPage) {
            displayBooksByKeyWordAndPage(booksByKeyWordAndPage.content);
            addNumberOfElementsToSearchResults(booksByKeyWordAndPage.numberOfElements);
        },
        error: function (exception) {
            displayErrorMessage(exception.responseText);
        }
    })
}

function getAuthorsForBookByBookId(book_id, counter) {
    $.ajax({
        url: '/findAuthorsByBookId',
        data: {bookId: book_id},
        success: function (authors) {
            setAuthorsInBookInfo(authors, counter);
        }
    })
}

function getAverageScoreForBookByKeyWord(book_id) {
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

function displayBooksByKeyWordAndPage(booksByKeyWordAndPage) {
    let books_by_keyword_container = document.getElementById('books_by_keyword');
    let add_to_wishlist = document.getElementById('add_to_wishlist').value;
    books_by_keyword_container.innerHTML = '';
    let counter = 0;
    for (let book of booksByKeyWordAndPage) {
        counter = counter + 1;
        let averageScore = getAverageScoreForBookByKeyWord(book.bookId);
        let book_price = '$' + book.price;
        let book_href = 'http://localhost:8070/bookshop/book?title=' + book.title;
        let book_href_with_under_scores = book_href.replace(/ /g, "_")
        books_by_keyword_container.innerHTML +=
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
        let score_div = document.getElementById('book_score' + counter);
        getAuthorsForBookByBookId(book.bookId, counter);
        setBooksByKeyWordScore(averageScore, score_div)
    }
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

function displayErrorMessage(exception) {
    let books_by_genre_container = document.getElementById('books_by_keyword');
    books_by_genre_container.innerHTML = '';
    let jsonResponse = JSON.parse(exception);
    books_by_genre_container.innerHTML += '<p class="error-message">' + jsonResponse['message'] + '</p>';
}

function addNumberOfElementsToSearchResults(key_word) {
    let search_results = document.getElementById('result_title');
    search_results.innerText += ' (' + key_word + ')';
}

function setBooksByKeyWordScore(score, score_div) {
    if (score > 0 && score < 3) {
        score_div.style.backgroundColor = 'red';
        score_div.innerText = score;
    } else if (score >= 3 && score < 4) {
        score_div.style.backgroundColor = '#fc3';
        score_div.innerText = score;
    } else if (score >= 4) {
        score_div.style.backgroundColor = '#6c3';
        score_div.innerText = score;
    } else {
        score_div.style.backgroundColor = 'silver';
        score_div.innerText = 'tbh';
    }
}