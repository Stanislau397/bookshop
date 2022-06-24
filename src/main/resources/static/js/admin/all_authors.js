window.addEventListener('DOMContentLoaded', function () {
    let page = new URLSearchParams(window.location.search).get('page');
    getAuthorsByPage(page);
    addMonthToOption();
    addDaysToOption();
    addYearsToOption();
});

function addAuthor() {
    let day = document.getElementById('day');
    let month = document.getElementById('month');
    let year = document.getElementById('year');
    let date = new Date(Date.UTC(year.value, month.value, day.value))
        .toISOString().slice(0, 10);
    let form = new FormData();
    form.append('firstName', $('#first_name').val());
    form.append('birthDate', date);
    form.append('lastName', $('#last_name').val());
    form.append('biography', $('#biography').val())
    form.append('image', $('#file-input')[0].files[0]);
    $.ajax({
        method: 'POST',
        url: '/addAuthor',
        cache: false,
        processData: false,
        contentType: false,
        data: form,
        success: function () {
            let page = new URLSearchParams(window.location.search).get('page');
            resetAuthorForm();
            hideAddAuthorModal();
            getAuthorsByPage(page);
        },
        error: function (errorMessage) {
            console.log(errorMessage.responseText);
        }
    })
}

function deleteAuthor() {
    let author_id_input = document.getElementById('author_id_input');
    $.ajax({
        method: 'POST',
        url : '/deleteAuthorById',
        data : {authorId : author_id_input.value},
        success : function () {
            let page = new URLSearchParams(window.location.search).get('page');
            $('#deleteAuthorModal').modal('hide');
            getAuthorsByPage(page);
        },
        error : function () {

        }
    })
}

function getAuthorsByKeyword() {
    let keywordInput = document.getElementById('keyword');
    let page = new URLSearchParams(window.location.search).get('page');
    if (page !== '') {
        $.ajax({
            url: '/findAuthorsByKeyword',
            data: {keyWord: keywordInput.value},
            success: function (foundAuthorsByKeyword) {
                buildTableBodyForAuthors(foundAuthorsByKeyword);
            },
            error: function (exception) {

            }
        });
    } else {
        getUsersPerPage(page);
    }
}

function getAuthorsByPage(pageNumber) {
    let url = window.location.href.split('?')[0] + "?page=" + pageNumber;
    history.pushState(undefined, '', url);
    $.ajax({
        url: '/findAuthorsByPage',
        data: {page: pageNumber},
        success: function (foundAuthorsByPage) {
            buildTableBodyForAuthors(foundAuthorsByPage.content);
            buildPaginationForAuthorsTable(foundAuthorsByPage.totalPages);
        },
        error: function (exception) {

        }
    });
}


function addYearsToOption() {
    let start = 1900;
    let finish = new Date().getFullYear();
    let year_select = document.getElementById('year');
    for (let i = start; i <= finish; i++) {
        year_select.innerHTML += '<option value="' + i + '">' + i + '</option>';
    }
}

function addMonthToOption() {
    let start = 1;
    let finish = 12;
    let month_select = document.getElementById('month');
    for (let i = start; i <= finish; i++) {
        let value = i - 1;
        month_select.innerHTML += '<option value="' + value + '">' + i + '</option>';
    }
}

function addDaysToOption() {
    let start = 1;
    let finish = 31;
    let day_select = document.getElementById('day');
    for (let i = start; i <= finish; i++) {
        let value = i;
        day_select.innerHTML += '<option value="' + value + '">' + value + '</option>';
    }
}

function resetAuthorForm() {
    let first_name = document.getElementById('first_name');
    first_name.value = '';
    let last_name = document.getElementById('last_name');
    last_name.value = '';
    let biography = document.getElementById('biography');
    biography.value = '';
    document.getElementById('author_image').src = "http://localhost:8090/image/author/default-author.jpg";
}

function hideAddAuthorModal() {
    $('#addAuthorModal').modal('hide');
}

function setAuthorIdInInputField(authorId) {
    document.getElementById('author_id_input').value = authorId;
}

function buildTableBodyForAuthors(authors) {
    const tableBody = document.getElementById('tableData');
    const editBtnName = document.getElementById('edit-btn-name').value;
    const deleteAuthorLabel = document.getElementById('delete_author_btn').innerText;
    let authorsHtml = '';
    for (let author of authors) {
        authorsHtml += '<tr>' +
            '<td>' +
            '<div class="d-flex align-items-center">' +
            '<img class="rounded-circle" src=' + author.imagePath + '>' +
            '</div>' + '</td>' +
            '<td>' + author.firstName + '</td>' +
            '<td>' + author.lastName + '</td>' +
            '<td>' + author.birthDate + '</td>' +
            '<td>' +
            '<a type="button" ' +
            'class="btn btn-secondary role-btn" ' +
            'href="/admin/cabinet/edit_author?authorId='+author.authorId +'">' + editBtnName + '</a>' +
            '<a type="button" ' +
            'class="btn btn-danger role-btn" ' +
            'data-bs-toggle="modal" ' +
            'data-bs-target="#deleteAuthorModal" ' +
            'onclick="setAuthorIdInInputField('+author.authorId+')">' + deleteAuthorLabel + '</a>' +
            '</td>' + '</tr>';
    }
    tableBody.innerHTML = authorsHtml;
}

function buildPaginationForAuthorsTable(pages) {
    let pagination_ul = document.getElementById('authors-pagination');
    pagination_ul.innerHTML = '';
    if (pages > 1) {
        for (let i = 0; i < pages; i++) {
            let page = i + 1;
            pagination_ul.innerHTML +=
                '<li class="page-item">' +
                '<button class="page-link shadow-none" ' +
                'onclick="getAuthorsByPage(' + page + ')">' + page + '</button>' +
                '</li>';
        }
    }
}