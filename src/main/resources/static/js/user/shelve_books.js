window.addEventListener('DOMContentLoaded', function () {
    let user_name = findUserName();
    profileUser = getUserProfileInfo(user_name);
    let book_status = new URLSearchParams(window.location.search).get('books');
    if (book_status !== null) {
        displayBooksByStatus(book_status);
    }
});

function getBooksByStatusAndShelveId(book_status, shelve_id) {
    let found_books_by_status_and_shelve_id;
    $.ajax({
        url: '/findBooksByShelveIdAndBookStatus',
        data: {
            shelveId: shelve_id,
            bookStatus: book_status,
            pageNumber: 1
        },
        async: false,
        success: function (booksByShelveIdAndStatus) {
            found_books_by_status_and_shelve_id = booksByShelveIdAndStatus;
            return found_books_by_status_and_shelve_id;
        }
    })
    return found_books_by_status_and_shelve_id;
}

function removeBookFromShelve(shelve_id, book_id) {
    let shelveBookDeleted = deleteShelveBookByShelveIdAndBookId(shelve_id, book_id);
    let book_status = new URLSearchParams(window.location.search).get('books');
    if (shelveBookDeleted) {
        displayBooksByStatus(book_status);
        changeNumberOfBookByStatusAndShelveId(book_status, shelve_id);
    }
}

function editBookStatusOnShelve() {
    let shelve_id = profileUser.bookShelve.bookShelveId;
    let book_status_from_url = new URLSearchParams(window.location.search).get('books');
    let new_book_status = document.querySelector('input[name="choice"]:checked').value;
    let book_id = document.getElementById('book_id').value;
    let book_status_on_shelve_changed = editBookStatusOnShelveByBookIdAndShelveId(new_book_status, book_id, shelve_id);
    if (book_status_on_shelve_changed) {
        displayBooksByStatus(book_status_from_url);
        changeNumberOfBookByStatusAndShelveId(book_status_from_url, shelve_id);
        changeNumberOfBookByStatusAndShelveId(new_book_status, shelve_id);
        hideEditBookStatusModal();
    }
}

function changeNumberOfBookByStatusAndShelveId(book_status, shelve_id) {
    if (book_status === 'READ') {
        displayNumberOfReadBooks(shelve_id, book_status);
    }
    if (book_status === 'CURRENTLY_READING') {
        displayNumberOfCurrentlyReadingBooks(shelve_id, book_status);
    }
    if (book_status === 'WANT_TO_READ') {
        displayNumberOfWantToReadBooks(shelve_id, book_status);
    }
}

function displayBooksByStatus(book_status) {
    let shelve_id = profileUser.bookShelve.bookShelveId;
    let user_id = profileUser.userId;
    let shelve_books = getBooksByStatusAndShelveId(book_status, shelve_id);
    let shelve_books_container_div = document.getElementById('shelve_books_container');
    let counter = 0;
    shelve_books_container_div.innerHTML = '';
    for (let shelveBook of shelve_books) {
        counter = counter + 1;
        let book_id = shelveBook.book.bookId;
        let title = shelveBook.book.title;
        let bookHref = 'http://localhost:8070/bookshop/book?title=' + title.replace(/ /g, "_");
        let authors = getAuthorsByBookId(book_id);
        shelve_books_container_div.innerHTML +=
            '<div class="shelve-book">' +
            '<div class="shelve-book-image-container">' +
            '<a href="' + bookHref + '">' +
            '<img class="shelve-book-image" src="' + shelveBook.book.imagePath + '">' +
            '</a>' +
            '</div>' +
            '<div class="shelve-book-info">' +
            '<div class="shelve-book-info-head">' +
            '<div class="shelve-book-title">' +
            '<a href="' + bookHref + '">' + title + '</a>' +
            '</div>' +
            '<div class="shelve-book-actions" id="actions' + counter + '">' +
            '</div>' +
            '</div>' +
            '<div class="shelve-book-author" id="authors_for_shelve_book' + counter + '">' +
            '</div>' +
            '<div class="shelve-book-genres">' +
            '</div>' +
            '<div class="shelve-book-my-score">' +
            '<button class="btn shadow-none my-score-btn" id="user_score_for_book' + counter + '">' + '</button>' +
            '<button class="btn shadow-none my-score-btn" id="average_book_score' + counter + '" ">' + '</button>' +
            '</div>' +
            '<div class="shelve-book-description" id="shelve_book_description' + counter + '">' + shelveBook.book.description + '</div>' +
            '<div class="arrow-container" id="arrow_container' + counter + '">' + '</div>' +
            '</div>' +
            '</div>';
        displayAuthorsToShelveBook(authors, counter);
        displayUserScoreForBook(user_id, book_id, counter);
        displayAverageBookScoreByBookId(book_id, counter);
        displayEditAndDeleteShelveBookButtons(counter, shelve_id, book_id, title);
        displayArrowDownAfterDescription(counter);
    }
    changeUrl(book_status)
}

