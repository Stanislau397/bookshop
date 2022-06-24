window.addEventListener('DOMContentLoaded', function () {
    let user_name = document.getElementById('authenticated-user-name').innerText;
    getUserByUsername(user_name)
});

function getUserByUsername(user_name) {
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

function setUserAvatar(avatar_src) {
    let avatar_img = document.getElementById('user-avatar');
    avatar_img.src = avatar_src;
}