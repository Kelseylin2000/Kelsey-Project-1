function handleSignIn(event) {
    event.preventDefault();
    const provider = "native";
    const email = document.getElementById("sign-in-email").value;
    const password = document.getElementById("sign-in-password").value;

    fetch("/api/1.0/user/signin", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ provider, email, password }),
    })
        .then((response) => response.json())
        .then((data) => {
            if (data.data) {
                localStorage.setItem("accessToken", data.data.access_token);
                fetchUserProfile(data.data.access_token);
            } else {
                alert(data.error);
            }
        })
        .catch((error) => console.error("Error signing in:", error));
}

function handleSignUp(event) {
    event.preventDefault();
    const name = document.getElementById("sign-up-name").value;
    const email = document.getElementById("sign-up-email").value;
    const password = document.getElementById("sign-up-password").value;

    fetch("/api/1.0/user/signup", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ name, email, password }),
    })
        .then((response) => response.json())
        .then((data) => {
            if (data.data) {
                localStorage.setItem("accessToken", data.data.access_token);
                fetchUserProfile(data.data.access_token);
            } else {
                alert(data.error);
            }
        })
        .catch((error) => console.error("Error signing up:", error));
}
