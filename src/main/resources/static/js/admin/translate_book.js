window.addEventListener('DOMContentLoaded', function () {
    let book_id = new URLSearchParams(window.location.search).get('id');
    displayLocalizedBookDetails(book_id);
    displayLanguagesInSelect();
});

function displayLocalizedBookDetails(book_id) {
    let book_in_english = getLocalizedBookByBookId(book_id);

    let image = document.getElementById('book_image');
    image.src = book_in_english.localizedBook.imagePath;

    let old_image_path_input = document.getElementById('old_image_path');
    old_image_path_input.value = book_in_english.localizedBook.imagePath;

    let book_title_input = document.getElementById('book_title');
    book_title_input.value = book_in_english.localizedBook.title;

    let book_description_input = document.getElementById('book_description');
    book_description_input.value = book_in_english.localizedBook.description;

    let book_id_input = document.getElementById('book_id');
    book_id_input.value = book_in_english.bookId;

    let price_input = document.getElementById('price');
    price_input.value = book_in_english.price;
    let pages_input = document.getElementById('pages');
    pages_input.value = book_in_english.pages;
    let publish_date_input = document.getElementById('publish_date');
    publish_date_input.value = book_in_english.publishDate;
    let isbn_input = document.getElementById('isbn');
    isbn_input.value = book_in_english.isbn;
    let cover_type_input = document.getElementById('cover_type');
    cover_type_input.value = book_in_english.coverType;
}


function createLocalizedBook() {
    let localized_title = document.getElementById('book_title').value;
    let localized_description = document.getElementById('book_description').value;

    let image_path = document.getElementById('old_image_path').value;
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

function createBookDto() {
    let localized_book = createLocalizedBook();
    let book_id = document.getElementById('book_id').value;
    let price = document.getElementById('price').value;
    let pages = document.getElementById('pages').value;
    let publish_date = document.getElementById('publish_date').value;
    let isbn = document.getElementById('isbn').value;
    let cover_type = document.getElementById('cover_type').value;
    console.log(isbn)
    return {
        "bookId": book_id,
        "price": price,
        "pages": pages,
        "isbn" : isbn,
        "publishDate": publish_date,
        "coverType": cover_type,
        localizedBook: localized_book
    }
}


function translate_book() {
    let formData = new FormData();
    let bookDto = createBookDto();
    let language_name = document.getElementById('language').value;
    formData.append("localizedImage", $('#file-input')[0].files[0]);
    formData.append('bookToTranslate', new Blob([JSON.stringify(bookDto)], {
        type: "application/json"
    }));
    formData.append('languageName', language_name);
    if (isBookTitleValid(bookDto.localizedBook.title)
        && isBookDescriptionValid(bookDto.localizedBook.description)
        && isImageValid(bookDto.localizedBook.imagePath)) {

        $.ajax({
            method: 'POST',
            url: '/addLocalizationToBook',
            contentType: false,
            processData: false,
            data: formData,
            success: function () {
                window.history.back();
            },
            error: function (exception) {
                displayErrorMessage(exception.responseText)
            }
        })
    }
}

function displayErrorMessage(exception) {
    let errorMessageDiv = document.getElementById('error_message');
    let jsonResponse = JSON.parse(exception);
    errorMessageDiv.innerText = jsonResponse['message'];
    showErrorModal();
}

function showErrorModal() {
    $('#errorModal').modal('show');
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

