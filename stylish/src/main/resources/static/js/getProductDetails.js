let selectedColor = null;
let selectedSize = null;
let maxQuantity = 0;
let CheckoutFormExisted = false;

document.addEventListener("DOMContentLoaded", () => {
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get("id");

    if (productId) {
        fetch(`/api/1.0/products/details?id=${productId}`)
            .then((response) => response.json())
            .then((data) => {
                const product = data.data;

                if (!product) {
                    console.error("No product data found");
                    return;
                }

                const productDetailsContainer =
                    document.getElementById("product-details");
                const imagesHtml = product.images
                    .map((image) => `<img src="${image}" alt="Product Image">`)
                    .join("");

                const productHtml = `
                    <div class="product-main-session">
                        <div class="product-image">
                            <img src="${product.main_image}" alt="${
                    product.title
                }">
                        </div>
                        <div class="product-info">
                            <h1>${product.title}</h1>
                            <p>${product.id}</p>
                            <p class="product-price">TWD.${product.price}</p>
                            <hr>
                            <div class="color-options">
                                <span>顏色 | </span>
                                ${
                                    product.colors && product.colors.length > 0
                                        ? product.colors
                                              .map(
                                                  (color) => `
                                    <div style="background-color: ${color.code}" class="color-option" data-color="${color.code}"></div>
                                `
                                              )
                                              .join("")
                                        : "<p>無顏色選項</p>"
                                }
                            </div>
                            <div class="size-options">
                                <span>尺寸 | </span>
                                ${
                                    product.sizes && product.sizes.length > 0
                                        ? product.sizes
                                              .map(
                                                  (size) => `
                                    <div class="size-option" data-size="${size}">${size}</div>
                                `
                                              )
                                              .join("")
                                        : "<p>無尺寸選項</p>"
                                }
                            </div>
                            <div class="quantity-container">
                                <span>數量 |</span>
                                <div class="quantity-selector">
                                    <button id="decrease-quantity" class="quantity-btn">-</button>
                                    <span id="quantity" class="quantity-number">1</span>
                                    <button id="increase-quantity" class="quantity-btn">+</button>
                                </div>
                            </div>
                            <a href="#checkout-container"><button class="add-to-cart" disabled>立刻下訂</button></a>
                            <p>請先選擇顏色、尺寸、數量再按下訂按鈕</p>
                            <p class="color-warn">實品顏色依單品照為主</p>
                            <p>材質: ${product.texture}</p>
                            <p>洗滌方式: ${product.wash}</p>
                            <p>產地: ${product.place}</p>
                            <p>備註: ${product.note}</p>
                        </div>
                    </div>
                    <div id="checkout-container"></div>
                    <div class="product-info-section">
                        <h2>更多產品資訊</h2>
                        <div class="product-description">
                            <p>${product.description}</p>
                        </div>
                        <div class="product-image">
                            ${imagesHtml}
                        </div>
                    </div>
                `;

                productDetailsContainer.innerHTML = productHtml;

                let quantity = 1;
                const addToCartButton = document.querySelector(".add-to-cart");

                const updateQuantity = () => {
                    const quantityElement = document.getElementById("quantity");
                    if (quantity > maxQuantity) {
                        quantity = maxQuantity;
                    }
                    quantityElement.textContent = quantity;
                    updateAddToCartButton();
                };

                const updateAddToCartButton = () => {
                    if (selectedColor && selectedSize && quantity > 0) {
                        addToCartButton.disabled = false;
                        addToCartButton.style.opacity = "1";
                    } else {
                        addToCartButton.disabled = true;
                        addToCartButton.style.opacity = "0.2";
                    }
                };

                document
                    .getElementById("decrease-quantity")
                    .addEventListener("click", () => {
                        if (quantity > 1) {
                            quantity--;
                            updateQuantity();
                        }
                    });

                document
                    .getElementById("increase-quantity")
                    .addEventListener("click", () => {
                        quantity++;
                        updateQuantity();
                    });

                const colorOptions = document.querySelectorAll(".color-option");
                const sizeOptions = document.querySelectorAll(".size-option");

                colorOptions.forEach((option) => {
                    option.addEventListener("click", () => {
                        colorOptions.forEach((o) =>
                            o.classList.remove("selected")
                        );
                        option.classList.add("selected");
                        selectedColor = option.getAttribute("data-color");
                        updateSizeOptions();
                        updateMaxQuantity();
                        updateAddToCartButton();
                    });
                });

                sizeOptions.forEach((option) => {
                    option.addEventListener("click", () => {
                        if (option.classList.contains("disabled")) return;
                        sizeOptions.forEach((o) =>
                            o.classList.remove("selected")
                        );
                        option.classList.add("selected");
                        selectedSize = option.getAttribute("data-size");
                        updateMaxQuantity();
                        updateAddToCartButton();
                    });
                });

                const updateMaxQuantity = () => {
                    if (selectedColor && selectedSize) {
                        const selectedVariant = product.variants.find(
                            (variant) =>
                                variant.size === selectedSize &&
                                variant.color_code === selectedColor
                        );
                        if (selectedVariant) {
                            maxQuantity = selectedVariant.stock;
                            updateQuantity();
                        } else {
                            maxQuantity = 0;
                        }
                    } else {
                        maxQuantity = 0;
                    }
                    updateAddToCartButton();
                };

                const updateSizeOptions = () => {
                    sizeOptions.forEach((option) => {
                        const size = option.getAttribute("data-size");
                        const variant = product.variants.find(
                            (v) =>
                                v.size === size &&
                                v.color_code === selectedColor
                        );

                        if (variant && variant.stock > 0) {
                            option.classList.remove("disabled");
                            option.style.opacity = "1";
                            option.style.pointerEvents = "auto";
                        } else {
                            option.classList.add("disabled");
                            option.style.opacity = "0.5";
                            option.style.pointerEvents = "none";
                        }
                    });
                };

                // checkout
                addToCartButton.addEventListener("click", () => {
                    // check if token exists and vaild, then show checkout form. Otherwise, lead to /profile.html
                    const token = localStorage.getItem("accessToken");
                    if (token) {
                        fetch("/api/1.0/user/profile", {
                            headers: {
                                Authorization: `Bearer ${token}`,
                            },
                        })
                            .then((response) => response.json())
                            .then((data) => {
                                if (data.data) {
                                    if (!CheckoutFormExisted) {
                                        showCheckoutForm();
                                    }
                                } else {
                                    window.location.href = "/profile.html";
                                }
                            })
                            .catch((error) => {
                                alert("請重新登入");
                                window.location.href = "/profile.html";
                            });
                    } else {
                        window.location.href = "/profile.html";
                    }
                });

                const showCheckoutForm = () => {
                    CheckoutFormExisted = true;

                    const checkoutContainer =
                        document.getElementById("checkout-container");
                    checkoutContainer.innerHTML = `
                        <h2>信用卡資訊</h2>
                        <div class="tpfield" id="card-number"></div>
                        <div class="tpfield" id="card-expiration-date"></div>
                        <div class="tpfield" id="card-ccv"></div>
                        <button disabled id="submit-button" onclick="onSubmit()">Submit</button>
                        <div id="card-errors"></div>
                    `;

                    const script = document.createElement("script");
                    script.src = "/js/tapPay.js";
                    script.onload = function () {
                        console.log("tapPay.js 加載完成");
                    };
                    document.body.appendChild(script);
                };
            })
            .catch((error) =>
                console.error("Error fetching product details:", error)
            );
    } else {
        console.error("No product ID found in URL");
    }
});
