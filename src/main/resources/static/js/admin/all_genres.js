window.addEventListener('DOMContentLoaded', function () {
    let page_number = new URLSearchParams(window.location.search).get('page');
    let language_name = new URLSearchParams(window.location.search).get('lang');
    displayLocalizedGenres(language_name, page_number);
    displayAllLanguages();
});

function displayLocalizedGenres(language_name, page_number) {
    let localized_genres = '';
    let url = '';
    if (language_name === null || language_name === '' || language_name === 'null') {
        localized_genres = getLocalizedGenresByLanguageNameAndPageNumber(language_name, page_number);
        let language = getCurrentLocale();
        url = window.location.href.split('?')[0] + "?page=" + page_number + '&lang=' + language;
        history.pushState(undefined, '', url);
        buildTableBodyForLocalizedGenres(localized_genres.content);
    } else {
        url = window.location.href.split('?')[0] + "?page=" + page_number + '&lang=' + language_name;
        history.pushState(undefined, '', url);
        localized_genres = getLocalizedGenresByLanguageNameAndPageNumber(language_name, page_number);
        buildTableBodyForLocalizedGenres(localized_genres.content);
    }

    let total_pages = localized_genres.totalPages;
    buildPaginationForGenresTable(total_pages);
}

function displayLocalizedGenresByKeywordAndLanguage() {
    let key_word = document.getElementById('keyword').value;
    let language = new URLSearchParams(window.location.search).get('lang');
    let localized_genres_by_keyword_and_lang = getLocalizedGenresByKeywordAndLanguage(key_word, language);
    buildTableBodyForLocalizedGenres(localized_genres_by_keyword_and_lang);
}

function addGenre() {
    let genre_title = document.getElementById('genre_title').value;
    let selected_language = document.getElementById('language').value;
    $.ajax({
        method: 'POST',
        url: '/addGenre',
        data: {
            genreTitle: genre_title,
            language: selected_language
        },
        success: function () {
            let page = new URLSearchParams(window.location.search).get('page');
            let language_name = new URLSearchParams(window.location.search).get('lang');
            hideAddGenreModal();
            displayLocalizedGenres(language_name, page);
        },
        error: function (exception) {
            hideAddGenreModal();
            showErrorModalWithMessage(exception.responseText);
        }
    })
}

function addTranslationToGenre() {
    let genre_title_input = document.getElementById('title_to_translate');
    let genre_id_for_edit = document.getElementById('genre_id_for_translation');
    let language_name = document.getElementById('language_name_for_translation');
    $.ajax({
        method: 'POST',
        url: '/addLocalizedGenreByTitleAndGenreIdAndLanguageName',
        data: {
            genreTitle: genre_title_input.value,
            genreId: genre_id_for_edit.value,
            languageName: language_name.value
        },
        success: function () {
            let page = new URLSearchParams(window.location.search).get('page');
            let language_name = new URLSearchParams(window.location.search).get('lang');
            hideTranslateGenreModal();
            displayLocalizedGenres(language_name, page);
        },
        error: function (exception) {
            hideTranslateGenreModal();
            showErrorModalWithMessage(exception.responseText);
        }
    })
}

function editGenreTitle() {
    let genre_title_input = document.getElementById('title_to_edit');
    let genre_id_for_edit = document.getElementById('genre_id');
    let language_name = document.getElementById('genre_language');
    $.ajax({
        method: 'POST',
        url: '/updateLocalizedGenreByGenreIdAndLanguageName',
        data: {
            genreTitle: genre_title_input.value,
            genreId: genre_id_for_edit.value,
            languageName: language_name.value
        },
        success: function () {
            let page = new URLSearchParams(window.location.search).get('page');
            let language_name = new URLSearchParams(window.location.search).get('lang');
            hideEditGenreModal();
            displayLocalizedGenres(language_name, page);
        },
        error: function (exception) {
            hideEditGenreModal();
            showErrorModalWithMessage(exception.responseText);
        }
    })
}

function deleteLocalizedGenreById() {
    let genre_id_input = document.getElementById('genre-id');
    let page_number = new URLSearchParams(window.location.search).get('page');
    let language_name = new URLSearchParams(window.location.search).get('lang');
    $.ajax({
        method: 'POST',
        url: '/removeLocalizedGenreById',
        data: {localizedGenreId: genre_id_input.value},
        success: function () {
            hideDeleteGenreModal();
            displayLocalizedGenres(language_name, page_number)
        },
        error: function (exception) {
            hideDeleteGenreModal();
            showErrorModalWithMessage(exception.responseText);
        }
    })
}

