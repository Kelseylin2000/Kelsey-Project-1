const APP_ID = 12348;
const APP_KEY =
    "app_pa1pQcKoY22IlnSXq5m5WP5jFKzoRG58VEXpT7wU62ud7mMbDOGzCYIlzzLF";
TPDirect.setupSDK(APP_ID, APP_KEY, "sandbox");

TPDirect.card.setup({
    fields: {
        number: {
            element: "#card-number",
            placeholder: "**** **** **** ****",
        },
        expirationDate: {
            element: "#card-expiration-date",
            placeholder: "MM / YY",
        },
        // Display ccv field
        ccv: {
            element: "#card-ccv",
            placeholder: "CVV",
        },
    },
    styles: {
        // Style all elements
        input: {
            color: "gray",
        },
        // Styling ccv field
        "input.ccv": {
            "font-size": "16px",
        },
        // Styling expiration-date field
        "input.expiration-date": {
            "font-size": "16px",
        },
        // Styling card-number field
        "input.card-number": {
            "font-size": "16px",
        },
        // style focus state
        ":focus": {
            color: "black",
        },
        // style valid state
        ".valid": {
            color: "green",
        },
        // style invalid state
        ".invalid": {
            color: "red",
        },
        // Media queries
        // Note that these apply to the iframe, not the root window.
        "@media screen and (max-width: 400px)": {
            input: {
                color: "orange",
            },
        },
    },
    // This setting will display the first six and last four digits of the credit card number after the card number is entered correctly.    isMaskCreditCardNumber: true,
    maskCreditCardNumberRange: {
        beginIndex: 6,
        endIndex: 11,
    },
});

TPDirect.card.onUpdate(function (update) {
    const submitButton = document.getElementById("submit-button");

    if (update.canGetPrime) {
        submitButton.removeAttribute("disabled");
    } else {
        submitButton.setAttribute("disabled", true);
    }

    const errorsElement = document.getElementById("card-errors");
    errorsElement.textContent = "";

    if (update.status.number === 2) {
        errorsElement.textContent += "Invalid card number. ";
    } else if (update.status.number === 0) {
        errorsElement.textContent += "Valid card number. ";
    }

    if (update.status.expiry === 2) {
        errorsElement.textContent += "Invalid expiration date. ";
    } else if (update.status.expiry === 0) {
        errorsElement.textContent += "Valid expiration date. ";
    }

    if (update.status.ccv === 2) {
        errorsElement.textContent += "Invalid CCV. ";
    } else if (update.status.ccv === 0) {
        errorsElement.textContent += "Valid CCV. ";
    }

    if (update.canGetPrime) {
        console.log("You can get prime now.");
    } else {
        console.log("Cannot get prime yet.");
    }
});

function onSubmit(event) {
    TPDirect.card.getPrime((result) => {
        if (result.status !== 0) {
            console.log("get prime error");
            return;
        }

        console.log("successfully get prime, prime: " + result.card.prime);

        const token = localStorage.getItem("accessToken");

        fetch("/api/1.0/order/checkout", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({
                prime: result.card.prime,
                order: {
                    shipping: "delivery",
                    payment: "credit_card",
                    subtotal: 300,
                    freight: 14,
                    total: 300,
                    recipient: {
                        name: "Luke",
                        phone: "0987654321",
                        email: "luke@gmail.com",
                        address: "市政府站",
                        time: "morning",
                    },
                    list: [
                        {
                            id: "1",
                            name: "夏日的粉紅洋裝",
                            price: 300,
                            color: {
                                code: "#FFBCB4",
                                name: "粉色",
                            },
                            size: "L",
                            qty: 1,
                        },
                    ],
                },
            }),
        })
            .then((response) => response.json())
            .then((data) => {
                if (data.data) {
                    window.location.href = "/thankyou.html";
                } else {
                    alert("Error during checkout:", data.error);
                }
            })
            .catch((error) => {
                console.error("Fetch error:", error);
            });
    });
}
