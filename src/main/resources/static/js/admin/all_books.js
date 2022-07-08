window.addEventListener('DOMContentLoaded', function () {
    let page = new URLSearchParams(window.location.search).get('page');
    getBooksByPage(page);
    getAllPublishersForSelect();
    getAllGenresForSelect();
    getAllAuthorsForSelect();
});

function getBooksByPage(pageNumber) {
    $.ajax({
        url : '/findBooksByPage',
        data : {page : pageNumber},
        success : function (booksByPage) {
            buildTableBodyForBooks(booksByPage.content);
        },
        error : function (exception) {
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

function getAllPublishersForSelect() {
    $.ajax({
        url : '/findAllPublishers',
        success : function (publishers) {
            displayPublishersInOption(publishers);
        },
        error : function (exception) {
            console.log(exception);
        }
    })
}

function getAllGenresForSelect() {
    $.ajax({
        url : '/findAllGenres',
        success : function (genres) {
            displayGenresForSelect(genres);
        },
        error : function (exception) {
            console.log(exception);
        }
    })
}

function getAllAuthorsForSelect() {
    $.ajax({
        url : '/findAllAuthors',
        success : function (authors) {
            displayAuthorsForSelect(authors);
        },
        error : function (exception) {
            console.log(exception);
        }
    })
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

function displayPublishersInOption(publishers) {
    let publisher_select = document.getElementById('publisher');
    publisher_select.innerHTML = '';
    for (let publisher of publishers) {
        publisher_select.innerHTML +=
            '<option value="'+ publisher +'">' + publisher.name + '</option>';
    }
}

function displayGenresForSelect(genres) {
    let genres_select = document.getElementById('genre');
    genres_select.innerHTML = '';
    for (let genre of genres) {
        genres_select.innerHTML +=
            '<option value="'+ genre +'">' + genre.title + '</option>';
    }
}

function displayAuthorsForSelect(authors) {
    let authors_select = document.getElementById('author');
    authors_select.innerHTML = '';
    for (let author of authors) {
        authors_select.innerHTML +=
            '<option value="'+ author +'">' +
            author.firstName + " " + author.lastName +
            '</option>';
    }
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
            '<td style="width: 750px">' + book.title + '</td>' +
            '<td>' +
            '<button type="button" ' +
            'class="btn btn-secondary role-btn" ' +
            'data-bs-toggle="modal" ' +
            'data-bs-target="#editPublisherModal" ' +
            'value="' + book.name + '" ' +
            'onclick="">' + editBtn + '</button>' +
            '<a type="button" ' +
            'class="btn btn-danger role-btn" ' +
            'data-bs-toggle="modal" ' +
            'data-bs-target="#deletePublisherModal" ' +
            'onclick="">' + deleteBtn + '</a>' +
            '</td>' + '</tr>';
    }
    tableBody.innerHTML = authorsHtml;
}