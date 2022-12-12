window.addEventListener('DOMContentLoaded', function () {
    let page_number = new URLSearchParams(window.location.search).get('page');
    let language_name = new URLSearchParams(window.location.search).get('lang');
    displayLocalizedBooks(language_name, page_number);
    displayLanguagesInSelect();
    addDaysToOption();
    addMonthToOption();
    addYearsToOption();
});


function displayLocalizedBooks(language_name, page_number) {
    let localized_books = '';
    let url = '';
    if (language_name === null || language_name === '' || language_name === 'null') {
        localized_books = getLocalizedBooksByLanguageAndPage(language_name, page_number);
        let locale = getCurrentLocale();
        url = window.location.href.split('?')[0] + "?page=" + page_number + '&lang=' + locale;
        history.pushState(undefined, '', url);
        buildTableBodyForBooks(localized_books.content);
    } else {
        url = window.location.href.split('?')[0] + "?page=" + page_number + '&lang=' + language_name;
        history.pushState(undefined, '', url);
        localized_books = getLocalizedBooksByLanguageAndPage(language_name, page_number);
        buildTableBodyForBooks(localized_books.content);
    }
    buildPaginationForBooksTable(localized_books.totalPages);
}

function createLocalizedBook() {
    let localized_title = document.getElementById('book_title').value;
    let localized_description = document.getElementById('book_description').value;

    let image_path = "http://localhost:8090/image/book/default.png";
    let file_input = document.getElementById('file-input');
    if (file_input.files.length !== 0) {
        image_path = file_input.files[0].name;
    }
    return {
        "title": localized_title,
        "description": localized_description,
        "imagePath": image_path
    };
}

function createBook() {
    let localized_book = createLocalizedBook();
    let day = document.getElementById('day');
    let month = document.getElementById('month');
    let year = document.getElementById('year');
    let date = new Date(Date.UTC(year.value, month.value, day.value))
        .toISOString().slice(0, 10);
    let book_price = document.getElementById('book_price').value;
    let book_pages = document.getElementById('book_pages').value;
    let book_isbn = document.getElementById('book_isbn').value;
    let book_cover_type = document.getElementById('book_cover_type').value;

    return {
        "localizedBook": localized_book,
        "publishDate": date,
        "price": book_price,
        "pages": book_pages,
        "isbn": book_isbn,
        "coverType": book_cover_type,
    }
}

function addBook() {
    let created_book = createBook();
    let language_name = document.getElementById('language').value;
    let formData = new FormData();
    formData.append("bookImage", $('#file-input')[0].files[0]);
    formData.append('book', new Blob([JSON.stringify(created_book)], {
        type: "application/json"
    }));
    formData.append('languageName', language_name);
    if (isBookDataValid(created_book.localizedBook.title, created_book.localizedBook.description, created_book.isbn,
            created_book.price, created_book.coverType, created_book.pages)
        && isDateValid(created_book.publishDate)
        && isImageValid(created_book.localizedBook.imagePath)) {
        $.ajax({
            method: 'POST',
            url: '/addBook',
            contentType: false,
            processData: false,
            data: formData,
            success: function () {
                hideAddBookModal();
                displaySuccessModal(created_book.localizedBook.title);
            },
            error: function (exception) {
                displayErrorMessage(exception.responseText);
            }
        })
    }
}

function getBooksByPage(pageNumber) {
    let url = window.location.href.split('?')[0] + "?page=" + pageNumber;
    history.pushState(undefined, '', url);
    $.ajax({
        url: '/findBooksByPage',
        data: {page: pageNumber},
        success: function (booksByPage) {
            buildTableBodyForBooks(booksByPage.content);
            buildPaginationForBooksTable(booksByPage.totalPages);
        },
        error: function (exception) {
            displayErrorMessageForSearchInput(exception.responseText);
        }
    })
}

function getBooksByKeyWord(key_word) {
    if (key_word !== '') {
        $.ajax({
            url: '/displayLocalizedBooksByKeyword',
            data: {keyword: key_word},
            success: function (booksByKeyWord) {
                hideErrorMessageForSearchInput();
                buildTableBodyForBooks(booksByKeyWord);
            },
            error: function (exception) {
                displayErrorMessageForSearchInput(exception.responseText);
            }
        });
    } else {
        let page_number = new URLSearchParams(window.location.search).get('page');
        let language_name = new URLSearchParams(window.location.search).get('lang');
        displayLocalizedBooks(language_name, page_number);
    }
}

function displayErrorMessageForSearchInput(exception) {
    const tableBody = document.getElementById('tableData');
    tableBody.innerHTML = '';
    let errorMessageDiv = document.getElementById('error-message-for-search');
    let jsonResponse = JSON.parse(exception);
    errorMessageDiv.innerText = jsonResponse['message'];
}

