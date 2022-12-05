const title_regex = new XRegExp("^[\\p{L}\\s\\-_,\\.:;()''\"\"]+$");
const isbn_regex = new RegExp("^(?:ISBN(?:-13)?:?\\ )?" +
    "(?=[0-9]{13}$|(?=(?:[0-9]+[-\\ ]){4})" +
    "[-\\ 0-9]{17}$)97[89][-\\ ]?[0-9]{1,5}" +
    "[-\\ ]?[0-9]+[-\\ ]?[0-9]+[-\\ ]?[0-9]$");
const price_regex = /^(?:\d*\.\d{1,2}|\d+)$/;
const description_min_length = 100;
const description_max_length = 2000;
const min_amount_of_pages = 24;
const max_amount_of_pages = 1500;

window.addEventListener('DOMContentLoaded', function () {
});

function isBookDataValid(title, description, isbn, price, cover_type, pages) {
    return isBookTitleValid(title)
        && isBookDescriptionValid(description)
        && isIsbnValid(isbn)
        && isPriceValid(price)
        && isCoverTypesContains(cover_type)
        && isAmountOfPagesValid(pages);
}

function isBookTitleValid(title) {
    if (!title_regex.test(title)) {
        displayInvalidTitleAlert();
        return false;
    }
    hideInvalidTitleAlert();
    return true;
}


function isBookDescriptionValid(description) {
    if (description.length > description_min_length
        && description.length < description_max_length) {
        hideInvalidDescriptionAlertDiv();
        return true;
    }
    displayInvalidDescriptionAlertDiv();
    return false;
}

function isIsbnValid(isbn) {
    if (!isbn_regex.test(isbn)) {
        displayInvalidIsbnAlertDiv();
        return false;
    }
    hideInvalidIsbnAlertDiv();
    return true;
}

function isAmountOfPagesValid(pages) {
    if (pages >= min_amount_of_pages && pages <= max_amount_of_pages) {
        hideInvalidPagesAlertDiv();
        return true;
    }
    displayInvalidPagesAlertDiv();
    return false;
}

function isPriceValid(price) {
    if (price_regex.test(price)) {
        hideInvalidPriceAlertDiv();
        return true;
    }
    displayInvalidPriceAlertDiv();
    return false;
}

function isCoverTypesContains(selected_cover_type) {
    let cover_types = getAllCoverTypes();
    if (cover_types.includes(selected_cover_type)) {
        hideInvalidCoverTypeAlertDiv();
        return true;
    }
    displayInvalidCoverTypeAlertDiv();
    return false;
}

function displayInvalidTitleAlert() {
    let title_alert_div = document.getElementById('book_title_alert');
    title_alert_div.style.display = 'block';
}

function hideInvalidTitleAlert() {
    let title_alert_div = document.getElementById('book_title_alert');
    title_alert_div.style.display = 'none';
}

function displayInvalidDescriptionAlertDiv() {
    let description_alert_div = document.getElementById('book_description_alert');
    description_alert_div.style.display = 'block';
}

function hideInvalidDescriptionAlertDiv() {
    let description_alert_div = document.getElementById('book_description_alert');
    description_alert_div.style.display = 'none';
}

function displayInvalidIsbnAlertDiv() {
    let description_alert_div = document.getElementById('book_isbn_alert');
    description_alert_div.style.display = 'block';
}

function hideInvalidIsbnAlertDiv() {
    let description_alert_div = document.getElementById('book_isbn_alert');
    description_alert_div.style.display = 'none';
}

function displayInvalidPagesAlertDiv() {
    let description_alert_div = document.getElementById('book_pages_alert');
    description_alert_div.style.display = 'block';
}

function hideInvalidPagesAlertDiv() {
    let description_alert_div = document.getElementById('book_pages_alert');
    description_alert_div.style.display = 'none';
}

function displayInvalidPriceAlertDiv() {
    let description_alert_div = document.getElementById('book_price_alert');
    description_alert_div.style.display = 'block';
}

function hideInvalidPriceAlertDiv() {
    let description_alert_div = document.getElementById('book_price_alert');
    description_alert_div.style.display = 'none';
}

function displayInvalidCoverTypeAlertDiv() {
    let description_alert_div = document.getElementById('book_cover_types_alert');
    description_alert_div.style.display = 'block';
}

function hideInvalidCoverTypeAlertDiv() {
    let description_alert_div = document.getElementById('book_cover_types_alert');
    description_alert_div.style.display = 'none';
}