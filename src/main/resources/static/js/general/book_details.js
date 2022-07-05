window.addEventListener('DOMContentLoaded', function () {
    let bookTitleFromParameter = new URLSearchParams(window.location.search).get('title');
    let title = bookTitleFromParameter.replace(/_/g, ' ');
    getBookDetailsByTitle(decodeURI(title));
});

function getBookDetailsByTitle(bookTitle) {
    console.log(bookTitle)
    $.ajax({
        url : '/findBookDetails',
        data : {title : bookTitle},
        success : function (bookInfo) {
            setBookTitle(bookInfo);
            setBookImage(bookInfo);
            setBookTitle(bookInfo);
            setH1Title(bookInfo);
            setAuthorFirstAndLastName(bookInfo);
            setBookDescription(bookInfo);
            setProductDetails(bookInfo);
            setAuthorBiography(bookInfo);
        },
        error : function (exception) {
            console.log(exception.responseText)
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

function setAuthorFirstAndLastName(bookInfo) {
    let authorId = bookInfo.authors[0].authorId;
    let author_name_a = document.getElementById('author_name');
    let firstName = bookInfo.authors[0].firstName;
    let lastName = bookInfo.authors[0].lastName;
    author_name_a.innerText = firstName + " " + lastName;
    author_name_a.href = "author?authorId=" + authorId;
}

function setBookDescription(bookInfo) {
    let description_p = document.getElementById('description');
    description_p.innerText = bookInfo.description;
}

function setProductDetails(bookInfo) {
    let book_price_li = document.getElementById('book_price');
    book_price_li.innerText = '$' + bookInfo.price;
    // let book_publisher_li = document.getElementById('book_publisher');
    // book_publisher_li.innerText = bookInfo.publisher.name;
    let book_year_li = document.getElementById('publish_year');
    book_year_li.innerText = bookInfo.publishDate;
    let pages_li = document.getElementById('pages');
    pages_li.innerText = bookInfo.pages;
    let cover_type_li = document.getElementById('book_cover_type');
    cover_type_li.innerText = bookInfo.coverType;
    let isbn_li = document.getElementById('book_isbn');
    isbn_li.innerText = bookInfo.isbn;
}

function setAuthorBiography(bookInfo) {
    let author_biography_p = document.getElementById('author_biography');
    author_biography_p.innerText = bookInfo.authors[0].biography;
}