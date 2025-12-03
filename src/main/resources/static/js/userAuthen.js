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
    password: p1, // goes to transient field in UserInfo.java
  };

  try {
    const response = await fetch("http://localhost:8080/api/auth/signup", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(user),
    });

    const result = await response.text();
    alert(result);

    // direct user to login page if they successful created an account
    if (result === "SUCCESS") {
      window.location.href = "Login.html";
    }
  } catch (e) {
    console.error("Signup error:", e);
    alert("Unable to sign up. Server offline?");
  }
}

// Login frontend logic
async function loginUser() {
  // retrieve username and password to check with user credentials in the db that match
  const username = document.getElementById("usernameLogin").value;
  const password = document.getElementById("passwordLogin").value;

  const req = { username, password };

  // part the checks with db
  try {
    const response = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });

    const result = await response.text();

    if (result === "ERROR") {
      alert("Invalid username or password.");
      return;
    }

    // if the login is a success, then save userId to localStorage and send them to Dashboard.html
    localStorage.setItem("userId", result);
    window.location.href = "../jeopardyDash/Dashboard.html";
  } catch (e) {
    console.error("Login error:", e);
    alert("Unable to login. Server offline?");
  }
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
