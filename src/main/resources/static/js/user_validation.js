function registerUser() {
    let user_name_input = $('#user_name').val();
    let email_input = $("#email").val();
    let password_input = $('#password').val();
    let userForm = new FormData();
    userForm.append('userName', user_name_input);
    userForm.append('email', email_input);
    userForm.append('password', password_input);
    $.ajax({
        method : 'POST',
        url : '/register_user',
        cache: false,
        contentType: false,
        processData: false,
        data: userForm,
        success : function (response) {
            console.log(response);
        },
        error : function (errorMessage) {
            console.log(errorMessage.responseText);
        }
    })
}


function validateUsername() {
    let user_name_input = document.getElementById('user_name');
    checkIfUsernameMatchesRegex(user_name_input);
    checkIfUsernameIsTaken(user_name_input);
}

function validateEmail() {
    let email_input = document.getElementById('email');
    checkIfEmailMatchesRegex(email_input);
    checkIfEmailIsTaken(email_input);
}

function checkIfUsernameIsTaken(user_name_input) {
    let username_alert_div = document.getElementById('user_name_alert');
    $.ajax({
        url: '/checkIfUsernameIsTaken',
        data: {username: user_name_input.value},
        success: function (usernameTaken) {
            if (usernameTaken) {
                username_alert_div.style.display = 'block'
            } else {
                username_alert_div.style.display = 'none';
            }
        }
    });
}

function checkIfUsernameMatchesRegex(user_name_input) {
    let maxUsernameLength = 25;
    let username_regex_alert_div = document.getElementById('username_regex_alert');
    let username_regex = /^[a-zA-Z0-9_]{4,}[a-zA-Z]+[0-9]*$/;
    if (user_name_input.value === '') {
        username_regex_alert_div.style.display = 'none';
        return false;
    }
    if (username_regex.test(user_name_input.value)
        && user_name_input.value.length <= maxUsernameLength) {
        username_regex_alert_div.style.display = 'none';
        return true;
    } else {
        username_regex_alert_div.style.display = 'block';
        return false;
    }
}


function checkIfEmailIsTaken(email_input) {
    let email_alert_div = document.getElementById('email_alert');
    $.ajax({
        url: '/checkIfEmailIsTaken',
        data: {email: email_input.value},
        success: function (emailTaken) {
            if (emailTaken) {
                email_alert_div.style.display = 'block';
            } else {
                email_alert_div.style.display = 'none';
            }
        }
    });
}

function checkIfEmailMatchesRegex(email_input) {
    let email_regex = /^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/;
    let email_regex_alert_div = document.getElementById('email_regex_alert');
    if (email_input.value === '') {
        email_regex_alert_div.style.display = 'none';
        return false;
    }
    if (email_regex.test(email_input.value)) {
        email_regex_alert_div.style.display = 'none';
        return true;
    } else {
        email_regex_alert_div.style.display = 'block';
        return false;
    }
}

function checkIfPasswordMatchesRegex() {
    let password_regex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/;
    let password_input = document.getElementById('password');
    let password_regex_alert_div = document.getElementById('password_regex_alert');
    if (password_input.value === '') {
        password_regex_alert_div.style.display = 'none';
        return false;
    }
    if (password_regex.test(password_input.value)) {
        password_regex_alert_div.style.display = 'none';
        return true;
    } else {
        password_regex_alert_div.style.display = 'block';
        return false;
    }
}

function checkIfPasswordMatchesConfirmPassword() {
    let password_input = document.getElementById('password');
    let confirm_password_input = document.getElementById('confirm-password');
    let passwords_not_equal_alert_div = document.getElementById('passwords_not_equal_alert');
    if (confirm_password_input.value === '') {
        passwords_not_equal_alert_div.style.display = 'none';
        return false;
    }
    if (password_input.value === confirm_password_input.value) {
        passwords_not_equal_alert_div.style.display = 'none';
        return true;
    } else {
        passwords_not_equal_alert_div.style.display = 'block';
        return false;
    }
}