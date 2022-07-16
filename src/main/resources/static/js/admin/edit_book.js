window.addEventListener('DOMContentLoaded', function () {
    let bookTitleFromParameter = new URLSearchParams(window.location.search).get('title');
    getBookByTitle(bookTitleFromParameter);
    getAllAuthors();
    getAllPublishers();
    getAllGenres();
});


function editBook() {
    let day = document.getElementById('day');
    let month = document.getElementById('month');
    let year = document.getElementById('year');
    let date = new Date(Date.UTC(year.value, month.value, day.value))
        .toISOString().slice(0, 10);
    let bookToUpdate = new FormData();
    bookToUpdate.append('bookId', $('#book_id').val());
    bookToUpdate.append('title', $('#book_title').val());
    bookToUpdate.append('publishDate', date);
    bookToUpdate.append('description', $('#book_description').val());
    bookToUpdate.append('price', $('#book_price').val());
    bookToUpdate.append('pages', $('#book_pages').val());
    bookToUpdate.append('isbn', $('#book_isbn').val());
    bookToUpdate.append('coverType', $('#book_cover_type').val());
    bookToUpdate.append('newBookImage', $('#file-input')[0].files[0]);
    bookToUpdate.append('imagePath', $('#old_image_path').val());
    $.ajax({
        method: 'POST',
        url: '/updateBookInfo',
        cache: false,
        processData: false,
        contentType: false,
        data: bookToUpdate,
        success: function (isBookUpdated) {
            let book_title_input = document.getElementById('book_title').value;
            let queryParams = new URLSearchParams(window.location.search);
            queryParams.set('title', book_title_input);
            history.replaceState(null, null, "?" + queryParams.toString());
            let bookTitleFromParameter = new URLSearchParams(window.location.search).get('title');
            getBookByTitle(bookTitleFromParameter);
            displaySuccessMessage();
        },
        error: function (errorMessage) {
            console.log(errorMessage.responseText);
        }
    })
}

function addAuthorToBook() {
    let author_id_from_select = document.getElementById('book_authors_to_select').value;
    let book_id_input = document.getElementById('book_id').value;
    $.ajax({
        method: 'POST',
        url: '/addAuthorToBook',
        data: {
            authorId: author_id_from_select,
            bookId: book_id_input
        },
        success: function () {
            hideAddAuthorToBookModal();
            getAuthorsForBookByBookId(book_id_input);
        },
        error: function (exception) {
            hideAddAuthorToBookModal();
            displayErrorMessageInModal(exception.responseText);
        }
    })
}

function addGenreToBook() {
    let genre_id_from_select = document.getElementById('book_genres_to_select').value;
    let book_id_input = document.getElementById('book_id').value;
    $.ajax({
        method: 'POST',
        url: '/addGenreToBook',
        data: {
            bookId: book_id_input,
            genreId: genre_id_from_select
        },
        success: function () {
            hideAddGenreModal()
            getGenresForBook(book_id_input);
        },
        error: function (exception) {
            hideAddGenreModal();
            displayErrorMessageInModal(exception.responseText);
        }
    })
}

function addPublisherToBook() {
    let publisher_id_from_select = document.getElementById('book_publishers_to_select').value;
    let book_id_input = document.getElementById('book_id').value;
    $.ajax({
        method: 'POST',
        url: '/addPublisherToBook',
        data: {
            bookId: book_id_input,
            publisherId: publisher_id_from_select
        },
        success: function () {
            hideAddPublisherModal();
            getPublishersForBook(book_id_input);
        },
        error: function (exception) {
            hideAddPublisherModal();
            displayErrorMessageInModal(exception.responseText);
        }
    })
}

function removeAuthorFromBook() {
    let book_id_input = document.getElementById('book_id');
    let author_id_input = document.getElementById('author_id_input');
    $.ajax({
        method: 'POST',
        url: '/removeAuthorFromBook',
        data: {
            bookId: book_id_input.value,
            authorId: author_id_input.value
        },
        success: function () {
            hideRemoveAuthorFormBookModal();
            getAuthorsForBookByBookId(book_id_input.value);
        },
        error: function (exception) {
            displayErrorMessageInModal(exception.responseText);
            hideRemoveAuthorFormBookModal();
        }
    })
}

