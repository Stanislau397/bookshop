window.addEventListener('DOMContentLoaded', function () {
    const gap = 16;

    let carousel = document.getElementById("high_score_books_carousel");
    let content = document.getElementById("high_score_books_content");
    let next = document.getElementById('next_item_for_high_score_books');
    let prev = document.getElementById("prev_item_for_high_score_books");

    next.addEventListener("click", e => {
        carousel.scrollBy(width + gap, 0);
        if (carousel.scrollWidth !== 0) {
            prev.style.display = "flex";
        }
        if (content.scrollWidth - width - gap <= carousel.scrollLeft + width) {
            next.style.display = "none";
        }
    });
    prev.addEventListener("click", e => {
        carousel.scrollBy(-(width + gap), 0);
        if (carousel.scrollLeft - width - gap <= 0) {
            prev.style.display = "none";
        }
        if (!content.scrollWidth - width - gap <= carousel.scrollLeft + width) {
            next.style.display = "flex";
        }
    });

    let width = carousel.offsetWidth;
    window.addEventListener("resize", e => (width = carousel.offsetWidth));
    getBooksWithHighScoreLimit15();
    getAmountOfBooksWithHighScore();
});

function addBookToShelve(book_id) {
    let shelve_id = document.getElementById('shelve_id').value;
    $.ajax({
        method: 'POST',
        url: '/addBookToShelve',
        data: {
            bookId: book_id,
            shelveId: shelve_id
        },
        success: function () {
            getBooksWithHighScoreLimit15();
        },
        error: function (error) {
            console.log(error.responseText);
        }
    })
}

function getBooksWithHighScoreLimit15() {
    let book_score = 4;
    $.ajax({
        url: '/findBooksWithHighScoreLimit15',
        data: {score: book_score},
        success: function (booksWithHighScoreLimit15) {
            displayHighScoreBooks(booksWithHighScoreLimit15);
        }
    })
}

function getAuthorByBookId(book_id) {
    let author = '';
    $.ajax({
        url: '/findAuthorsByBookId',
        data: {bookId: book_id},
        async: false,
        success: function (authorByBookId) {
            author = authorByBookId;
            return author;
        }
    })
    return author;
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

function getAmountOfBooksWithHighScore() {
    let highScore = 4;
    $.ajax({
        url: '/countBooksWithScoreGreaterThan',
        data: {score: highScore},
        async: false,
        success: function (amountOfBooks) {
            displayTotalBooksWithHighScore(amountOfBooks);
        }
    })
}

function displayHighScoreBooks(highScoreBooks) {
    let highScoreBooksContent = document.getElementById('high_score_books_content');
    highScoreBooksContent.innerHTML = '';
    let add_btn = document.getElementById('add').value;
    let counter = 0;
    for (let book of highScoreBooks) {
        let author = getAuthorByBookId(book.bookId);
        let averageScore = getAverageBookScoreByBookId(book.bookId);
        let bookHref = 'http://localhost:8070/bookshop/book?title=' + book.title.replace(/ /g, "_");
        let authorHref = '';
        let firstName = '';
        let lastName = '';
        counter = counter + 1;
        if (author !== '') {
            firstName = author[0].firstName;
            lastName = author[0].lastName;
            authorHref = 'http://localhost:8070/bookshop/author?authorId=' + author[0].authorId;
        }
        highScoreBooksContent.innerHTML +=
            '<div class="item">' +
            '<div class="image-container">' +
            '<a href="' + bookHref + '" class="image-href">' + '<div class="book-score">' + averageScore + '</div>' +
            '<img class="book-image" src="' + book.imagePath + '">' + '</a>' + '</div>' +
            '<div class="book-info-container">' + '<div class="book-title-container">' +
            '<a id="book_title" href="' + bookHref + '">' + book.title + '</a>' + '</div>' +
            '<div class="author-container">' +
            '<a id="book_author" href="' + authorHref + '">' + firstName + ' ' + lastName + '</a>' + '</div>' +
            '<div class="price">' + book.price + '</div>' + '</div>' +
            '<div class="button-container" id="button_container' + counter + '">' + '</div>' + '</div>';
        let button_container_div = document.getElementById('button_container' + counter);
        if (user != null) {
            let shelveId = user.bookShelve.bookShelveId;
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
function displayTotalBooksWithHighScore(totalBooks) {
    let total_books_a = document.getElementById('high_score_books_number');
    total_books_a.innerText += ' ' + totalBooks;
}
