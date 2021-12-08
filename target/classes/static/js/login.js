function save_tokens(tokens) {
    var xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        if (xhr.readyState == XMLHttpRequest.DONE){
            home_redirect(tokens);
        }
    };

    xhr.open('POST', '/save-tokens', true);
    xhr.send(tokens);
}

function home_redirect(tokens){
    const obj = JSON.parse(tokens)

    document.getElementById('token-input').value = obj.access_token
    document.getElementById('home').click()
}

// function login(){
//     var userName = document.getElementById("username").value;
//     var password = document.getElementById("password").value;
//     var data = { username : userName, password : password }
//     var xhr = new XMLHttpRequest();
//
//     // xhr.onload = function () {
//     //     if (xhr.readyState == XMLHttpRequest.DONE){
//     //         save_tokens(xhr.responseText);
//     //     }
//     // };
//
//     xhr.onreadystatechange = function () {
//         console.log(xhr.responseText);
//         if (xhr.status === 200){
//             console.log(xhr.responseText);
//             save_tokens(xhr.responseText);
//         }
//     };
//
//     xhr.open('POST', '/login/pass', true);
//     xhr.send(JSON.stringify(data));
// }

document.querySelector('#input-form').addEventListener("submit", function (e) {
    e.preventDefault();
    var userName = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var data = { username : userName, password : password }
    var xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        console.log(xhr.responseText);
        if (xhr.readyState === 4 && xhr.status === 200){
            alert(xhr.responseText);
            save_tokens(xhr.responseText);
        }
    };

    xhr.open('POST', '/login/pass', true);
    xhr.send(JSON.stringify(data));
});

// function executeLogin() {
//     var userName = document.getElementById("username").value;
//     var password = document.getElementById("password").value;
//     var data = { username : userName, password : password }
//     let url = '/login/pass';
//
//     fetch(url, {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json',
//         },
//         body: JSON.stringify(data),
//     })
//         .then(response => response.json())
//         .then(data => {
//             console.log('Success:', data);
//         })
//         .catch((error) => {
//             console.error('Error:', error);
//         }); // parses JSON response into native JavaScript objects
// }

// function login(){
//     executeLogin();
// }