function removePublisherFromBook() {
    let book_id_input = document.getElementById('book_id');
    let publisher_id_input = document.getElementById('publisher_id_input');
    $.ajax({
        method: 'POST',
        url: '/deletePublisherFromBook',
        data: {
            bookId: book_id_input.value,
            publisherId: publisher_id_input.value
        },
        success: function () {
            hideRemovePublisherModal();
            getPublishersForBook(book_id_input.value);
        },
        error: function (exception) {
            hideRemovePublisherModal();
            displayErrorMessageInModal(exception.responseText);
        }
    })
}

function removeGenreFromBook() {
    let book_id_input = document.getElementById('book_id');
    let genre_id_input = document.getElementById('genre_id_input');
    $.ajax({
        method: 'POST',
        url: '/removeGenreFromBook',
        data: {
            bookId: book_id_input.value,
            genreId: genre_id_input.value
        },
        success: function () {
            hideDeleteGenreFromBookModal();
            getGenresForBook(book_id_input.value);
        },
        error: function (exception) {
            hideDeleteGenreFromBookModal();
            displayErrorMessageInModal(exception.responseText);
        }
    })
}

function getBookByTitle(bookTitle) {
    $.ajax({
        url: '/findBookDetails',
        data: {title: bookTitle},
        success: function (bookDetails) {
            setBookInputFields(bookDetails);
            getAuthorsForBookByBookId(bookDetails.bookId);
            getPublishersForBook(bookDetails.bookId);
            getGenresForBook(bookDetails.bookId);
        },
        error: function (exception) {
            console.log(exception);
        }
    })
}

function getAuthorsForBookByBookId(book_id) {
    $.ajax({
        url: '/findAuthorsByBookId',
        data: {bookId: book_id},
        success: function (authors) {
            buildTableForBookAuthor(authors);
        }
    })
}

function getPublishersForBook(book_id) {
    $.ajax({
        url: '/findPublishersByBookId',
        data: {bookId: book_id},
        success: function (publishers) {
            buildTableBodyForBookPublishers(publishers);
        }
    })
}

function getGenresForBook(book_id) {
    $.ajax({
        url: '/findGenresByBookId',
        data: {bookId: book_id},
        success: function (genres) {
            buildTableBodyForBookGenres(genres);
        }
    })
}

function getAllAuthors() {
    $.ajax({
        url: '/findAllAuthors',
        success: function (authors) {
            displayAuthorsInSelect(authors);
        },
        error: function (exception) {

        }
    })
}

function getAllPublishers() {
    $.ajax({
        url: '/findAllPublishers',
        success: function (allPublishers) {
            displayPublishersInSelect(allPublishers);
        },
        error: function (exception) {

        }
    })
}

function getAllGenres() {
    $.ajax({
        url: '/findAllGenres',
        success: function (allGenres) {
            displayGenresInSelect(allGenres);
        },
        error: function (exception) {

        }
    })
}

function buildTableForBookAuthor(authors) {
    const tableBody = document.getElementById('tableData');
    const deleteAuthorLabel = document.getElementById('delete_author_btn').innerText;
    let authorsHtml = '';
    for (let author of authors) {
        authorsHtml += '<tr>' +
            '<td>' +
            '<div class="d-flex align-items-center">' +
            '<img class="rounded-circle" src=' + author.imagePath + '>' +
            '</div>' + '</td>' +
            '<td>' + author.firstName + '</td>' +
            '<td>' + author.lastName + '</td>' +
            '<td>' + author.birthDate + '</td>' +
            '<td>' +
            '<a type="button" ' +
            'class="btn btn-danger delete-btn" ' +
            'data-bs-toggle="modal" ' +
            'data-bs-target="#deleteAuthorFromBookModal" ' +
            'onclick="setAuthorIdInInputField(' + author.authorId + ')">' + deleteAuthorLabel + '</a>' +
            '</td>' + '</tr>';
    }
    tableBody.innerHTML = authorsHtml;
}

