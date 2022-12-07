let userForBooksByYear;

window.addEventListener('DOMContentLoaded', function () {
    let bookYear = new URLSearchParams(window.location.search).get('year');
    let pageNumber = new URLSearchParams(window.location.search).get('page');
    getBooksByYearAndPage(bookYear, pageNumber);
    hideBookYears();
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
            let bookYear = new URLSearchParams(window.location.search).get('year');
            let pageNumber = new URLSearchParams(window.location.search).get('page');
            getBooksByYearAndPage(bookYear, pageNumber);
        },
        error: function (error) {
            console.log(error.responseText);
        }
    })
}

function getUserByUsername(user_name) {
    $.ajax({
        url: '/findUserByUsername',
        data: {username: user_name},
        async: false,
        success: function (userByUsername) {
            userForBooksByYear = userByUsername;
            return userForBooksByYear;
        },
        error: function (exception) {

        }
    });
    return userForBooksByYear;
}

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
    let add_btn = document.getElementById('add').value;
    if (document.getElementById('user-name-h1') !== null) {
        let user_name = document.getElementById('user-name-h1').innerText;
        userForBooksByYear = getUserByUsername(user_name);
    }
    books_by_year_container.innerHTML = '';
    let counter = 0;
    for (let localizedBook of booksByYear) {
        counter = counter + 1;
        let book_price = '$' + localizedBook.book.price;
        let book_href = 'http://localhost:8070/bookshop/book?id=' + localizedBook.book.bookId;
        let book_href_with_under_scores = book_href.replace(/ /g, "_")
        books_by_year_container.innerHTML +=
            '<div class="book-container">' +
            '<div class="book-image-container">' +
            '<a href="' + book_href_with_under_scores + '">' + '<img class="book-image" src="' + localizedBook.imagePath + '"/>' + '</a>' +
            '</div>' +
            '<div class="book-info">' +
            '<a class="book-title" href="' + book_href_with_under_scores + '">' + localizedBook.title + '</a>' +
            '<div class="book-author-name" id="author_name' + counter + '">' + '</div>' +
            '<p class="book-price">' + book_price + '</p>' +
            '</div>' +
            '<div class="button-container" id="button_container' + counter + '">' + '</div>' + '</div>';
        let button_container_div = document.getElementById('button_container' + counter);
        if (userForBooksByYear != null) {
            let shelveId = userForBooksByYear.bookShelve.bookShelveId;
            displayButton(localizedBook.book.bookId, shelveId, localizedBook.title, button_container_div)
        } else {
            button_container_div.innerHTML =
                '<button type="button" ' +
                'class="book-shelve-btn">' +
                '<i class="fa fa-plus">' + '</i>' +
                '<a href="http://localhost:8070/bookshop/login">' + add_btn + '</a>' + '</button>'
        }
        setAuthorsInBookInfo(localizedBook.book.authors, counter);
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