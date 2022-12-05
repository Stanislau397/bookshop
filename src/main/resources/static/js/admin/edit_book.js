window.addEventListener('DOMContentLoaded', function () {
    let book_id_from_parameter = new URLSearchParams(window.location.search).get('id');
    displayBookDetailsForEditByBookTitle(book_id_from_parameter);
    getAllAuthors();
    getAllPublishers();
    getAllGenres();
});

function createUpdatedLocalizedBook() {
    let localized_title = document.getElementById('book_title').value;
    let localized_description = document.getElementById('book_description').value;
    let localized_book_id = document.getElementById('localized_book_id').value;

    let image_path = document.getElementById('book_image').src;
    let file_input = document.getElementById('file-input');
    if (file_input.files.length !== 0) {
        image_path = file_input.files[0].name;
    }
    return {
        "localizedBookId": localized_book_id,
        "title": localized_title,
        "description": localized_description,
        "imagePath": image_path
    };
}

function createUpdatedBook() {
    let day = document.getElementById('day');
    let month = document.getElementById('month');
    let year = document.getElementById('year');
    let date = new Date(Date.UTC(year.value, month.value, day.value))
        .toISOString().slice(0, 10);
    let book_price = document.getElementById('book_price').value;
    let book_pages = document.getElementById('book_pages').value;
    let book_isbn = document.getElementById('book_isbn').value;
    let book_cover_type = document.getElementById('book_cover_type').value;
    let book_id = document.getElementById('book_id').value;

    return {
        "bookId": book_id,
        "publishDate": date,
        "price": book_price,
        "pages": book_pages,
        "isbn": book_isbn,
        "coverType": book_cover_type
    }
}


function editBook() {
    let updated_book = createUpdatedBook();
    let updated_localized_book = createUpdatedLocalizedBook();
    console.log(updated_localized_book.localizedBookId)
    let formData = new FormData();
    formData.append("imageFromRequest", $('#file-input')[0].files[0]);
    formData.append('localizedBookFromRequest', new Blob([JSON.stringify(updated_localized_book)], {
        type: "application/json"
    }));
    formData.append('bookFromRequest', new Blob([JSON.stringify(updated_book)], {
        type: "application/json"
    }));
    if (isBookDataValid(updated_localized_book.title, updated_localized_book.description, updated_book.isbn,
            updated_book.price, updated_book.coverType, updated_book.pages)
        && isDateValid(updated_book.publishDate)
        && isImageValid(updated_localized_book.imagePath)) {
        $.ajax({
            method: 'POST',
            url: '/updateBookInfo',
            contentType: false,
            processData: false,
            data: formData,
            success: function (bookUpdated) {
                if (bookUpdated) {
                    displaySuccessMessage();
                }
            },
            error: function (exception) {
                displayErrorMessageInModal(exception.responseText);
            }
        })
    }
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

function displayBookDetailsForEditByBookTitle(book_id) {
    let localized_book_details = getLocalizedBookByBookId(book_id);
    setBookInputFields(localized_book_details);
    getAuthorsForBookByBookId(localized_book_details.book.bookId);
    getGenresForBook(localized_book_details.book.bookId);
    getPublishersForBook(localized_book_details.book.bookId);
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

function setBookInputFields(localized_book) {
    let book_id_input = document.getElementById('book_id');
    book_id_input.value = localized_book.book.bookId;

    let localized_book_id_input = document.getElementById('localized_book_id');
    localized_book_id_input.value = localized_book.localizedBookId;

    let book_image = document.getElementById('book_image');
    book_image.src = localized_book.imagePath;

    let book_title_input = document.getElementById('book_title');
    book_title_input.value = localized_book.title;

    let book_price_input = document.getElementById('book_price');
    book_price_input.value = localized_book.book.price;

    let book_isbn_input = document.getElementById('book_isbn');
    book_isbn_input.value = localized_book.book.isbn;

    let book_pages_input = document.getElementById('book_pages');
    book_pages_input.value = localized_book.book.pages;

    let book_description_input = document.getElementById('book_description');
    book_description_input.value = localized_book.description;

    let old_image_input = document.getElementById('old_image_path');
    old_image_input.value = localized_book.imagePath;

    setBookCoverTypeSelect(localized_book.book);

    let date = localized_book.book.publishDate;
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