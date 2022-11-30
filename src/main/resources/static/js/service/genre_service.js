function getLocalizedGenresByLanguageNameAndPageNumber(language_name, page_number) {
    let localized_genres_by_lang_and_page = '';
    $.ajax({
        url: '/displayLocalizedGenresByLanguageNameAndPageNumber',
        data: {
            languageName: language_name,
            pageNumber: page_number
        },
        async: false,
        success: function (found_localized_genres_by_lang_and_page) {
            localized_genres_by_lang_and_page = found_localized_genres_by_lang_and_page;
            return localized_genres_by_lang_and_page;
        }
    })
    return localized_genres_by_lang_and_page;
}

function getLocalizedGenresByKeywordAndLanguage(key_word, language) {
    let localized_genres_by_keyword_and_language = '';
    $.ajax({
        url: '/displayLocalizedGenresByKeywordAndLanguage',
        data: {
            keyword: key_word,
            languageName: language
        },
        async: false,
        success: function (found_localized_genres) {
            localized_genres_by_keyword_and_language = found_localized_genres;
            return localized_genres_by_keyword_and_language;
        }
    })
    return localized_genres_by_keyword_and_language;
}

function checkIfLocalizedGenreExistsByTitleAndLanguage(genre_title) {
    let language_name = new URLSearchParams(window.location.search).get('lang');
    $.ajax({
        url: '/checkIfLocalizedGenreExistsByTitleAndLanguage',
        data: {
            title: genre_title,
            languageName: language_name
        },
        success : function (localizedGenreExists) {
            console.log(localizedGenreExists)
        },
        error : function (exception) {
            console.log(exception.responseText);
        }
    })
}