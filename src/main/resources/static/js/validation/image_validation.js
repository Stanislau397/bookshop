const image_regex = /\.(jpe?g|png|gif|bmp)$/i;

window.addEventListener('DOMContentLoaded', function () {

});

function isImageValid(image) {
    if (!image_regex.test(image)) {
        displayInvalidImageAlertDiv();
        return false;
    }
    hideInvalidImageAlertDiv();
    return true;
}

function displayInvalidImageAlertDiv() {
    let image_alert_div = document.getElementById('image_alert');
    image_alert_div.style.display = 'block';
}

function hideInvalidImageAlertDiv() {
    let image_alert_div = document.getElementById('image_alert');
    image_alert_div.style.display = 'none';
}