const title_regex = new XRegExp("^[\\p{L}\\s\\-_,\\.:;()''\"\"]+$");
const description_min_length = 100;
const description_max_length = 2000;

window.addEventListener('DOMContentLoaded', function () {

});

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