function hideErrorMessageForSearchInput() {
    let errorMessageDiv = document.getElementById('error-message-for-search');
    errorMessageDiv.innerText = '';
}

function buildTableBodyForBooks(localizedBooks) {
    const tableBody = document.getElementById('tableData');
    const editBtn = document.getElementById('edit-btn-name').value;
    const deleteBtn = document.getElementById('delete-btn-name').value;
    const add_localization_btn = document.getElementById('add_localization_properties').value;
    let booksHtml = '';
    let language_name_from_url = new URLSearchParams(window.location.search).get('lang');
    let counter = 0;
    for (let localized_book of localizedBooks) {
        counter = counter + 1;
        let book_language = getLanguageByLocalizedBookTitle(localized_book.title);
        booksHtml += '<tr>' +
            '<td>' + counter + '</td>' +
            '<td>' +
            '<div class="d-flex align-items-center">' +
            '<img class="img-thumbnail" style="width: 50px" src=' + localized_book.imagePath + '>' +
            '</div>' + '</td>' +
            '<td style="width: 680px">' + localized_book.title + '</td>' +
            '<td>';

        if (language_name_from_url !== book_language.name) {
            let translate_book_url = '/admin/cabinet/translate_book?id=' + localized_book.book.bookId;
            booksHtml += '<a type="button" ' +
                'class="btn btn-secondary role-btn" ' +
                'href="' + translate_book_url + '">' + add_localization_btn + '</a>'
        } else {
            let edit_book_url = '/admin/cabinet/edit_book?id=' + localized_book.book.bookId;
            booksHtml += '<a type="button" ' +
                'class="btn btn-secondary role-btn" ' +
                'href="' + edit_book_url + '">' + editBtn + '</a>'
        }

        booksHtml +=
            '<a type="button" ' +
            'class="btn btn-danger role-btn" ' +
            'data-bs-toggle="modal" ' +
            'data-bs-target="#deletePublisherModal" ' +
            'onclick="">' + deleteBtn + '</a>' +
            '</td>' + '</tr>';
    }
    tableBody.innerHTML = booksHtml;
}

function addYearsToOption() {
    let start = 1900;
    let finish = new Date().getFullYear();
    let year_select = document.getElementById('year');
    for (let i = start; i <= finish; i++) {
        year_select.innerHTML += '<option value="' + i + '">' + i + '</option>';
    }
}

function addMonthToOption() {
    let start = 1;
    let finish = 12;
    let month_select = document.getElementById('month');
    for (let i = start; i <= finish; i++) {
        let value = i - 1;
        month_select.innerHTML += '<option value="' + value + '">' + i + '</option>';
    }
}

function addDaysToOption() {
    let start = 1;
    let finish = 31;
    let day_select = document.getElementById('day');
    for (let i = start; i <= finish; i++) {
        let value = i;
        day_select.innerHTML += '<option value="' + value + '">' + value + '</option>';
    }
}

function buildPaginationForBooksTable(pages) {
    let pagination_ul = document.getElementById('books-pagination');
    pagination_ul.innerHTML = '';
    let language_name = new URLSearchParams(window.location.search).get('lang');
    if (pages > 1) {
        for (let i = 0; i < pages; i++) {
            let page = i + 1;
            pagination_ul.innerHTML +=
                '<li class="page-item">' +
                '<button class="page-link shadow-none" ' +
                'onclick="displayLocalizedBooks(\'' + language_name + '\',\'' + page + '\')">' + page + '</button>' +
                '</li>';
        }
    }
}

function displayLanguagesInSelect() {
    let languages = findAllLanguages();
    let languages_select = document.getElementById('language');
    languages_select.innerHTML = '';
    for (let language of languages) {
        let language_name = language.name;
        languages_select.innerHTML +=
            '<option value="' + language_name + '">' + language_name + '</option>';
    }
}

function displaySuccessModal(book_title) {
    let success_message_h5 = document.getElementById('success_message');
    let success_message_input_value = document.getElementById('add_book_success_message').value;
    success_message_h5.innerText = success_message_input_value.replace('%s', book_title);
    $('#successModal').modal('show');
}

function hideAddBookModal() {
    $('#addBookModal').modal('hide');
}

function showErrorModal() {
    $('#errorModal').modal('show');
}

function displayErrorMessage(exception) {
    let errorMessageDiv = document.getElementById('error_message');
    let jsonResponse = JSON.parse(exception);
    errorMessageDiv.innerText = jsonResponse['message'];
    showErrorModal();
}