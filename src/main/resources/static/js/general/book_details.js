let book;
let userInfo;

window.addEventListener('DOMContentLoaded', function () {
    let bookTitleFromParameter = new URLSearchParams(window.location.search).get('title');
    let title = bookTitleFromParameter.replace(/_/g, ' ');
    book = getBookDetailsByTitle(decodeURI(title));
    getBookReviews(book.bookId, 1);
    getAverageBookReviewScoreByBookId(book.bookId);
    userInfo = getUserByUserName();
});

function getBookDetailsByTitle(bookTitle) {
    let bookDetails = null;
    $.ajax({
        url: '/findBookDetails',
        async: false,
        data: {title: bookTitle},
        success: function (bookInfo) {
            bookDetails = bookInfo;
            setBookTitle(bookInfo);
            setBookImage(bookInfo);
            setBookTitle(bookInfo);
            setH1Title(bookInfo);
            getAuthorsByBookId(bookInfo.bookId);
            getPublishersByBookId(bookInfo.bookId);
            getGenresByBookId(bookInfo.bookId);
            setBookDescription(bookInfo);
            setProductDetails(bookInfo);
            return bookDetails;
        },
        error: function (exception) {
            console.log(exception.responseText)
        }
    })
    return bookDetails;
}

function getAuthorsByBookId(id) {
    $.ajax({
        url: '/findAuthorsByBookId',
        data: {bookId: id},
        success: function (authors) {
            setAuthors(authors);
        },
        error: function (exception) {
            console.log(exception.responseText);
        }
    })
}

function getPublishersByBookId(id) {
    $.ajax({
        url: '/findPublishersByBookId',
        data: {bookId: id},
        success: function (publishers) {
            setBookPublishers(publishers);
        },
        error: function (exception) {
            console.log(exception.responseText);
        }
    })
}

function getGenresByBookId(id) {
    $.ajax({
        url: '/findGenresByBookId',
        data: {bookId: id},
        success: function (genres) {
            setBookGenres(genres);
        },
        error: function (exception) {
            let book_genres_li = document.getElementById('book_genres');
            book_genres_li.innerText = '-';
        }
    })
}

function getUserByUserName() {
    if (document.getElementById('user-name-h1') !== null) {
        let user_name_h2 = document.getElementById('user-name-h1');
        let userToReturn = null;
        if (user_name_h2.innerText !== '') {
            $.ajax({
                url: '/findUserByUsername',
                async: false,
                data: {username: user_name_h2.innerText},
                success: function (user) {
                    userToReturn = user;
                    checkIfUserReviewedBook(user.userId, book.bookId);
                    setUserImageInReview(user);
                    return userToReturn;
                }
            })
        }
        return userToReturn;
    }
}

function getBookReviews(book_id, page_number) {
    $.ajax({
        url: '/findBookReviewsByBookIdAndPage',
        data: {
            bookId: book_id,
            pageNumber: page_number
        },
        success: function (bookReviews) {
            displayBookReviews(bookReviews.content);
            buildPaginationForBookReviews(bookReviews.totalPages);
        }
    })
}

function getUserByReviewId(review_id) {
    let userByReviewId = null;
    $.ajax({
        url: '/findUserByReviewId',
        data: {reviewId: review_id},
        async: false,
        success: function (foundUser) {
            userByReviewId = foundUser;
            return userByReviewId;
        }
    })
    return userByReviewId;
}

function getAverageBookReviewScoreByBookId(book_id) {
    let score_div = document.getElementById('average_score');
    $.ajax({
        url: '/findAverageBookReviewScore',
        data: {bookId: book_id},
        success: function (averageScore) {
            setBookReviewScore(averageScore, score_div);
        },
        error: function () {
            setBookReviewScore(0, score_div);
        }
    })
}

function checkIfUserReviewedBook(user_id, book_id) {
    $.ajax({
        url: '/isUserReviewedGivenBook',
        data: {
            bookId: book_id,
            userId: user_id
        },
        success: function (userReviewedBook) {
            if (userReviewedBook) {
                hideAddReviewContainer();
            }
        }
    })
}

function addReview() {
    let review_text = document.getElementById('review_text').value;
    let review_score = document.querySelector('input[name="star"]:checked').value;
    let review_form = new FormData();
    review_form.append('reviewText', review_text);
    review_form.append('score', review_score);
    review_form.append('userId', userInfo.userId);
    review_form.append('bookId', book.bookId);

    $.ajax({
        method: 'POST',
        url: '/addReviewToBook',
        cache: false,
        processData: false,
        contentType: false,
        data: review_form,
        success: function () {
            resetRadioInput();
            resetTextArea();
            getBookReviews(book.bookId, 1);
            getAverageBookReviewScoreByBookId(book.bookId);
            checkIfUserReviewedBook(userInfo.userId, book.bookId);
        },
        error: function (exception) {
        }
    })
}

function editReview() {
    let review_text_to_edit = document.getElementById('text_to_edit').value;
    let edited_review_score = document.querySelector('input[name="edit_star"]:checked').value;
    let review_id = document.getElementById('review_id').value;
    let review_form = new FormData();
    review_form.append('newText', review_text_to_edit);
    review_form.append('newScore', edited_review_score);
    review_form.append('userId', userInfo.userId);
    review_form.append('reviewId', review_id);

    $.ajax({
        method: 'POST',
        url: '/editReview',
        cache: false,
        processData: false,
        contentType: false,
        data: review_form,
        success: function () {
            hideEditReviewModal();
            getBookReviews(book.bookId, 1);
            getAverageBookReviewScoreByBookId(book.bookId);
        },
        error: function (exception) {
            console.log('false');
        }
    })
}

