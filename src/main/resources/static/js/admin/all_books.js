window.addEventListener('DOMContentLoaded', function () {
    let page = new URLSearchParams(window.location.search).get('page');
    getBooksByPage(page);
    addDaysToOption();
    addMonthToOption();
    addYearsToOption();
});

function addBook() {
    let day = document.getElementById('day');
    let month = document.getElementById('month');
    let year = document.getElementById('year');
    let date = new Date(Date.UTC(year.value, month.value, day.value))
        .toISOString().slice(0, 10);
    let bookForm = new FormData();
    bookForm.append('title', $('#book_title').val());
    bookForm.append('publishDate', date);
    bookForm.append('description', $('#book_description').val());
    bookForm.append('price', $('#book_price').val());
    bookForm.append('pages', $('#book_pages').val());
    bookForm.append('isbn', $('#book_isbn').val());
    bookForm.append('coverType', $('#book_cover_type').val());
    bookForm.append('bookImage', $('#file-input')[0].files[0]);
    $.ajax({
        method: 'POST',
        url: '/addBook',
        cache: false,
        processData: false,
        contentType: false,
        data: bookForm,
        success: function () {
            let page = new URLSearchParams(window.location.search).get('page');
            getBooksByPage(page);
        },
        error: function (errorMessage) {
            console.log(errorMessage.responseText);
        }
    })
}

function getBooksByPage(pageNumber) {
    $.ajax({
        url: '/findBooksByPage',
        data: {page: pageNumber},
        success: function (booksByPage) {
            buildTableBodyForBooks(booksByPage.content);
        },
        error: function (exception) {
            displayErrorMessageForSearchInput(exception.responseText);
        }
    })
}

function getBooksByKeyWord() {
    let keywordInput = document.getElementById('keyword');
    let page = new URLSearchParams(window.location.search).get('page');
    if (keywordInput.value !== '') {
        $.ajax({
            url: '/findBooksByKeyword',
            data: {keyWord: keywordInput.value},
            success: function (booksByKeyWord) {
                hideErrorMessageForSearchInput();
                buildTableBodyForBooks(booksByKeyWord);
            },
            error: function (exception) {
                displayErrorMessageForSearchInput(exception.responseText);
            }
        });
    } else {
        getBooksByPage(page);
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

function buildTableBodyForBooks(books) {
    const tableBody = document.getElementById('tableData');
    const editBtn = document.getElementById('edit-btn-name').value;
    const deleteBtn = document.getElementById('delete-btn-name').value;
    let authorsHtml = '';
    let counter = 0;
    for (let book of books) {
        counter = counter + 1;
        authorsHtml += '<tr>' +
            '<td>' + counter + '</td>' +
            '<td>' +
            '<div class="d-flex align-items-center">' +
            '<img class="img-thumbnail" style="width: 50px" src=' + book.imagePath + '>' +
            '</div>' + '</td>' +
            '<td style="width: 700px">' + book.title + '</td>' +
            '<td>' +
            '<a type="button" ' +
            'class="btn btn-secondary role-btn" ' +
            'href="/admin/cabinet/edit_book?title='+book.title +'">' + editBtn + '</a>' +
            '<a type="button" ' +
            'class="btn btn-danger role-btn" ' +
            'data-bs-toggle="modal" ' +
            'data-bs-target="#deletePublisherModal" ' +
            'onclick="">' + deleteBtn + '</a>' +
            '</td>' + '</tr>';
    }
    tableBody.innerHTML = authorsHtml;
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