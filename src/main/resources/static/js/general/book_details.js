window.addEventListener('DOMContentLoaded', function () {
    let bookTitleFromParameter = new URLSearchParams(window.location.search).get('title');
    let title = bookTitleFromParameter.replace(/_/g, ' ');
    getBookDetailsByTitle(decodeURI(title));
});

function getBookDetailsByTitle(bookTitle) {
    $.ajax({
        url : '/findBookDetails',
        data : {title : bookTitle},
        success : function (bookInfo) {
            setBookTitle(bookInfo);
            setBookImage(bookInfo);
            setBookTitle(bookInfo);
            setH1Title(bookInfo);
            getAuthorsByBookId(bookInfo.bookId);
            getPublishersByBookId(bookInfo.bookId);
            getGenresByBookId(bookInfo.bookId);
            setBookDescription(bookInfo);
            setProductDetails(bookInfo);
        },
        error : function (exception) {
            console.log(exception.responseText)
        }
    })
}

function getAuthorsByBookId(id) {
    $.ajax({
        url : '/findAuthorsByBookId',
        data : {bookId : id},
        success : function (authors) {
            setAuthors(authors);
        },
        error : function (exception) {
            console.log(exception.responseText);
        }
    })
}

function getPublishersByBookId(id) {
    $.ajax({
        url : '/findPublishersByBookId',
        data : {bookId: id},
        success : function (publishers) {
            setBookPublishers(publishers);
        },
        error : function (exception) {
            console.log(exception.responseText);
        }
    })
}

function getGenresByBookId(id) {
    $.ajax({
        url : '/findGenresByBookId',
        data : {bookId: id},
        success : function (genres) {
            setBookGenres(genres);
        },
        error : function (exception) {
            let book_genres_li = document.getElementById('book_genres');
            book_genres_li.innerText = '-';
        }
    })
}

function setBookTitle(bookInfo) {
    let title = document.getElementById('title');
    title.innerText = bookInfo.title;
}

function setBookImage(bookInfo) {
    let book_img = document.getElementById('book-img');
    book_img.src = bookInfo.imagePath;
}

function setH1Title(bookInfo) {
    let book_title_h1 = document.getElementById('book-title');
    book_title_h1.innerText = bookInfo.title;
}

function setAuthors(authors) {
    let author_div = document.getElementById('author');
    let author_span_name = document.getElementById('author_for_span').value;
    for (let i = 0; i < authors.length; i++) {
        let firstName = authors[i].firstName;
        let lastName = authors[i].lastName;
        if (i !== authors.length - 1) {
            author_div.innerHTML +=
                '<a class="author-name" href="author?authorId='+authors[i].authorId+'">' +
                firstName + ' ' + lastName + ', ' +
                '</a>';
        } else {
            author_div.innerHTML +=
                '<a class="author-name" href="author?authorId='+authors[i].authorId+'">' +
                firstName + ' ' + lastName +
                '</a>';
        }
    }
    author_div.innerHTML += '<span>' + author_span_name + '</span>';
}

function setBookDescription(bookInfo) {
    let description_p = document.getElementById('description');
    description_p.innerText = bookInfo.description;
}

function setProductDetails(bookInfo) {
    setBookPrice(bookInfo);
    setBookPublishYear(bookInfo);
    setBookPages(bookInfo);
    setBookCoverType(bookInfo);
    setBookIsbn(bookInfo);
}

function setBookIsbn(bookInfo) {
    let isbn_li = document.getElementById('book_isbn');
    isbn_li.innerText = bookInfo.isbn;
}

function setBookCoverType(bookInfo) {
    let cover_type_li = document.getElementById('book_cover_type');
    cover_type_li.innerText = bookInfo.coverType.toLowerCase();
}

function setBookPrice(bookInfo) {
    let book_price_li = document.getElementById('book_price');
    book_price_li.innerText = '$' + bookInfo.price;
}

function setBookPublishYear(bookInfo) {
    let book_year_li = document.getElementById('publish_year');
    book_year_li.innerText = bookInfo.publishDate;
}

function setBookPages(bookInfo) {
    let pages_li = document.getElementById('pages');
    pages_li.innerText = bookInfo.pages;
}

function setBookPublishers(publishers) {
    let book_publisher_li = document.getElementById('book_publisher');
    if (publishers.length !== 0) {
        for (let i = 0; i < publishers.length; i++) {
            if (i !== publishers.length - 1) {
                book_publisher_li.innerText += publishers[i].name + ', '
            } else {
                book_publisher_li.innerText += publishers[i].name;
            }
        }
    } else {
        book_publisher_li.innerText = '-';
    }
}

function setBookGenres(genres) {
    let book_genres_li = document.getElementById('book_genres');
    if (genres.length !== 0) {
        for (let i = 0; i < genres.length; i++) {
            if (i !== genres.length - 1) {
                book_genres_li.innerHTML +=
                    '<a class="book-genre" ' +
                    'href="booksByGenre?genreTitle='+genres[i].title+'&page=1">' + genres[i].title +
                    ', ' + '</a>'
            } else {
                book_genres_li.innerHTML +=
                    '<a class="book-genre" ' +
                    'href="booksByGenre?genreTitle='+genres[i].title+'&page=1">' + genres[i].title +
                    '</a>'
            }
        }
    } else {
        book_genres_li.innerText = '-';
    }
}