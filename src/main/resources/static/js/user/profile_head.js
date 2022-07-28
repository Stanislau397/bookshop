window.addEventListener('DOMContentLoaded', function () {
    let user_name = findUserName();
    getUserProfileInfo(user_name);
    getUserRolesByUsername(user_name);
});

function getUserProfileInfo(user_name) {
    $.ajax({
        url: '/findUserByUsername',
        data: {username: user_name},
        success: function (foundUser) {
            setUserProfileAvatar(foundUser.avatarName);
            setProfileUsername(user_name);
        }
    })
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