function buildTableBodyForBookPublishers(publishers) {
    const tableBody = document.getElementById('publisherTableData');
    const deleteBtn = document.getElementById('delete_author_btn').innerText;
    let publishersHtml = '';
    let counter = 0;
    for (let publisher of publishers) {
        counter = counter + 1;
        publishersHtml += '<tr>' +
            '<td>' + counter + '</td>' +
            '<td>' +
            '<div class="d-flex align-items-center">' +
            '<img class="img-thumbnail" style="width: 50px" src=' + publisher.imagePath + '>' +
            '</div>' + '</td>' +
            '<td style="width: 750px">' + publisher.name + '</td>' +
            '<td>' +
            '<a type="button" ' +
            'class="btn btn-danger delete-btn" ' +
            'data-bs-toggle="modal" ' +
            'data-bs-target="#deletePublisherFromBookModal" ' +
            'onclick="setPublisherIdInputField(' + publisher.publisherId + ')">' + deleteBtn + '</a>' +
            '</td>' + '</tr>';
    }
    tableBody.innerHTML = publishersHtml;
}

function buildTableBodyForBookGenres(genres) {
    const tableBody = document.getElementById('genreTableData');
    const deleteBtn = document.getElementById('delete_author_btn').innerText;
    let genresHtml = '';
    let counter = 0;
    for (let genre of genres) {
        counter = counter + 1;
        genresHtml += '<tr>' +
            '<td>' + counter + '</td>' +
            '<td style="width: 820px">' + genre.title + '</td>' +
            '<td>' +
            '<a type="button" ' +
            'class="btn btn-danger delete-btn" ' +
            'data-bs-toggle="modal" ' +
            'data-bs-target="#deleteGenreFromBookModal" ' +
            'onclick="setGenreIdInput(' + genre.genreId + ')">' + deleteBtn + '</a>' +
            '</td>' + '</tr>';
    }
    tableBody.innerHTML = genresHtml;
}

function displayAuthorsInSelect(authors) {
    let authors_select = document.getElementById('book_authors_to_select');
    for (let author of authors) {
        let firstName = author.firstName;
        let lastName = author.lastName;
        let authorId = author.authorId;
        authors_select.innerHTML +=
            '<option value="' + authorId + '">' + firstName + ' ' + lastName + '</option>';
    }
}

function displayPublishersInSelect(publishers) {
    let publishers_select = document.getElementById('book_publishers_to_select');
    for (let publisher of publishers) {
        let publisherName = publisher.name;
        let publisherId = publisher.publisherId;
        publishers_select.innerHTML +=
            '<option value="' + publisherId + '">' + publisherName + '</option>';
    }
}

function displayGenresInSelect(genres) {
    let genres_select = document.getElementById('book_genres_to_select');
    for (let genre of genres) {
        let genreTitle = genre.title;
        let genreId = genre.genreId;
        genres_select.innerHTML +=
            '<option value="' + genreId + '">' + genreTitle + '</option>';
    }
}

function setAuthorIdInInputField(authorId) {
    document.getElementById('author_id_input').value = authorId;
}

function setPublisherIdInputField(id) {
    let publisher_id_input = document.getElementById('publisher_id_input');
    publisher_id_input.value = id;
}

function setGenreIdInput(genreId) {
    let genre_id_input = document.getElementById('genre_id_input');
    genre_id_input.value = genreId;
}

