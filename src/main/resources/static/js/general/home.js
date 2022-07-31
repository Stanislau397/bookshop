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

function changeBookStatusOnShelve() {
    let radio_input = document.querySelector('input[name="choice"]:checked').value;
    let book_id = document.getElementById('book_id').value;
    let shelve_id = document.getElementById('shelve_id').value;
    $.ajax({
        method: 'POST',
        url: '/updateBookStatusOnShelve',
        data: {
            bookStatus: radio_input,
            bookId: book_id,
            shelveId: shelve_id
        },
        success: function () {
            hideEditBookStatusModal();
        },
        error: function (error) {
            console.log(error.responseText);
        }
    })
    console.log(radio_input);
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

function getBookStatusOnShelve(book_id, shelve_id) {
    let foundStatus;
    $.ajax({
        url: '/findBookStatusOnShelveByShelveIdAndBookId',
        data: {
            bookId: book_id,
            shelveId: shelve_id
        },
        async: false,
        success: function (bookStatus) {
            foundStatus = bookStatus;
            return foundStatus;
        },
        error: function (error) {
            console.log(error.responseText);
        }
    })
    return foundStatus;
}

function displayHighScoreBooks(highScoreBooks) {
    let highScoreBooksContent = document.getElementById('high_score_books_content');
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
                'class="wish-list-btn" id="wish_list_btn">' +
                '<i class="fa fa-plus">' + '</i>' + add_btn + '</button>'
        }
    }
}

function displayTotalBooksWithHighScore(totalBooks) {
    let total_books_a = document.getElementById('high_score_books_number');
    total_books_a.innerText += ' ' + totalBooks;
}

function displayButton(book_id, shelve_id, book_title, button_container) {
    let add_btn = document.getElementById('add').value;
    let change_btn = document.getElementById('change').value;
    let bookExists = checkIfBookExistsInShelve(book_id, shelve_id);
    if (bookExists) {
        button_container.innerHTML =
            '<button type="button" ' +
            'data-bs-toggle="modal" ' +
            'data-bs-target="#editBookStatusModal" ' +
            'onclick="setBookShelveInfoInModalHeader(\'' + shelve_id + '\',\'' + book_id + '\',\'' + book_title + '\')" ' +
            'class="change-book-status-btn" id="change_book_status_btn"> ' +
            '<i class="fa fa-check">' + '</i>' + change_btn + '</button>'
    } else {
        button_container.innerHTML =
            '<button type="button" ' +
            'onclick="addBookToShelve(\'' + book_id + '\',\'' + "WANT_TO_READ" + '\')" ' +
            'class="wish-list-btn" id="wish_list_btn">' +
            '<i class="fa fa-plus">' + '</i>' + add_btn + '</button>'
    }
}

function setBookShelveInfoInModalHeader(shelve_id, book_id, book_title) {
    let book_title_h5 = document.getElementById('book_title_in_modal_header');
    book_title_h5.innerText = book_title;
    let shelve_id_input = document.getElementById('shelve_id');
    shelve_id_input.value = shelve_id;
    let book_id_input = document.getElementById('book_id');
    book_id_input.value = book_id;
    let book_status = getBookStatusOnShelve(book_id, shelve_id);
    $("input[name=choice][value=" + book_status + "]").prop('checked', true);
}

function hideEditBookStatusModal() {
    $('#editBookStatusModal').modal('hide');
}
