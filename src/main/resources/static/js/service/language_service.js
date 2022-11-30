function findAllLanguages() {
    let languages = '';
    $.ajax({
        url: '/findAllLanguages',
        async: false,
        success: function (foundLanguages) {
            languages = foundLanguages;
            return languages;
        }
    })
    return languages;
}

function getCurrentLocale() {
    let locale = '';
    $.ajax({
        url: '/displayCurrentLocale',
        async: false,
        success: function (currentLocale) {
            locale = currentLocale;
            return locale;
        }
    })
    return locale;
}

function getLanguageByLocalizedBookTitle(book_title) {
    let language = '';
    $.ajax({
        url: '/displayLanguageByLocalizedBookTitle',
        data: {title: book_title},
        async: false,
        success: function (found_language) {
            language = found_language;
            return language;
        }
    })
    return language;
}