function setBookInputFields(book) {
    let book_id_input = document.getElementById('book_id');
    book_id_input.value = book.bookId;

    let book_image = document.getElementById('book_image');
    book_image.src = book.imagePath;

    let book_title_input = document.getElementById('book_title');
    book_title_input.value = book.title;

    let book_price_input = document.getElementById('book_price');
    book_price_input.value = book.price;

    let book_isbn_input = document.getElementById('book_isbn');
    book_isbn_input.value = book.isbn;

    let book_pages_input = document.getElementById('book_pages');
    book_pages_input.value = book.pages;

    let book_description_input = document.getElementById('book_description');
    book_description_input.value = book.description;

    let old_image_input = document.getElementById('old_image_path');
    old_image_input.value = book.imagePath;

    setBookCoverTypeSelect(book);

    let date = book.publishDate;
    let dateArray = date.split('-');

    let yearSelect = addYearsToBookOption();
    let yearOption = '<option selected value="' + dateArray[0] + '">' + dateArray[0] + '</option>';
    yearSelect.innerHTML += yearOption;

    let month = dateArray[1] - 1;
    let monthSelect = addMonthToBookOption();
    let monthOption = '<option selected value="' + month + '">' + dateArray[1] + '</option>';
    monthSelect.innerHTML += monthOption;

    let daySelect = addDaysToBookOption();
    let dayOption = '<option selected value="' + dateArray[2] + '">' + dateArray[2] + '</option>';
    daySelect.innerHTML += dayOption;
}

function setBookCoverTypeSelect(book) {
    let book_cover_type_select = document.getElementById('book_cover_type');
    let hard_cover = document.getElementById('hard_cover').value;
    let soft_cover = document.getElementById('soft_cover').value;
    if (book.coverType === 'HARDCOVER') {
        book_cover_type_select.innerHTML =
            '<option selected value="HARDCOVER">' + hard_cover + '</option>' +
            '<option value="SOFTCOVER">' + soft_cover + '</option>';
    } else {
        book_cover_type_select.innerHTML =
            '<option selected value="SOFTCOVER">' + soft_cover + '</option>' +
            '<option value="HARDCOVER">' + hard_cover + '</option>';
    }
}

function addYearsToBookOption() {
    let start = 1800;
    let finish = new Date().getFullYear();
    let year_select_for_update = document.getElementById('year');
    let year_option_for_update = '';
    for (let i = start; i <= finish; i++) {
        year_option_for_update += '<option value="' + i + '">' + i + '</option>';
    }
    year_select_for_update.innerHTML = year_option_for_update;
    return year_select_for_update;
}

function addMonthToBookOption() {
    let start = 1;
    let finish = 12;
    let month_select_for_update = document.getElementById('month');
    let month_option_for_update = '';
    for (let i = start; i <= finish; i++) {
        let value = i - 1;
        month_option_for_update += '<option value="' + value + '">' + i + '</option>';
    }
    month_select_for_update.innerHTML = month_option_for_update;
    return month_select_for_update;
}

function addDaysToBookOption() {
    let start = 1;
    let finish = 31;
    let day_select_for_update = document.getElementById('day');
    let day_option_for_update = '';
    for (let i = start; i <= finish; i++) {
        let value = i;
        day_option_for_update += '<option value="' + value + '">' + value + '</option>';
    }
    day_select_for_update.innerHTML = day_option_for_update;
    return day_select_for_update;
}

function displaySuccessMessage() {
    $('#success_modal').modal('show');
}

function hideAddAuthorToBookModal() {
    $('#addAuthorToBookModal').modal('hide');
}

function hideRemoveAuthorFormBookModal() {
    $('#deleteAuthorFromBookModal').modal('hide');
}

function hideAddPublisherModal() {
    $('#addPublisherToBookModal').modal('hide');
}

function hideRemovePublisherModal() {
    $('#deletePublisherFromBookModal').modal('hide');
}

function hideAddGenreModal() {
    $('#addGenreToBookModal').modal('hide');
}

function hideDeleteGenreFromBookModal() {
    $('#deleteGenreFromBookModal').modal('hide');
}

function displayErrorMessageInModal(exception) {
    let errorMessageH5 = document.getElementById('errorModalMessage');
    let jsonResponse = JSON.parse(exception);
    errorMessageH5.innerText = jsonResponse['message'];
    $('#error_modal').modal('show');
}