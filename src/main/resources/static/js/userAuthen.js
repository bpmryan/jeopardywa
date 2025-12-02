// Signup.html frontend logic
async function signupUser() {
  const fName = document.getElementById("fName").value;
  const lName = document.getElementById("lName").value;
  const email = document.getElementById("email").value;
  const username = document.getElementById("username").value;
  const p1 = document.getElementById("firstPassword").value;
  const p2 = document.getElementById("finalPassword").value;

  // checks if the first password (p1) and the final password (p2) match before signing up
  if (p1 !== p2) {
    alert("Passwords do not match!");
    return;
  }

  // password is then saved as p1 (first password)
  // sent to db
  const user = {
    fName,
    lName,
    email,
    username,
    password: p1,
  };

  const res = await fetch("/api/user/signup", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(user),
  });

  const txt = await res.text();

  // direct user to login page if they successful created an account
  if (txt === "SUCCESS") {
    alert("Account created! Please login.");
    window.location.href = "../userAuthen/Login.html";
  } else {
    alert(txt);
  }
}

// Login frontend logic
async function loginUser() {
  // retrieve username and password to check with user credentials in the db that match
  const username = document.getElementById("usernameLogin").value;
  const password = document.getElementById("passwordLogin").value;

  const req = { username, password };

  // part the checks with db
  const res = await fetch("/api/user/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(req),
  });

  const userId = await res.text();

  if (userId === "ERROR") {
    alert("Invalid username or password.");
    return;
  }

  // if the login is a success, then save userId to localStorage and send them to Dashboard.html
  localStorage.setItem("userId", userId);
  window.location.href = "../jeopardyDash/Dashboard.html";
}

// auto direct if logged in 
function requireLogin() {
    if (!localStorage.getItem("userId")) {
        window.location.href = "../userAuthen/Login.html";
    }
}

// user is sent back to index.html once they logout
// userId is removed from localStorage once this happens
function logoutUser() {
    localStorage.removeItem("userId");
    window.location.href = "../index.html";
}