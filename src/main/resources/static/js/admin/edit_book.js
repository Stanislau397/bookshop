window.addEventListener('DOMContentLoaded', function () {
    let bookTitleFromParameter = new URLSearchParams(window.location.search).get('title');
    getBookByTitle(bookTitleFromParameter);
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
            history.replaceState(null, null, "?"+queryParams.toString());
            let bookTitleFromParameter = new URLSearchParams(window.location.search).get('title');
            getBookByTitle(bookTitleFromParameter);
            console.log(isBookUpdated);
        },
        error: function (errorMessage) {
            console.log(errorMessage.responseText);
        }
    })
}

function getBookByTitle(bookTitle) {
    $.ajax({
        url: '/findBookDetails',
        data: {title: bookTitle},
        success: function (bookDetails) {
            setBookInputFields(bookDetails);
        },
        error: function (exception) {
            console.log(exception);
        }
    })
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
    let yearOption = '<option selected value="' + dateArray[0] +'">' + dateArray[0] + '</option>';
    yearSelect.innerHTML += yearOption;

    let month = dateArray[1] - 1;
    let monthSelect = addMonthToBookOption();
    let monthOption = '<option selected value="' + month +'">' + dateArray[1] + '</option>';
    monthSelect.innerHTML += monthOption;

    let daySelect = addDaysToBookOption();
    let dayOption = '<option selected value="' + dateArray[2] +'">' + dateArray[2] + '</option>';
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
    for (let i = start; i <=finish; i++) {
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