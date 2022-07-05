window.addEventListener('DOMContentLoaded', function () {
    let page = new URLSearchParams(window.location.search).get('page');
    getPublishersByPage(page);
});

function addPublisher() {
    let page = new URLSearchParams(window.location.search).get('page');
    let publisherForm = new FormData();
    publisherForm.append('publisherImage', $('#file-input')[0].files[0]);
    publisherForm.append('name', $('#publisher_name').val());
    publisherForm.append('description', $('#publisher_description').val());
    $.ajax({
        method: 'POST',
        url: '/addPublisher',
        cache: false,
        processData: false,
        contentType: false,
        data: publisherForm,
        success: function () {
            resetPublisherFormFields();
            hideAddPublisherModal();
            getPublishersByPage(page);
        },
        error: function (errorMessage) {
            hideAddPublisherModal();
            displayExceptionModalWithErrorMessage(errorMessage.responseText);
        }
    })
}

function deletePublisherById() {
    let page = new URLSearchParams(window.location.search).get('page');
    let publisher_id_input = document.getElementById('publisher_id');
    $.ajax({
        method: 'POST',
        url : '/deletePublisherById',
        data : {publisherId : publisher_id_input.value},
        success : function () {
            hideDeletePublisherModal();
            getPublishersByPage(page);

        },
        error : function (exception) {
            hideDeletePublisherModal();
            displayExceptionModalWithErrorMessage(exception.responseText);
        }
    })
}

function getPublishersByPage(pageNumber) {
    $.ajax({
        url: '/findPublishersByPage',
        data: {page: pageNumber},
        success: function (publishers) {
            buildTableBodyForPublishers(publishers.content);
        },
        error: function (exception) {

        }
    })
}

function getPublisherByKeyWord() {
    let keywordInput = document.getElementById('keyword');
    let page = new URLSearchParams(window.location.search).get('page');
    if (keywordInput.value !== '') {
        $.ajax({
            url: '/findPublishersByKeyword',
            data: {keyWord: keywordInput.value},
            success: function (publishers) {
                buildTableBodyForPublishers(publishers);
                hideErrorMessageInSearch();
            },
            error: function (exception) {
                setErrorMessageInSearch(exception.responseText);
            }
        });
    } else {
        getPublishersByPage(page);
        hideErrorMessageInSearch();
    }
}

function buildTableBodyForPublishers(publishers) {
    const tableBody = document.getElementById('tableData');
    const editBtn = document.getElementById('edit-btn-name').value;
    const deleteBtn = document.getElementById('delete-btn-name').value;
    let authorsHtml = '';
    let counter = 0;
    for (let publisher of publishers) {
        counter = counter + 1;
        authorsHtml += '<tr>' +
            '<td>' + counter + '</td>' +
            '<td>' +
            '<div class="d-flex align-items-center">' +
            '<img class="img-thumbnail" style="width: 50px" src=' + publisher.imagePath + '>' +
            '</div>' + '</td>' +
            '<td style="width: 770px">' + publisher.name + '</td>' +
            '<td>' +
            '<a type="button" ' +
            'class="btn btn-secondary" ' +
            'href="">' + editBtn + '</a>' +
            '<a type="button" ' +
            'class="btn btn-danger role-btn" ' +
            'data-bs-toggle="modal" ' +
            'data-bs-target="#deletePublisherModal" ' +
            'onclick="setPublisherIdInputField(' + publisher.publisherId + ')">' + deleteBtn + '</a>' +
            '</td>' + '</tr>';
    }
    tableBody.innerHTML = authorsHtml;
}

function setErrorMessageInSearch(exception) {
    const tableBody = document.getElementById('tableData');
    tableBody.innerHTML = '';
    let errorMessageDiv = document.getElementById('error-message-for-search');
    let jsonResponse = JSON.parse(exception);
    errorMessageDiv.innerText = jsonResponse['message'];
}

function hideErrorMessageInSearch() {
    let errorMessageDiv = document.getElementById('error-message-for-search');
    errorMessageDiv.innerText = '';
}

function hideAddPublisherModal() {
    $('#addPublisherModal').modal('hide');
}

function hideDeletePublisherModal() {
    $('#deletePublisherModal').modal('hide');
}

function displayExceptionModalWithErrorMessage(exception) {
    $('#errorModal').modal('show');
    let errorMessageH5 = document.getElementById('error-message-for-search');
    let jsonResponse = JSON.parse(exception);
    errorMessageH5.innerText = jsonResponse['message'];
}

function resetPublisherFormFields() {
    let default_image_src = 'http://localhost:8090/image/book/default.png';
    let name_field = document.getElementById('publisher_name');
    name_field.value = '';
    let description_field = document.getElementById('publisher_description');
    description_field.value = '';
    document.getElementById('publisher_image').src = default_image_src;
}

function setPublisherIdInputField(id) {
    let publisher_id_input = document.getElementById('publisher_id');
    publisher_id_input.value = id;
}