const authorId = new URLSearchParams(window.location.search).get('authorId');

window.addEventListener('DOMContentLoaded', function () {
    getAuthorInfoById(authorId)
});

function updateAuthorInfo() {
    let day = document.getElementById('day');
    let month = document.getElementById('month');
    let year = document.getElementById('year');
    let updatedDate = new Date(Date.UTC(year.value, month.value, day.value))
        .toISOString().slice(0, 10);
    let image_path = document.getElementById('image').src;
    let form = new FormData();
    form.append('authorId', $('#author_id').val())
    form.append('firstName', $('#first-name').val());
    form.append('birthDate', updatedDate);
    form.append('lastName', $('#last-name').val());
    form.append('biography', $('#biography').val());
    form.append('imagePath', image_path);
    form.append('image', $('#file-input')[0].files[0]);
    $.ajax({
        method: 'POST',
        url: '/updateAuthorInfo',
        cache: false,
        processData: false,
        contentType: false,
        data: form,
        success: function () {
            getAuthorInfoById(authorId);
            displaySuccessMessage();
        },
        error: function (errorMessage) {
            displayErrorMessage(errorMessage.responseText)
        }
    })
}

function getAuthorInfoById(id) {
    $.ajax({
        url : '/findAuthorInfoById',
        data : {authorId : id},
        success : function (authorInfo) {
            setActorInfoForUpdateForm(authorInfo);
        }
    })
}

function setActorInfoForUpdateForm(authorInfo) {
    document.getElementById('author_id').value = authorInfo.authorId;
    document.getElementById('first-name').value = authorInfo.firstName;
    document.getElementById('last-name').value = authorInfo.lastName;
    document.getElementById('biography').value = authorInfo.biography;
    document.getElementById('image').src = authorInfo.imagePath;
    let date = authorInfo.birthDate;
    let dateArray = date.split('-');
    let yearSelect = setYearsSelect();
    let yearOption = '<option selected value="' + dateArray[0] +'">' + dateArray[0] + '</option>';
    yearSelect.innerHTML += yearOption;
    let month = dateArray[1] - 1;
    let monthSelect = setMonthsSelect();
    let monthOption = '<option selected value="' + month +'">' + dateArray[1] + '</option>';
    monthSelect.innerHTML += monthOption;
    let daySelect = setDateSelect();
    let dayOption = '<option selected value="' + dateArray[2] +'">' + dateArray[2] + '</option>';
    daySelect.innerHTML += dayOption;
}

function setYearsSelect() {
    let start = 1800;
    let finish = new Date().getFullYear();
    let year_select_for_update = document.getElementById('year');
    let year_option_for_update = '';
    for (let i = start; i <=finish; i++) {
        year_option_for_update += '<option value="' + i + '">' + i + '</option>';
    }
    year_select_for_update.innerHTML = year_option_for_update;
    return year_select_for_update;
}

function setMonthsSelect() {
    let start = 1;
    let finish = 12;
    let month_select_for_update = document.getElementById('month');
    let month_option_for_update = '';
    for (let i = start; i <= finish; i++) {
        let value = i - 1;
        month_option_for_update += '<option value="' + value + '">' + i + '</option>';
    }
    month_select_for_update.innerHTML = month_option_for_update;
    return month_select_for_update;
}

function setDateSelect() {
    let start = 1;
    let finish = 31;
    let day_select_for_update = document.getElementById('day');
    let day_option_for_update = '';
    for (let i = start; i <= finish; i++) {
        let value = i;
        day_option_for_update += '<option value="' + value + '">' + value + '</option>';
    }
    day_select_for_update.innerHTML = day_option_for_update;
    return day_select_for_update;
}

function displaySuccessMessage() {
    $('#success_modal').modal('show');
}

function displayErrorMessage(exception) {
    let errorMessageModalLabel = document.getElementById('errorModalMessage');
    let jsonResponse = JSON.parse(exception);
    errorMessageModalLabel.innerText = jsonResponse['message'];
    $('#error_modal').modal('show')
}

function previewImage() {
    let image = document.getElementById('image');
    let oFReader = new FileReader();
    oFReader.readAsDataURL(document.getElementById("file-input").files[0]);

    oFReader.onload = function (oFREvent) {
        image.src = oFREvent.target.result;
    }
}