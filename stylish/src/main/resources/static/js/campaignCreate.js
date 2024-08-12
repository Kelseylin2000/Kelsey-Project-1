document.addEventListener("DOMContentLoaded", function () {
    const productSelect = document.getElementById("productSelect");

    // Function to fetch product data
    const fetchProducts = (url) => {
        fetch(url)
            .then((response) => response.json())
            .then((data) => {
                const products = data.data;

                products.forEach((product) => {
                    const option = document.createElement("option");
                    option.value = product.id;
                    option.textContent = `${product.id} - ${product.title}`;
                    productSelect.appendChild(option);
                });

                if (data.next_paging) {
                    fetchProducts(
                        `/api/1.0/products/all?paging=${data.next_paging}`
                    );
                }
            })
            .catch((error) => console.error("Error fetching products:", error));
    };

    // Initial fetch call
    fetchProducts("/api/1.0/products/all");
});
