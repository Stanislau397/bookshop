let user;

window.addEventListener('DOMContentLoaded', function () {
    if (document.getElementById('user-name-h1') !== null) {
        let user_name = document.getElementById('user-name-h1').innerText;
        user = getUserByUsername(user_name);
    }
    getAllGenresForDropDown();
});

function getBooksByKeyWord(key_word) {
    if (key_word !== '') {
        $.ajax({
            url: '/findBooksByKeyword',
            data: {keyWord: key_word},
            success: function (booksByKeyWord) {
                displaySearchResults(booksByKeyWord);
            },
            error: function (exception) {
                displayErrorMessageInSearchResults(exception.responseText);
            }
        })
    } else {
        let search_result_container = document.getElementById('search_result');
        search_result_container.innerHTML = '';
    }
}

function getAllGenresForDropDown() {
    $.ajax({
        url: '/findAllGenres',
        success: function (allGenres) {
            displayAllGenresInDropDown(allGenres);
        }
    })
}

function getUserByUsername(user_name) {
    let user_by_username;
    $.ajax({
        url: '/findUserByUsername',
        data: {username: user_name},
        async: false,
        success: function (userByUsername) {
            let avatar_scr = userByUsername.avatarName;
            setUserAvatarInHeader(avatar_scr);
            setPathToUserProfileInHeader(user_name);
            setShelveId(userByUsername);
            user_by_username = userByUsername;
            return user_by_username;
        },
        error: function (exception) {

        }
    });
    return user_by_username;
}

function setUserAvatarInHeader(avatar_src) {
    let avatar_img = document.getElementById('navbarScrollingDropdown');
    avatar_img.src = avatar_src;
}

function displaySearchResults(booksByKeyWord) {
    let search_result_container = document.getElementById('search_result');
    search_result_container.innerHTML = '';
    for (let book of booksByKeyWord) {
        let book_href = 'http://localhost:8070/bookshop/book?title=' + book.title;
        let book_href_with_under_scores = book_href.replace(/ /g, "_")
        let publishDate = book.publishDate;
        let dateArray = publishDate.split('-');
        let year = dateArray[0];
        search_result_container.innerHTML +=
            '<a class="book-container" href="' + book_href_with_under_scores + '">' +
            '<div class="book-image">' + '<img class="found-image" src="' + book.imagePath + '">' + '</div>' +
            '<div class="book-info">' +
            '<div class="book-name">' + book.title + '</div>' +
            '<div class="book-year">' + year + '</div>' + '</div>' + '</a>';
    }
}

function displayAllGenresInDropDown(all_genres) {
    let genres_ul = document.getElementById('genres_ul');
    for (let genre of all_genres) {
        let title = genre.title;
        let booksByGenreHref = 'http://localhost:8070/bookshop/booksByGenre?genreTitle=' + title + '&page=1';
        genres_ul.innerHTML +=
            '<li class="genre-li">' + '<a class="genre-href" href="' + booksByGenreHref + '">' + title + '</a>' +
            '</li>';
    }
}

function displayErrorMessageInSearchResults(exception) {
    let search_result_div = document.getElementById('search_result');
    search_result_div.innerHTML = '';
    let jsonResponse = JSON.parse(exception);
    search_result_div.innerHTML += '<div class="error-message">' + jsonResponse['message'] + '</div>';
}

function openSearchResultsPage() {
    let key_word = document.getElementById('key_word').value;
    let search_by_keyword_a = document.getElementById('search_by_keyword');
    search_by_keyword_a.href = 'http://localhost:8070/bookshop/booksByKeyWord?keyWord=' + key_word + '&page=1';
}

function setPathToUserProfileInHeader(user_name) {
    let path = 'http://localhost:8070/user/profile?username=' + user_name;
    let profile_a = document.getElementById('profile_path');
    profile_a.href = path;
}

function setShelveId(user) {
    let shelve_id_input = document.getElementById('shelve_id');
    shelve_id_input.value = user.bookShelve.bookShelveId;
}