window.addEventListener('DOMContentLoaded', function () {
    hideFirstNameAlertMessage();
    hideLastNameAlertMessage();
});

function validateFirstName(firstName) {
    let firstNameRegex = "^[A-Z][a-z]+\\s[A-Z][a-z]+\\s[A-Z][a-z]+$";
    if (firstName.match(firstNameRegex)) {
        hideFirstNameAlertMessage();
    } else {
        displayFirstNameAlertMessage();
    }
}

function validateLastName(lastName) {
    let lastNameRegex = /^(?=.{1,30}$)[a-z]+(?:['_.\\s][a-z]+)*$/i;
    if (lastNameRegex.test(lastName)) {
        hideLastNameAlertMessage();
    } else {
        showLastNameAlertMessage();
    }
}

function hideFirstNameAlertMessage() {
    $('#first-name-alert').hide();
}

function hideLastNameAlertMessage() {
    $('#last-name-alert').hide();
}

function showLastNameAlertMessage() {
    $('#last-name-alert').show();
}

function displayFirstNameAlertMessage() {
    $('#first-name-alert').show();
}