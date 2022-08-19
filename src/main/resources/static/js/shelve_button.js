function getBookStatusOnShelve(book_id, shelve_id) {
    let foundStatus;
    $.ajax({
        url: '/findBookStatusOnShelveByShelveIdAndBookId',
        data: {
            bookId: book_id,
            shelveId: shelve_id
        },
        async: false,
        success: function (bookStatus) {
            foundStatus = bookStatus;
            return foundStatus;
        },
        error: function (error) {
            console.log(error.responseText);
        }
    })
    return foundStatus;
}

function checkIfBookExistsInShelve(book_id, shelve_id) {
    let result;
    $.ajax({
        url: '/isBookExistsInShelve',
        data: {
            bookId: book_id,
            shelveId: shelve_id
        },
        async: false,
        success: function (bookExists) {
            result = bookExists;
            return result;
        }
    })
    return result;
}

function changeBookStatusOnShelve() {
    let radio_input = document.querySelector('input[name="choice"]:checked').value;
    let book_id = document.getElementById('book_id').value;
    let shelve_id = document.getElementById('shelve_id').value;
    $.ajax({
        method: 'POST',
        url: '/updateBookStatusOnShelve',
        data: {
            bookStatus: radio_input,
            bookId: book_id,
            shelveId: shelve_id
        },
        success: function () {
            hideEditBookStatusModal();
        },
        error: function (error) {
            console.log(error.responseText);
        }
    })
}

function displayButton(book_id, shelve_id, book_title, button_container) {
    let add_btn = document.getElementById('add').value;
    let change_btn = document.getElementById('change').value;
    let bookExists = checkIfBookExistsInShelve(book_id, shelve_id);
    if (bookExists) {
        button_container.innerHTML =
            '<button type="button" ' +
            'data-bs-toggle="modal" ' +
            'data-bs-target="#editBookStatusModal" ' +
            'onclick="setBookShelveInfoInModalHeader(\'' + shelve_id + '\',\'' + book_id + '\',\'' + book_title + '\')" ' +
            'class="change-book-status-btn" id="change_book_status_btn"> ' +
            '<i class="fa fa-check">' + '</i>' + change_btn + '</button>'
    } else {
        button_container.innerHTML =
            '<button type="button" ' +
            'onclick="addBookToShelve(\'' + book_id + '\',\'' + "WANT_TO_READ" + '\')" ' +
            'class="book-shelve-btn">' +
            '<i class="fa fa-plus">' + '</i>' + add_btn + '</button>'
    }
}

function setBookShelveInfoInModalHeader(shelve_id, book_id, book_title) {
    let book_title_h5 = document.getElementById('book_title_in_modal_header');
    book_title_h5.innerText = book_title;
    let shelve_id_input = document.getElementById('shelve_id');
    shelve_id_input.value = shelve_id;
    let book_id_input = document.getElementById('book_id');
    book_id_input.value = book_id;
    let book_status = getBookStatusOnShelve(book_id, shelve_id);
    $("input[name=choice][value=" + book_status + "]").prop('checked', true);
}

function hideEditBookStatusModal() {
    $('#editBookStatusModal').modal('hide');
}