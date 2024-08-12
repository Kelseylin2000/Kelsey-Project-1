document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("accessToken");

    if (token) {
        fetchUserProfile(token);
    } else {
        showSignInUpForms();
    }
});

function fetchUserProfile(token) {
    fetch("/api/1.0/user/profile", {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    })
        .then((response) => response.json())
        .then((data) => {
            if (data.data) {
                showUserProfile(data.data);
            } else {
                showSignInUpForms();
            }
        })
        .catch((error) => {
            alert("請重新登入");
            showSignInUpForms();
        });
}

function showUserProfile(user) {
    document.getElementById("profile-container").innerHTML = `
        <div>
            <h1>歡迎，${user.name}</h1>
            <p>Email: ${user.email}</p>
        </div>
    `;
}

function showSignInUpForms() {
    const script = document.createElement("script");
    script.src = "/js/facebookSignin.js";
    script.onload = function () {
        document.getElementById("profile-container").innerHTML = `
            <form id="sign-in-form">
                <h2>登入</h2>
                <input type="email" id="sign-in-email" placeholder="Email">
                <input type="password" id="sign-in-password" placeholder="Password">
                <button type="submit">登入</button>
                <fb:login-button
                scope="public_profile,email"
                onlogin="checkLoginState();"
                >
                Facebook Login
                </fb:login-button>
            </form>
            <form id="sign-up-form">
                <h2>註冊</h2>
                <input type="text" id="sign-up-name" placeholder="Name">
                <input type="email" id="sign-up-email" placeholder="Email">
                <input type="password" id="sign-up-password" placeholder="Password">
                <button type="submit">註冊</button>
            </form>
        `;

        document
            .getElementById("sign-in-form")
            .addEventListener("submit", handleSignIn);
        document
            .getElementById("sign-up-form")
            .addEventListener("submit", handleSignUp);

        // After ensuring that the Facebook SDK has been loaded, display the login button
        FB.XFBML.parse(document.getElementById("profile-container"));
    };
    document.body.appendChild(script);
}
