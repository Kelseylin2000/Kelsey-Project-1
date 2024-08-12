const productListContainer = document.getElementById("products-container");

const urlParams = new URLSearchParams(window.location.search);

let category;
if (urlParams.has("category")) {
    category = urlParams.get("category");
} else {
    category = "all";
}
fetch(`/api/1.0/products/${category}`)
    .then((response) => response.json())
    .then((data) => {
        const products = data.data;
        if (products && products.length > 0) {
            products.forEach((product) => {
                // Create product item container
                const productItem = document.createElement("div");
                productItem.dataset.productId = product.id;
                productItem.classList.add("product-item");

                // Product image
                const productLink = document.createElement("a");
                productLink.href = `/product.html?id=${product.id}`;

                const productImage = document.createElement("img");
                productImage.src = product.main_image;
                productImage.alt = product.title;

                productLink.appendChild(productImage);
                productItem.appendChild(productLink);

                // Product name
                const productLink1 = document.createElement("a");
                productLink1.href = `/product.html?id=${product.id}`;

                const productName = document.createElement("div");
                productName.classList.add("product-name");
                productName.innerText = product.title;

                productLink1.appendChild(productName);
                productItem.appendChild(productLink1);

                // Product price
                const productPrice = document.createElement("div");
                productPrice.classList.add("product-price");
                productPrice.innerText = `TWD.${product.price}`;
                productItem.appendChild(productPrice);

                // Product color options
                const colorOptions = document.createElement("div");
                colorOptions.classList.add("color-options");
                product.colors.forEach((color) => {
                    const colorOption = document.createElement("div");
                    colorOption.classList.add("color-option");
                    colorOption.style.backgroundColor = color.code;
                    colorOptions.appendChild(colorOption);
                });
                productItem.appendChild(colorOptions);

                // Append product item to the container
                productListContainer.appendChild(productItem);
            });
        }
    })
    .catch((error) => console.error("Error fetching products:", error));
