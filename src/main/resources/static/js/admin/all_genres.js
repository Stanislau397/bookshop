window.addEventListener('DOMContentLoaded', function () {
    let page = new URLSearchParams(window.location.search).get('page');
    getGenresByPage(page)
});

function addGenre() {
    let genre_title_input = document.getElementById('title');
    $.ajax({
        method: 'POST',
        url: '/addGenre',
        data: {title: genre_title_input.value},
        success: function () {
            let page = new URLSearchParams(window.location.search).get('page');
            hideAddGenreModal();
            getGenresByPage(page);
        },
        error: function (exception) {
            hideAddGenreModal();
            showErrorModalWithMessage(exception.responseText);
        }
    })
}

function editGenreTitle() {
    let genre_title_input = document.getElementById('title-to-edit');
    let genre_id_for_edit = document.getElementById('genre-id-for-edit');
    console.log(genre_id_for_edit.value);
    $.ajax({
        method: 'POST',
        url: '/updateGenreTitle',
        data: {
            title: genre_title_input.value,
            genreId: genre_id_for_edit.value
        },
        success: function () {
            let page = new URLSearchParams(window.location.search).get('page');
            hideEditGenreModal();
            getGenresByPage(page);
        },
        error: function (exception) {
            hideEditGenreModal();
            showErrorModalWithMessage(exception.responseText);
        }
    })
}

function deleteGenreById() {
    let genre_id_input = document.getElementById('genre-id');
    $.ajax({
        method: 'POST',
        url: '/deleteGenreById',
        data: {genreId: genre_id_input.value},
        success: function () {
            let page = new URLSearchParams(window.location.search).get('page');
            hideDeleteGenreModal();
            getGenresByPage(page);
        },
        error: function (exception) {
            hideDeleteGenreModal();
            showErrorModalWithMessage(exception.responseText);
        }
    })
}

function getGenresByPage(pageNumber) {
    let url = window.location.href.split('?')[0] + "?page=" + pageNumber;
    history.pushState(undefined, '', url);
    $.ajax({
        url: '/findGenresByPage',
        data: {page: pageNumber},
        success: function (foundGenresByPage) {
            buildTableBodyForGenres(foundGenresByPage.content);
            buildPaginationForGenresTable(foundGenresByPage.totalPages);
        },
        error: function (exception) {

        }
    });
}

function getGenresByKeyWord() {
    let keywordInput = document.getElementById('keyword');
    let page = new URLSearchParams(window.location.search).get('page');
    if (keywordInput.value !== '') {
        $.ajax({
            url: '/findGenresByKeyword',
            data: {keyWord: keywordInput.value},
            success: function (foundAuthorsByKeyword) {
                buildTableBodyForGenres(foundAuthorsByKeyword);
            },
            error: function (exception) {

            }
        });
    } else {
        getGenresByPage(page);
    }
}

function buildTableBodyForGenres(genres) {
    const tableBody = document.getElementById('tableData');
    const editBtn = document.getElementById('edit-btn-name').value;
    const deleteBtn = document.getElementById('delete-btn-name').value;
    let genresHtml = '';
    let counter = 0;
    for (let genre of genres) {
        counter = counter + 1;
        genresHtml += '<tr>' +
            '<td>' + counter + '</td>' +
            '<td style="width: 820px">' + genre.title + '</td>' +
            '<td>' +
            '<button type="button" ' +
            'class="btn btn-secondary role-btn" ' +
            'data-bs-toggle="modal" ' +
            'data-bs-target="#editGenreModal" ' +
            'value="' + genre.title + '" ' +
            'onclick="setGenreTitleAndGenreIdToEdit(this.value, ' + genre.genreId + ')">' + editBtn + '</button>' +
            '<a type="button" ' +
            'class="btn btn-danger role-btn" ' +
            'data-bs-toggle="modal" ' +
            'data-bs-target="#deleteGenreModal" ' +
            'onclick="setGenreIdInput(' + genre.genreId + ')">' + deleteBtn + '</a>' +
            '</td>' + '</tr>';
    }
    tableBody.innerHTML = genresHtml;
}

function buildPaginationForGenresTable(pages) {
    let pagination_ul = document.getElementById('genres-pagination');
    pagination_ul.innerHTML = '';
    if (pages > 1) {
        for (let i = 0; i < pages; i++) {
            let page = i + 1;
            pagination_ul.innerHTML +=
                '<li class="page-item">' +
                '<button class="page-link shadow-none" ' +
                'onclick="getGenresByPage(' + page + ')">' + page + '</button>' +
                '</li>';
        }
    }
}

function setGenreIdInput(genreId) {
    let genre_id_input = document.getElementById('genre-id');
    genre_id_input.value = genreId;
}

function setGenreTitleAndGenreIdToEdit(genreTitle, genreId) {
    let genre_title_input = document.getElementById('title-to-edit');
    genre_title_input.value = genreTitle;
    let genre_id_for_edit_input = document.getElementById('genre-id-for-edit');
    genre_id_for_edit_input.value = genreId;
}

function showErrorModalWithMessage(exception) {
    $('#errorModal').modal('show');
    let error_message_text = document.getElementById('error-message');
    let jsonResponse = JSON.parse(exception);
    error_message_text.innerText = jsonResponse['message'];
}

function hideAddGenreModal() {
    $('#addGenreModal').modal('hide');
}

function hideDeleteGenreModal() {
    $('#deleteGenreModal').modal('hide');
}

function hideEditGenreModal() {
    $('#editGenreModal').modal('hide');
}