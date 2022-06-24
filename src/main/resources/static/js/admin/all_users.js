window.addEventListener('DOMContentLoaded', function () {
    let page = new URLSearchParams(window.location.search).get('page');
    getUsersPerPage(page);
    findAllRoles();
});

function changeUserStatusByUsername() {
    let userName_input = document.getElementById('user_name_input');
    $.ajax({
        method: 'POST',
        url: '/updateUserStatusByUsername',
        cache: false,
        data: {userName: userName_input.value},
        success: function () {
            let page = new URLSearchParams(window.location.search).get('page');
            getUsersPerPage(page);
        },
        error: function (exception) {

        }
    })
}

function changeUserRole() {
    let user_id_input = document.getElementById('user_id_input');
    let selected_role = document.getElementById('roles');
    $.ajax({
        method: 'POST',
        url: '/updateUserRoleByUserId',
        cache: false,
        data: {
            userId: user_id_input.value,
            roleId: selected_role.value
        },
        success: function () {
            let page = new URLSearchParams(window.location.search).get('page');
            getUsersPerPage(page);
            $('#roleModal').modal('hide');
        },
        error: function (exception) {

        }
    })
}

function getUsersByKeyword() {
    let keywordInput = document.getElementById('keyword');
    let page = new URLSearchParams(window.location.search).get('page');
    if (page !== '') {
        $.ajax({
            url: '/findUsersByKeyword',
            data: {keyword: keywordInput.value},
            success: function (foundUsersByKeyword) {
                hideErrorMessage();
                buildTableBodyForUsers(foundUsersByKeyword);
            },
            error: function (exception) {
                displayErrorMessage(exception.responseText);
            }
        });
    } else {
        getUsersPerPage(page);
    }
}

function getUsersPerPage(pageNumber) {
    let url = window.location.href.split('?')[0] + "?page=" + pageNumber;
    history.pushState(undefined, '', url);
    $.ajax({
        url: '/findUsersWithPagination',
        data: {page: pageNumber},
        success: function (foundUsersWithPagination) {
            hideErrorMessage();
            buildTableBodyForUsers(foundUsersWithPagination.content);
            buildPaginationForUsersTable(foundUsersWithPagination.totalPages);
        },
        error: function (exception) {
            displayErrorMessage(exception.responseText);
        }
    });
}

function findAllRoles() {
    $.get('/findAllRoles', function (allRoles) {
        buildOptionOfRoles(allRoles);
    });
}

function displayErrorMessage(exception) {
    const tableBody = document.getElementById('tableData');
    tableBody.innerHTML = '';
    let errorMessageDiv = document.getElementById('error-message');
    let jsonResponse = JSON.parse(exception);
    errorMessageDiv.innerText = jsonResponse['message'];
}

function hideErrorMessage() {
    let errorMessageDiv = document.getElementById('error-message');
    errorMessageDiv.innerText = '';
}

function setUsernameInputField(input) {
    let user_name_input = document.getElementById('user_name_input');
    user_name_input.value = input;
}

function setUserIdInputField(input) {
    let user_id_input = document.getElementById('user_id_input');
    user_id_input.value = input;
}

function buildTableBodyForUsers(users) {
    const status_label = document.getElementById('statusModalLabel').innerText;
    const role_label = document.getElementById('roleModalModalLabel').innerText;
    const tableBody = document.getElementById('tableData');
    let usersHtml = '';
    for (let user of users) {
        let user_roles = parseUserRoles(user.roles);
        let userStatus = 'Active';
        if (user.locked) {
            userStatus = 'Banned'
        }
        usersHtml += '<tr>' +
            '<td>' +
            '<div class="d-flex align-items-center">' +
            '<img class="rounded-circle" src=' + user.avatarName + '>' +
            '<div class="ms-3">' +
            '<p class="fw-bold mb-1">' + user.userName + '</p>' +
            '<p class="text-muted mb-0">' + user.email + '</p>' +
            '</div>' +
            '</div>' + '</td>' +
            '<td>' + user_roles + '</td>' +
            '<td>' + userStatus + '</td>' +
            '<td>' + '<button type="button" ' +
            'class="btn btn-primary" ' +
            'data-bs-toggle="modal" data-bs-target="#statusModal" ' +
            'value="' + user.userName + '" ' +
            'onclick="setUsernameInputField(this.value)">' + status_label + '</button>' +
            '<button type="button" ' +
            'class="btn btn-secondary role-btn" ' +
            'data-bs-toggle="modal" ' +
            'data-bs-target="#roleModal" ' +
            'value="' + user.userId + '" ' +
            'onclick="setUserIdInputField(this.value)">' + role_label + '</button>' +
            '</td>' + '</tr>';
    }
    tableBody.innerHTML = usersHtml;
}

function buildOptionOfRoles(givenRoles) {
    let roles_select = document.getElementById('roles');
    roles_select.innerHTML = '';
    let role_option = '';
    for (let role of givenRoles) {
        role_option += '<option value="' + role.roleId + '">' + role.roleName + '</option>';
    }
    roles_select.innerHTML = role_option;
}

function buildPaginationForUsersTable(pages) {
    let pagination_ul = document.getElementById('users-pagination');
    pagination_ul.innerHTML = '';
    if (pages > 1) {
        for (let i = 0; i < pages; i++) {
            let page = i + 1;
            pagination_ul.innerHTML +=
                '<li class="page-item">' +
                '<button class="page-link" ' +
                'onclick="getUsersPerPage(' + page + ')">' + page + '</button>' +
                '</li>';
        }
    }
}

function parseUserRoles(roles) {
    let found_roles = '';
    for (let i = 0; i < roles.length; i++) {
        if (i !== roles.length - 1) {
            found_roles = found_roles.concat(roles[i].roleName).concat(',');
        } else {
            found_roles = found_roles.concat(roles[i].roleName);
        }
    }
    return found_roles;
}