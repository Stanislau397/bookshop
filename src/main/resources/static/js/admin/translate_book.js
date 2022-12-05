window.addEventListener('DOMContentLoaded', function () {
    let book_id = new URLSearchParams(window.location.search).get('id');
    displayLocalizedBookDetails(book_id);
    displayLanguagesInSelect();
});

function displayLocalizedBookDetails(book_id) {
    let book_in_english = getLocalizedBookByBookId(book_id);

    let image = document.getElementById('book_image');
    image.src = book_in_english.imagePath;

    let old_image_path_input = document.getElementById('old_image_path');
    old_image_path_input.value = book_in_english.imagePath;

    let book_title_input = document.getElementById('book_title');
    book_title_input.value = book_in_english.title;

    let book_description_input = document.getElementById('book_description');
    book_description_input.value = book_in_english.description;
}


function createLocalizedBook() {
    let title_from_url = new URLSearchParams(window.location.search).get('title');
    let localized_title = document.getElementById('book_title').value;
    let localized_description = document.getElementById('book_description').value;
    let book = getBookByLocalizedBookTitle(title_from_url);

    let image_path = document.getElementById('old_image_path').value;
    let file_input = document.getElementById('file-input');
    if (file_input.files.length !== 0) {
        image_path = file_input.files[0].name;
    }
    return {
        "title": localized_title,
        "description": localized_description,
        "imagePath": image_path,
        "book": book
    };
}


function translate_book() {
    let formData = new FormData();
    let localized_book = createLocalizedBook();
    let language_name = document.getElementById('language').value;
    formData.append("localizedImage", $('#file-input')[0].files[0]);
    formData.append('localizedBook', new Blob([JSON.stringify(localized_book)], {
        type: "application/json"
    }));
    formData.append('languageName', language_name);
    if (isBookTitleValid(localized_book.title)
        && isBookDescriptionValid(localized_book.description)
        && isImageValid(localized_book.imagePath)) {

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

