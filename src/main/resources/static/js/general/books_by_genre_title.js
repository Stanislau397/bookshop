let userForBooksByGenre;

window.addEventListener('DOMContentLoaded', function () {
    let genreTitle = new URLSearchParams(window.location.search).get('genreTitle');
    let pageNumber = new URLSearchParams(window.location.search).get('page');
    hideAllGenres();
    setPageTitle(genreTitle);
    setGenreButton(genreTitle);
    getBooksByGenreTitleAndPage(genreTitle, pageNumber);
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
            let genreTitle = new URLSearchParams(window.location.search).get('genreTitle');
            let pageNumber = new URLSearchParams(window.location.search).get('page');
            getBooksByGenreTitleAndPage(genreTitle, pageNumber);
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
            userForBooksByGenre = userByUsername;
            return userForBooksByGenre;
        },
        error: function (exception) {

        }
    });
    return userForBooksByGenre;
}

function getBooksByGenreTitleAndPage(genre_title, page_number) {
    let url = window.location.href.split('?')[0] + "?genreTitle=" + genre_title + "&page=" + page_number;
    history.pushState(undefined, '', url);
    setGenreButton(genre_title);
    $.ajax({
        url: '/findBooksByGenreTitleAndPageNumber',
        data: {
            genreTitle: genre_title,
            page: page_number
        },
        success: function (booksByGenreTitle) {
            displayBooksByGenreTitle(booksByGenreTitle.content);
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

function getLocalizedGenres() {
    $.ajax({
        url: '/findLocalizedGenres',
        success: function (localizedGenres) {
            displayAllGenresInDiv(localizedGenres);
        }
    })
}

function displayBooksByGenreTitle(booksByGenreTitle) {
    if (document.getElementById('user-name-h1') !== null) {
        let user_name = document.getElementById('user-name-h1').innerText;
        userForBooksByGenre = getUserByUsername(user_name);
    }
    let books_by_genre_container = document.getElementById('books_by_genre_title');
    let add_btn = document.getElementById('add').value;
    books_by_genre_container.innerHTML = '';
    let counter = 0;
    for (let book of booksByGenreTitle) {
        counter = counter + 1;
        let localized_book = book.localizedBook;
        let book_price = '$' + book.price;
        let book_href = 'http://localhost:8070/bookshop/book?id=' + book.bookId;
        let book_href_with_under_scores = book_href.replace(/ /g, "_")
        books_by_genre_container.innerHTML +=
            '<div class="book-container">' +
            '<div class="book-image-container">' +
            '<a href="' + book_href_with_under_scores + '">' + '<img class="book-image" src="' + localized_book.imagePath + '"/>' + '</a>' +
            '</div>' +
            '<div class="book-info">' +
            '<a class="book-title" href="' + book_href_with_under_scores + '">' + localized_book.title + '</a>' +
            '<div class="book-author-name" id="author_name' + counter + '">' + '</div>' +
            '<p class="book-price">' + book_price + '</p>' +
            '</div>' +
            '<div class="button-container" id="button_container' + counter + '">' + '</div>' + '</div>';
        let button_container_div = document.getElementById('button_container' + counter);
        getAuthorsForBookByBookId(book.bookId, counter);
        if (userForBooksByGenre != null) {
            let shelveId = userForBooksByGenre.bookShelve.bookShelveId;
            displayButton(book.bookId, shelveId, localized_book.title, button_container_div)
        } else {
            button_container_div.innerHTML =
                '<button type="button" ' +
                'class="book-shelve-btn">' +
                '<i class="fa fa-plus">' + '</i>' +
                '<a href="http://localhost:8070/bookshop/login">' + add_btn + '</a>' + '</button>'
        }
    }
}

function setPageTitle(title) {
    let page_title = document.getElementById('title');
    page_title.innerText = title;
}

function setGenreButton(genre_title) {
    let genre_button = document.getElementById('genre-btn');
    genre_button.innerText = genre_title;
}

function displayAllGenresInDiv(genres) {
    let targetDiv = document.getElementById("all_genres");
    targetDiv.innerHTML = '';
    if (targetDiv.style.display !== "none") {
        targetDiv.style.display = "none";
    } else {
        targetDiv.style.display = 'block'
        addGenresToGenresDiv(genres, targetDiv);
    }
}

function addGenresToGenresDiv(localizedGenres, genres_div) {
    for (let localizedGenre of localizedGenres) {
        let genreTitle = localizedGenre.title;
        genres_div.innerHTML +=
            '<div class="genre-title">' +
            '<button class="genre-title-btn" ' +
            'value="' + genreTitle + '" ' +
            'onclick="getBooksByGenreTitleAndPage(this.value, ' + 1 + ')">' + genreTitle + '</button>' +
            '</div>';
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

function hideAllGenres() {
    let all_genres_div = document.getElementById('all_genres');
    all_genres_div.style.display = 'none';
}

function displayErrorMessage(exception) {
    let books_by_genre_container = document.getElementById('books_by_genre_title');
    books_by_genre_container.innerHTML = '';
    let jsonResponse = JSON.parse(exception);
    books_by_genre_container.innerHTML += '<p class="error-message">' + jsonResponse['message'] + '</p>';
}