function buildTableBodyForLocalizedGenres(localized_genres_by_lang_and_page) {
    const tableBody = document.getElementById('tableData');
    const editBtn = document.getElementById('edit-btn-name').value;
    const deleteBtn = document.getElementById('delete-btn-name').value;
    const translate_text = document.getElementById('translate_text').value;
    let language_name_from_url = new URLSearchParams(window.location.search).get('lang');
    let genresHtml = '';
    let counter = 0;
    for (let genre of localized_genres_by_lang_and_page) {
        counter = counter + 1;
        let genre_title = genre.title;
        genresHtml += '<tr>' +
            '<td>' + counter + '</td>' +
            '<td style="width: 850px">' + genre_title + '</td>' +
            '<td>';
        if (language_name_from_url !== genre.language.name) {
            genresHtml += '<button type="button" ' +
                'class="btn btn-secondary role-btn" ' +
                'data-bs-toggle="modal" ' +
                'data-bs-target="#translateGenreModal" ' +
                'value="' + genre.title + '" ' +
                'onclick="setGenreIdAndLanguageNameForTranslation(\'' + genre.genre.genreId + '\',\'' + language_name_from_url + '\')">' + translate_text +
                '</button>'
        } else {
            genresHtml += '<button type="button" ' +
                'class="btn btn-secondary role-btn" ' +
                'data-bs-toggle="modal" ' +
                'data-bs-target="#editGenreModal" ' +
                'value="' + genre.title + '" ' +
                'onclick="setGenreTitleAndGenreIdToEdit(\'' + genre.title + '\',\'' + genre.genre.genreId + '\',\'' + genre.language.name + '\')">' + editBtn + '</button>'
        }

        genresHtml += '<a type="button" ' +
            'class="btn btn-danger role-btn" ' +
            'data-bs-toggle="modal" ' +
            'data-bs-target="#deleteGenreModal" ' +
            'onclick="setGenreIdInput(' + genre.localizedGenreId + ')">' + deleteBtn + '</a>' +
            '</td>' + '</tr>';
    }
    tableBody.innerHTML = genresHtml;
}

function displayAllLanguages() {
    let page = new URLSearchParams(window.location.search).get('page');
    let lang_ul = document.getElementById('lang_ul');
    let all_languages = findAllLanguages();
    for (let language of all_languages) {
        let lang_name = language.name;
        lang_ul.innerHTML +=
            '<li onclick="displayLocalizedGenres(\'' + lang_name + '\',\'' + page + '\')">' + lang_name + '</li>';
    }
}

function buildPaginationForGenresTable(pages) {
    let pagination_ul = document.getElementById('genres-pagination');
    let language_name = new URLSearchParams(window.location.search).get('lang');
    pagination_ul.innerHTML = '';
    if (pages > 1) {
        for (let i = 0; i < pages; i++) {
            let page = i + 1;
            pagination_ul.innerHTML +=
                '<li class="page-item">' +
                '<button class="page-link shadow-none" ' +
                'onclick="displayLocalizedGenres(\'' + language_name + '\',\'' + page + '\')">' + page + '</button>' +
                '</li>';
        }
    }
}

function setGenreIdInput(genreId) {
    let genre_id_input = document.getElementById('genre-id');
    genre_id_input.value = genreId;
}

function setGenreIdAndLanguageNameForTranslation(genreId, languageName) {
    let genre_id_input = document.getElementById('genre_id_for_translation');
    genre_id_input.value = genreId;
    let language_name_input = document.getElementById('language_name_for_translation');
    language_name_input.value = languageName;
}

function setGenreTitleAndGenreIdToEdit(genreTitle, genreId, language) {
    let genre_title_input = document.getElementById('title_to_edit');
    genre_title_input.value = genreTitle;
    let genre_id_for_edit_input = document.getElementById('genre_id');
    genre_id_for_edit_input.value = genreId;
    let genre_language_input = document.getElementById('genre_language');
    genre_language_input.value = language;
}

function showErrorModalWithMessage(exception) {
    $('#errorModal').modal('show');
    let error_message_text = document.getElementById('error-message');
    let jsonResponse = JSON.parse(exception);
    error_message_text.innerText = jsonResponse['message'];
}

function hideAddGenreModal() {
    $('#addGenreModal').modal('hide');
}

function hideDeleteGenreModal() {
    $('#deleteGenreModal').modal('hide');
}

function hideEditGenreModal() {
    $('#editGenreModal').modal('hide');
}

function hideTranslateGenreModal() {
    $('#translateGenreModal').modal('hide');
}