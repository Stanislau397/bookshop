let profileUser;


window.addEventListener('DOMContentLoaded', function () {
    let user_name = findUserName();
    profileUser = getUserProfileInfo(user_name);
    getUserRolesByUsername(user_name);
    let shelve_id = profileUser.bookShelve.bookShelveId;
    let avatar = profileUser.avatarName;
    setUserProfileAvatar(avatar);
    setProfileUsername(user_name);
    displayNumberOfWantToReadBooks(shelve_id, 'WANT_TO_READ');
    displayNumberOfReadBooks(shelve_id, 'READ');
    displayNumberOfCurrentlyReadingBooks(shelve_id, 'CURRENTLY_READING')
});

function getUserProfileInfo(user_name) {
    $.ajax({
        url: '/findUserByUsername',
        data: {username: user_name},
        async: false,
        success: function (foundUser) {
            profileUser = foundUser;
            return profileUser;
        }
    })
    return profileUser;
}

function countBooksByShelveIdAndBookStatus(shelve_id, book_status) {
    let number_of_books = 0;
    $.ajax({
        url: '/findNumberOfBooksOnShelve',
        data: {
            shelveId: shelve_id,
            bookStatus: book_status
        },
        async: false,
        success: function (numberOfBooksByStatus) {
            number_of_books = numberOfBooksByStatus;
            return number_of_books;
        }
    })
    return number_of_books;
}

function getBooksByStatusAndShelveId(book_status, shelve_id) {
    let found_books_by_status_and_shelve_id;
    $.ajax({
        url: '/findBooksByShelveIdAndBookStatus',
        data: {
            shelveId: shelve_id,
            bookStatus: book_status,
            pageNumber : 1
        },
        async : false,
        success : function (booksByShelveIdAndStatus) {
            found_books_by_status_and_shelve_id = booksByShelveIdAndStatus;
            return found_books_by_status_and_shelve_id;
        }
    })
    return found_books_by_status_and_shelve_id;
}

function getUserRolesByUsername(user_name) {
    $.ajax({
        url: '/findUserRoles',
        data: {userName: user_name},
        success: function (roles) {
            setUserRolesInProfile(roles);
        }
    })
}

function setUserProfileAvatar(avatarPath) {
    let image = document.getElementById('account_image');
    image.src = avatarPath;
}

function findUserName() {
    let user_name = new URLSearchParams(window.location.search).get('username');
    if (user_name != null) {
        user_name = new URLSearchParams(window.location.search).get('username');
        return user_name;
    } else {
        if (document.getElementById('user-name-h1') != null) {
            user_name = document.getElementById('user-name-h1').innerText;
            return user_name;
        }
    }
    return user_name;
}

function setProfileUsername(username) {
    let user_name_div = document.getElementById('user_name');
    user_name_div.innerText = username;
}

function setUserRolesInProfile(roles) {
    let user_role = document.getElementById('user_role').value;
    let admin_role = document.getElementById('admin_role').value;
    let role_div = document.getElementById('user_roles');
    for (let role of roles) {
        let roleName = role.roleName;
        if (roleName === 'ADMIN') {
            roleName = admin_role;
        } else {
            roleName = user_role;
        }
        role_div.innerHTML += roleName;
    }
}

function displayBooksByStatus(book_status) {
    let shelve_id = profileUser.bookShelve.bookShelveId;
    let books = getBooksByStatusAndShelveId(book_status, shelve_id);
    console.log(books);
}

function displayNumberOfWantToReadBooks(shelve_id, book_status) {
    let numberOfWantToReadBooks = countBooksByShelveIdAndBookStatus(shelve_id, book_status);
    if (numberOfWantToReadBooks !== 0) {
        let want_to_read_button = document.getElementById('want_to_read_link');
        want_to_read_button.innerText += ' ' + numberOfWantToReadBooks;
    }
}

function displayNumberOfReadBooks(shelve_id, book_status) {
    let numberOfReadBooks = countBooksByShelveIdAndBookStatus(shelve_id, book_status);
    if (numberOfReadBooks !== 0) {
        let read_button = document.getElementById('read_link');
        read_button.innerText += ' ' + numberOfReadBooks;
    }
}

function displayNumberOfCurrentlyReadingBooks(shelve_id, book_status) {
    let numberOfCurrentlyReadingBooks = countBooksByShelveIdAndBookStatus(shelve_id, book_status);
    if (numberOfCurrentlyReadingBooks !== 0) {
        let currently_reading_button = document.getElementById('currently_reading_link');
        currently_reading_button.innerText += ' ' + numberOfCurrentlyReadingBooks;
    }
}