function setBookReviewScore(score, score_div) {
    if (score > 0 && score < 3) {
        score_div.style.backgroundColor = 'red';
        score_div.innerText = score;
    } else if (score >= 3 && score < 4) {
        score_div.style.backgroundColor = '#fc3';
        score_div.innerText = score;
    } else if (score >= 4) {
        score_div.style.backgroundColor = '#6c3';
        score_div.innerText = score;
    } else {
        score_div.style.backgroundColor = 'silver';
        score_div.innerText = 'tbh';
    }
}

function setUserImageInReview(user) {
    let user_image_path = user.avatarName;
    let image = document.getElementById('user_image_in_review');
    image.src = user_image_path;
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
                '<a class="author-name" href="author?authorId=' + authors[i].authorId + '">' +
                firstName + ' ' + lastName + ', ' +
                '</a>';
        } else {
            author_div.innerHTML +=
                '<a class="author-name" href="author?authorId=' + authors[i].authorId + '">' +
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
    let date = bookInfo.publishDate;
    let dateArray = date.split('-');
    let year = dateArray[0];
    let book_year_li = document.getElementById('publish_year');
    book_year_li.innerHTML +=
        '<a class="book-year" ' +
        'href="booksByYear?year=' + year + '&page=1">' + year + '</a>'
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
                    'href="booksByGenre?genreTitle=' + genres[i].title + '&page=1">' + genres[i].title +
                    ', ' + '</a>'
            } else {
                book_genres_li.innerHTML +=
                    '<a class="book-genre" ' +
                    'href="booksByGenre?genreTitle=' + genres[i].title + '&page=1">' + genres[i].title +
                    '</a>'
            }
        }
    } else {
        book_genres_li.innerText = '-';
    }
}

function displayBookReviews(bookReviews) {
    let edit = document.getElementById('edit').value;
    let book_reviews_container = document.getElementById('book_reviews_container');
    book_reviews_container.innerHTML = '';
    let userNameFromSecurity = '';
    if (document.getElementById('user-name-h1') !== null) {
        userNameFromSecurity = document.getElementById('user-name-h1').innerText;
    }
    let counter = 0;
    for (let review of bookReviews) {
        let user = getUserByReviewId(review.bookReviewId);
        let publishDate = review.publishDate;
        let reversedDate = publishDate.split('-').reverse().join('-');
        counter = counter + 1;
        book_reviews_container.innerHTML +=
            '<div class="review-container bg-light">' +
            '<div class="review-head">' +
            '<div class="review-head-image-container">' +
            '<img class="review-head-user-image" src="' + user.avatarName + '">' + '</div>' +
            '<div class="review-head-user-info">' +
            '<p class="review-head-user-name">' + user.userName + '</p>' +
            '<p class="review-head-post-date">' + reversedDate + '</p>' + '</div>' +
            '<div class="review-head-score-container">' +
            '<div class="review-score" id="review_score' + counter + '">' + '' + '</div>' + '</div>' + '</div>' +
            '<div class="review-middle">' + '<div class="review-text">' + review.reviewText + '</div>' + '</div>' + '</div>';
        let userRelatesToReview =
            checkIfUserRelatesToReviewByUserName(userNameFromSecurity, user.userName);
        if (userRelatesToReview) {
            book_reviews_container.innerHTML +=
                '<i class="fa fa-edit edit-icon" ' +
                'data-bs-toggle="modal" ' +
                'onclick="setUserReviewForEdit(\'' + review.score + '\',\'' + review.reviewText + '\',\'' + review.bookReviewId + '\')" ' +
                'data-bs-target="#editReviewModal">';
        }
        let score_div = document.getElementById('review_score' + counter + '');
        setBookReviewScore(review.score, score_div);
    }
}

function checkIfUserRelatesToReviewByUserName(userNameFromSecurity, userNameFromReview) {
    return userNameFromReview === userNameFromSecurity;
}

function setUserReviewForEdit(score, text, review_id) {
    if (score === '1') {
        $("#1").prop("checked", true);
    } else if (score === '2') {
        $("#2").prop("checked", true);
    } else if (score === '3') {
        $("#3").prop("checked", true);
    } else if (score === '4') {
        $("#4").prop("checked", true);
    } else {
        $("#5").prop("checked", true);
    }
    $('#text_to_edit').val(text);
    $('#review_id').val(review_id);
}

function resetRadioInput() {
    let radio_input = document.querySelector('input[name="star"]:checked');
    radio_input.checked = false;
}

function resetTextArea() {
    let text_area_input = document.getElementById('review_text');
    text_area_input.value = '';
}

function buildPaginationForBookReviews(pages) {
    let pagination_ul = document.getElementById('book-reviews-pagination');
    pagination_ul.innerHTML = '';
    if (pages > 1) {
        for (let i = 0; i < pages; i++) {
            let page = i + 1;
            pagination_ul.innerHTML +=
                '<li class="page-item">' +
                '<button class="page-link shadow-none" ' +
                'onclick="getBookReviews(book.bookId, ' + page + ')">' + page + '</button>' +
                '</li>';
        }
    }
}

function hideAddReviewContainer() {
    $('#h1-for-add-review').hide();
    $('#add_review_container').hide();
}

function hideEditReviewModal() {
    $('#editReviewModal').modal('hide');
}