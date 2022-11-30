const image_regex = /\.(jpe?g|png|gif|bmp)$/i;

window.addEventListener('DOMContentLoaded', function () {

});

function isImageValid() {
    let file_input = document.getElementById('file-input');
    let image_name = file_input.files[0].name;
    if (!image_regex.test(image_name)) {
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