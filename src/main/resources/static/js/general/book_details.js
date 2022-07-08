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
            getAuthorByBookId(bookInfo.bookId);
            getPublishersByBookId(bookInfo.bookId);
            setBookDescription(bookInfo);
            setProductDetails(bookInfo);
        },
        error : function (exception) {
            console.log(exception.responseText)
        }
    })
}

function getAuthorByBookId(id) {
    $.ajax({
        url : '/findAuthorByBookId',
        data : {bookId : id},
        success : function (author) {
            setAuthorFirstAndLastName(author);
            setAuthorBiography(author);
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

function setAuthorFirstAndLastName(author) {
    let authorId = author.authorId;
    let author_name_a = document.getElementById('author_name');
    let firstName = author.firstName;
    let lastName = author.lastName;
    author_name_a.innerText = firstName + " " + lastName;
    author_name_a.href = "author?authorId=" + authorId;
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
    cover_type_li.innerText = bookInfo.coverType;
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
                book_publisher_li.innerText += publishers[0].name + ', '
            } else {
                book_publisher_li.innerText += publishers[0].name;
            }
        }
    } else {
        book_publisher_li.innerText = '-';
    }
}

function setAuthorBiography(author) {
    let author_biography_p = document.getElementById('author_biography');
    author_biography_p.innerText = author.biography;
}