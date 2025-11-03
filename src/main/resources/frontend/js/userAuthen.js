// for Login.html
let loginForm = document.querySelector(".my-form");

loginForm.addEventListener("submit", (e) => {
    e.preventDefault();
    let username = document.getElementById("username");
    let password = document.getElementById("password");

    console.log("Username:", username.value);
    console.log("Password:", password.value);
    // process and send to API
})