function displayAuthorsToShelveBook(authors, counter) {
    let shelve_book_authors_div = document.getElementById('authors_for_shelve_book' + counter)
    if (authors !== null) {
        for (let author of authors) {
            shelve_book_authors_div.innerHTML +=
                '<a href="http://localhost:8070/bookshop/author?authorId=' + author.authorId + '">'
                + author.firstName + ' ' + author.lastName +
                '</a>';
        }
    }
}

function displayUserScoreForBook(user_id, book_id, counter) {
    let user_name_from_profile = profileUser.userName;
    let score_text = document.getElementById('score').value;
    let my_score_text = document.getElementById('my_score').value;
    let userScoreForBook = getBookScoreByUserIdAndBookId(user_id, book_id);
    let score_div = document.getElementById('user_score_for_book' + counter);

    if (userScoreForBook !== 0) {
        if (document.getElementById('user-name-h1') !== null
            && document.getElementById('user-name-h1').innerText === user_name_from_profile) {
            score_div.innerText = my_score_text + ': ' + userScoreForBook;
        } else {
            score_div.innerText = score_text + ' ' + user_name_from_profile + ': ' + userScoreForBook;
        }
    }
}

function displayAverageBookScoreByBookId(book_id, counter) {
    let users_score_text = document.getElementById('users_score_text').value;
    let average_book_score_button = document.getElementById('average_book_score' + counter);
    let average_book_score = getAverageBookScoreByBookId(book_id);
    average_book_score_button.innerText = users_score_text + ': ' + average_book_score;
}

function displayEditAndDeleteShelveBookButtons(counter, shelve_id, book_id, book_title) {
    let user_name_from_profile = profileUser.userName;
    let shelve_book_actions_div = document.getElementById('actions' + counter);
    if (document.getElementById('user-name-h1') !== null
        && document.getElementById('user-name-h1').innerText === user_name_from_profile) {
        shelve_book_actions_div.innerHTML +=
            '<button class="btn edit-book-on-shelve-btn shadow-none" ' +
            'onclick="setEditShelveBookModal(\'' + shelve_id + '\',\'' + book_id + '\',\'' + book_title + '\')" ' +
            'data-bs-toggle="modal" ' +
            'data-bs-target="#editBookStatusModal">' +
            '<i class="fa fa-edit">' + '</i>' +
            '</button>' +

            '<button class="btn delete-from-shelve-btn shadow-none" ' +
            'onclick="removeBookFromShelve(\'' + shelve_id + '\',\'' + book_id + '\')">' + '<i class="fa fa-trash">' + '</i>' + '</button>';
    }
}

function displayArrowDownAfterDescription(counter) {
    let arrow_div = document.getElementById('arrow_container' + counter);
    arrow_div.innerHTML +=
        '<i class="fa fa-angle-down" ' +
        'onclick="changeLineClampOfDescriptionToInheritAndMakeArrowUp(\'' + counter + '\',\'' + arrow_div + '\')">' +
        '</i>';
}

function changeLineClampOfDescriptionToInheritAndMakeArrowUp(counter) {
    let description_div = document.getElementById('shelve_book_description' + counter);
    let arrow_div = document.getElementById('arrow_container' + counter);
    description_div.style.webkitLineClamp = 'inherit';
    arrow_div.innerHTML = '';
    arrow_div.innerHTML +=
        '<i class="fa fa-angle-up" ' +
        'onclick="changeLineClampOfDescriptionTo3AndMakeArrowDown(\'' + counter + '\',\'' + arrow_div + '\')">' +
        '</i>';
}

function changeLineClampOfDescriptionTo3AndMakeArrowDown(counter) {
    let description_div = document.getElementById('shelve_book_description' + counter);
    let arrow_div = document.getElementById('arrow_container' + counter);
    description_div.style.webkitLineClamp = '3';
    arrow_div.innerHTML = '';
    arrow_div.innerHTML +=
        '<i class="fa fa-angle-down" ' +
        'onclick="changeLineClampOfDescriptionToInheritAndMakeArrowUp(\'' + counter + '\',\'' + arrow_div + '\')">' +
        '</i>';
}

function changeUrl(book_status) {
    let url = window.location.href.split('&')[0] + '&books=' + book_status;
    history.pushState(undefined, '', url);
}

function setEditShelveBookModal(shelve_id, book_id, book_title) {
    let book_title_h5 = document.getElementById('book_title_in_modal_header');
    book_title_h5.innerText = book_title;
    let shelve_id_input = document.getElementById('shelve_id');
    shelve_id_input.value = shelve_id;
    let book_id_input = document.getElementById('book_id');
    book_id_input.value = book_id;
    let book_status = getBookStatusOnShelve(book_id, shelve_id);
    $("input[name=choice][value=" + book_status + "]").prop('checked', true);
}