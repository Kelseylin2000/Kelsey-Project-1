window.fbAsyncInit = function () {
    FB.init({
        appId: "875247337790178",
        cookie: true,
        xfbml: true,
        version: "v20.0",
    });

    FB.AppEvents.logPageView();
};

(function (d, s, id) {
    var js,
        fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) {
        return;
    }
    js = d.createElement(s);
    js.id = id;
    js.src = "https://connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
})(document, "script", "facebook-jssdk");

function checkLoginState() {
    FB.getLoginStatus(function (response) {
        if (response.status === "connected") {
            // Get the access token
            var accessToken = response.authResponse.accessToken;

            console.log("FB accessToken", accessToken);

            // Send the access token to the backend
            fetch("/api/1.0/user/signin", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    provider: "facebook",
                    access_token: accessToken,
                }),
            })
                .then((response) => response.json())
                .then((data) => {
                    localStorage.setItem("accessToken", data.data.access_token);
                    showUserProfile(data.data.user);
                })
                .catch((error) => {
                    console.error(
                        "There has been a problem with fetch operation:",
                        error
                    );
                    document.getElementById("status").innerText =
                        "Login Failed";
                });
        } else {
            document.getElementById("status").innerText = "Login Failed";
        }
    });
}
