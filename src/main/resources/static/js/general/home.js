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

function checkIfBookExistsInShelve(book_id, shelve_id) {
    let result;
    $.ajax({
        url: '/isBookExistsInShelve',
        data: {
            bookId: book_id,
            shelveId: shelve_id
        },
        async: false,
        success: function (bookExists) {
            result = bookExists;
            return result;
        },
        error: function (error) {
            console.log(error.responseText);
        }
    })
    return result;
}

function displayHighScoreBooks(highScoreBooks) {
    let add_btn = document.getElementById('add').value;
    let highScoreBooksContent = document.getElementById('high_score_books_content');
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
            '<button type="button" ' +
            'onclick="addBookToShelve(\'' + book.bookId + '\',\'' + "WANT_TO_READ" + '\')" ' +
            'class="add-to-wishlist-btn" id="wish_list_btn' + counter + '">' +
            '<i class="fa fa-plus">' + '</i>' + add_btn + '</button>' + '</div>';
        if (user != null) {
            let shelveId = user.bookShelve.bookShelveId;
            let btn = document.getElementById('wish_list_btn' + counter);
            changeWishListBtn(book.bookId, shelveId, btn)
        }
    }
}

function displayTotalBooksWithHighScore(totalBooks) {
    let total_books_a = document.getElementById('high_score_books_number');
    total_books_a.innerText += ' ' + totalBooks;
}

function changeWishListBtn(book_id, shelve_id, btn) {
    let bookExists = checkIfBookExistsInShelve(book_id, shelve_id);
    if (bookExists) {
        let change_input_value = document.getElementById('change').value;
        btn.style.background = 'white';
        btn.style.color = 'black'
        btn.innerHTML = '<i class="fa fa-check">' + '</i>' + change_input_value;
        btn.onclick = '';
    }
}
