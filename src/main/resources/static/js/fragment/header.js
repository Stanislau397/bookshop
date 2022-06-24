window.addEventListener('DOMContentLoaded', function() {
    let user_name = document.getElementById('user-name-h1').innerText;
    getUserByUsername(user_name);
});

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