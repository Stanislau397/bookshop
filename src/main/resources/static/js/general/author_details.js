window.addEventListener('DOMContentLoaded', function () {
    let authorId = new URLSearchParams(window.location.search).get('authorId');
    getAuthorDetailsById(authorId);
});

function getAuthorDetailsById(id) {
    $.ajax({
        url: '/findAuthorInfoById',
        data: {authorId: id},
        success: function (authorInfo) {
            setAuthorImage(authorInfo);
            setFirstnameAndLastname(authorInfo);
            setBooksCountField(authorInfo);
            setBirthDate(authorInfo);
            setBiography(authorInfo);
            displayAuthorBooks(authorInfo.books);
        },
        error: function (exception) {

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
    birth_date_li.innerText = author.birthDate;
}

function setBiography(author) {
    let biography_li = document.getElementById('biography');
    biography_li.innerText = author.biography;
}

function displayAuthorBooks(authorBooks) {
    let author_books_container = document.getElementById('author_books');
    let authorName = document.getElementById('first_name_and_last_name').innerText;
    author_books_container.innerHTML = '';
    for (let book of authorBooks) {
        let book_price = '$' + book.price;
        author_books_container.innerHTML +=
            '<div class="book-container">' +
            '<div class="book-image-container">' +
            '<img class="book-image" src="' + book.imagePath + '"/>' +
            '</div>' +
            '<div class="book-info">' +
            '<a class="book-title">' + book.title + '</a>' +
            '<p class="book-author-name">' + authorName + '</p>' +
            '<p class="book-price">' + book_price + '</p>' +
            '</div>' +
            '<button type="button" ' +
            'class="add-to-wishlist-btn">' + 'Add to wishlist' + '</button>' +
            '</div>';
    }
}