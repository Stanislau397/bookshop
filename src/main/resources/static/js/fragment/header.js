window.addEventListener('DOMContentLoaded', function () {
    let user_name = document.getElementById('user-name-h1').innerText;
    getUserByUsername(user_name);
});

function getBooksByKeyWord(key_word) {
    if (key_word !== '') {
        $.ajax({
            url: '/findBooksByKeyword',
            data: {keyWord: key_word},
            success: function (booksByKeyWord) {
                displaySearchResults(booksByKeyWord);
            },
            error : function (exception) {
                displayErrorMessageInSearchResults(exception.responseText);
            }
        })
    } else {
        let search_result_container = document.getElementById('search_result');
        search_result_container.innerHTML = '';
    }
}

function getUserByUsername(user_name) {
    $.ajax({
        url: '/findUserByUsername',
        data: {username: user_name},
        success: function (userByUsername) {
            let avatar_scr = userByUsername.avatarName;
            setUserAvatarInHeader(avatar_scr);
        },
        error: function (exception) {

        }
    });
}

function setUserAvatarInHeader(avatar_src) {
    let avatar_img = document.getElementById('navbarScrollingDropdown');
    avatar_img.src = avatar_src;
}

function displaySearchResults(booksByKeyWord) {
    let search_result_container = document.getElementById('search_result');
    search_result_container.innerHTML = '';
    for (let book of booksByKeyWord) {
        let publishDate = book.publishDate;
        let dateArray = publishDate.split('-');
        let year = dateArray[0];
        search_result_container.innerHTML +=
            '<a class="book-container" href="http://localhost:8070/bookshop/book?title=' + book.title + '">' +
            '<div class="book-image">' + '<img class="found-image" src="' + book.imagePath + '">' + '</div>' +
            '<div class="book-info">' +
            '<div class="book-name">' + book.title + '</div>' +
            '<div class="book-year">' + year + '</div>' + '</div>' + '</a>';
    }
}

function displayErrorMessageInSearchResults(exception) {
    let search_result_div = document.getElementById('search_result');
    search_result_div.innerHTML = '';
    let jsonResponse = JSON.parse(exception);
    search_result_div.innerHTML += '<div class="error-message">' + jsonResponse['message'] + '</div>';
}