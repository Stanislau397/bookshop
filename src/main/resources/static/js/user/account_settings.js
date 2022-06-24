window.addEventListener('DOMContentLoaded', function () {
    let user_name = document.getElementById('user-name-h1').innerText;
    findUserInfoByUsername(user_name);
});

function findUserInfoByUsername(user_name) {
    $.ajax({
        url: '/findUserByUsername',
        data: {username: user_name},
        success: function (userByUsername) {
            let avatar_scr = userByUsername.avatarName;
            setUserAvatar(avatar_scr);
        },
        error: function (exception) {

        }
    });
}

function changeUserAvatar() {
    let user_name = document.getElementById('user-name-h1').innerText;
    let dataForm = new FormData();
    dataForm.append('userName', user_name);
    dataForm.append('avatar', $('#file-input')[0].files[0])
    $.ajax({
        method: 'POST',
        url: '/updateUserAvatarByUsername',
        cache: false,
        contentType: false,
        processData: false,
        data: dataForm,
        success: function () {
            hideChangeAvatarModal();
            showChangeImageSuccessModal();
            findUserInfoByUsername(user_name);
            getUserByUsername(user_name);
            resetImageInput();
        },
        error: function (exception) {
            resetImageInput();
            hideChangeAvatarModal();
            displayExceptionMessage(exception.responseText);
        }
    })
}

function changeUserPassword() {
    let user_name = document.getElementById('user-name-h1').innerText;
    $.ajax({
        method: 'POST',
        url: '/updateUserPasswordByUsername',
        data: {
            userName: user_name,
            oldPassword: $('#old_password').val(),
            newPassword: $('#password').val()
        },
        success: function () {
            showPasswordSuccessModal();
            resetPasswordInput();
        },
        error: function (exception) {
            displayExceptionMessage(exception.responseText);
            resetPasswordInput();
        }
    })
}

function setUserAvatar(avatarPath) {
    let userAvatar = document.getElementById('user-avatar');
    userAvatar.src = avatarPath;
}

function setUserAvatarInModal() {
    let image = document.getElementById('user-avatar');
    let userAvatar = document.getElementById('avatar-to-change');
    userAvatar.src = image.src;
}

function hideChangeAvatarModal() {
    $('#change-avatar-modal').modal('hide')
}

function showPasswordSuccessModal() {
    $('#password-success-modal').modal('show')
}

function showChangeImageSuccessModal() {
    $('#image-success-modal').modal('show')
}

function resetImageInput() {
    document.getElementById("file-input").value = "";
}

function resetPasswordInput() {
    let oldPassword = document.getElementById('old_password');
    oldPassword.value = '';
    let newPassword = document.getElementById('password');
    newPassword.value = '';
    let confirmPassword = document.getElementById('confirm-password');
    confirmPassword.value = '';
}

function displayExceptionMessage(exception) {
    let errorMessageModalLabel = document.getElementById('exceptionModalLabel');
    let jsonResponse = JSON.parse(exception);
    errorMessageModalLabel.innerText = jsonResponse['message'];
    $('#exception-modal').modal('show')
}