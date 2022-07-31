let author;
let userForAuthorBooks;

window.addEventListener('DOMContentLoaded', function () {
    let authorId = new URLSearchParams(window.location.search).get('authorId');
    getAuthorDetailsById(authorId);
    getAuthorGenresByAuthorId(authorId);
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
            displayAuthorBooks(author.books);
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
            userForAuthorBooks = userByUsername;
            return userForAuthorBooks;
        },
        error: function (exception) {

        }
    });
    return userForAuthorBooks;
}

function getAuthorDetailsById(id) {
    $.ajax({
        url: '/findAuthorInfoById',
        data: {authorId: id},
        async: false,
        success: function (authorInfo) {
            setTitle(authorInfo);
            setAuthorImage(authorInfo);
            setFirstnameAndLastname(authorInfo);
            setBooksCountField(authorInfo);
            setBirthDate(authorInfo);
            setBiography(authorInfo);
            displayAuthorBooks(authorInfo.books);
            author = authorInfo;
            return author;
        },
        error: function (exception) {

        }
    })
    return author;
}

function getAuthorGenresByAuthorId(author_id) {
    $.ajax({
        url: '/findGenresForAuthor',
        data: {authorId: author_id},
        success: function (authorGenres) {
            displayAuthorGenres(authorGenres);
        }
    })
}

function setAuthorImage(author) {
    let image = document.getElementById('author-image');
    image.src = author.imagePath;
}

function setFirstnameAndLastname(author) {
    let authorNameH2 = document.getElementById('first_name_and_last_name');
    authorNameH2.innerText = author.firstName + " " + author.lastName;
}

function setBooksCountField(author) {
    let books_count = document.getElementById('books-count');
    books_count.innerText = author.books.length;
}

function setBirthDate(author) {
    let birth_date_li = document.getElementById('birth_date');
    let birthDate = author.birthDate;
    birth_date_li.innerText = birthDate.split('-').reverse().join('-');
}

function setBiography(author) {
    let biography_li = document.getElementById('biography');
    biography_li.innerText = author.biography;
}

function setTitle(author) {
    let title = document.getElementById('author-name');
    title.innerText = author.firstName + " " + author.lastName;
}

function displayAuthorGenres(authorGenres) {
    let author_genres_li = document.getElementById('author_genres');
    let counter = 0;
    for (let genre of authorGenres) {
        let booksByGenreHref = 'http://localhost:8070/bookshop/booksByGenre?genreTitle=' + genre.title + '&page=1';
        if (authorGenres.length - 1 !== counter) {
            author_genres_li.innerHTML +=
                '<a class="books-by-genre" href="' + booksByGenreHref + '">' + genre.title + ', ' + '</a>';
        } else {
            author_genres_li.innerHTML +=
                '<a class="books-by-genre" href="' + booksByGenreHref + '">' + genre.title + '</a>';
        }
        counter = counter + 1;
    }
}

function displayAuthorBooks(authorBooks) {
    if (document.getElementById('user-name-h1') !== null) {
        let user_name = document.getElementById('user-name-h1').innerText;
        userForAuthorBooks = getUserByUsername(user_name);
    }
    let author_books_container = document.getElementById('author_books');
    let authorName = document.getElementById('first_name_and_last_name').innerText;
    let add_btn = document.getElementById('add').value;
    author_books_container.innerHTML = '';
    let counter = 0;
    for (let book of authorBooks) {
        counter = counter + 1;
        let book_price = '$' + book.price;
        let book_href = 'http://localhost:8070/bookshop/book?title=' + book.title;
        let book_href_with_under_scores = book_href.replace(/ /g, "_")
        author_books_container.innerHTML +=
            '<div class="book-container">' +
            '<div class="book-image-container">' +
            '<a href="' + book_href_with_under_scores + '">' + '<img class="book-image" src="' + book.imagePath + '"/>' + '</a>' +
            '</div>' +
            '<div class="book-info">' +
            '<a class="book-title" href="' + book_href_with_under_scores + '">' + book.title + '</a>' +
            '<p class="book-author-name">' + authorName + '</p>' +
            '<p class="book-price">' + book_price + '</p>' +
            '</div>' +
            '<div class="button-container" id="button_container' + counter + '">' + '</div>' + '</div>';
        let button_container_div = document.getElementById('button_container' + counter);
        if (userForAuthorBooks != null) {
            let shelveId = userForAuthorBooks.bookShelve.bookShelveId;
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