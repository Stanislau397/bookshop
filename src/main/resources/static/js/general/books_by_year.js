window.addEventListener('DOMContentLoaded', function () {
    let bookYear = new URLSearchParams(window.location.search).get('year');
    let pageNumber = new URLSearchParams(window.location.search).get('page');
    getBooksByYearAndPage(bookYear, pageNumber);
    hideBookYears();
});

function getBooksByYearAndPage(book_year, page_number) {
    let url = window.location.href.split('?')[0] + "?year=" + book_year + "&page=" + page_number;
    history.pushState(undefined, '', url);
    setYearBtn(book_year);
    $.ajax({
        url: '/findBooksByYearAndPageNumber',
        data: {
            year: book_year,
            page: page_number
        },
        success: function (booksByYear) {
            displayBooksByYear(booksByYear.content);
        },
        error : function (exception) {
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

function getExistingYears() {
    $.ajax({
        url : '/findExistingYearsInBooks',
        success : function (years) {
            displayExistingYearsInDiv(years);
        }
    })
}

function displayBooksByYear(booksByYear) {
    let books_by_year_container = document.getElementById('books_by_year');
    let add_to_wishlist = document.getElementById('add_to_wishlist').value;
    books_by_year_container.innerHTML = '';
    let counter = 0;
    for (let book of booksByYear) {
        counter = counter + 1;
        let book_price = '$' + book.price;
        let book_href = 'http://localhost:8070/bookshop/book?title=' + book.title;
        let book_href_with_under_scores = book_href.replace(/ /g, "_")
        books_by_year_container.innerHTML +=
            '<div class="book-container">' +
            '<div class="book-image-container">' +
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
        getAuthorsForBookByBookId(book.bookId, counter);
    }
}

function displayExistingYearsInDiv(years) {
    let targetDiv = document.getElementById("all_years");
    targetDiv.innerHTML = '';
    if (targetDiv.style.display !== "none") {
        targetDiv.style.display = "none";
    } else {
        targetDiv.style.display = 'block'
        addExistingYearsToDiv(years, targetDiv);
    }
}

function addExistingYearsToDiv(years, year_div) {
    for (let year of years) {
        year_div.innerHTML +=
            '<div class="book-year">' +
            '<button class="book-year-btn" ' +
            'value="' + year + '" ' +
            'onclick="getBooksByYearAndPage(this.value, ' + 1 + ')">' + year + '</button>' +
            '</div>';
    }
}

function setYearBtn(year) {
    let year_btn = document.getElementById('year_btn');
    year_btn.innerText = year;
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

function hideBookYears() {
    let book_years_div = document.getElementById('all_years');
    book_years_div.style.display = 'none';
}

function displayErrorMessage(exception) {
    let books_by_year_container = document.getElementById('books_by_year');
    books_by_year_container.innerHTML = '';
    let jsonResponse = JSON.parse(exception);
    books_by_year_container.innerHTML += '<p class="error-message">' + jsonResponse['message'] + '</p>';
}