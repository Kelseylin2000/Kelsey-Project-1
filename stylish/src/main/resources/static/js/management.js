let variantCount = 0;
const variantDivContainer = document.getElementById("variant_div_container");

function addVariant() {
    variantCount++;
    let variantDiv = `
        <div class="variant_div" id="variant_div_${variantCount}">
            <label for="variants[${variantCount}].colorCode">Color Code:</label>
            <input type="text" id="variants[${variantCount}].colorCode" name="variants[${variantCount}].colorCode" />

            <label for="variants[${variantCount}].size">Size:</label>
            <input type="text" id="variants[${variantCount}].size" name="variants[${variantCount}].size" />

            <label for="variants[${variantCount}].stock">Stock Quantity:</label>
            <input type="number" id="variants[${variantCount}].stock" name="variants[${variantCount}].stock" />
        </div>
    `;
    variantDivContainer.insertAdjacentHTML("beforeend", variantDiv);
}

let colorCount = 0;
const colorDivContainer = document.getElementById("color_div_container");

function addColor() {
    colorCount++;
    let colorDiv = `
        <div class="color_div" id="color_div_${colorCount}">
            <label for="colors[${colorCount}].code">Color Code:</label>
            <input type="text" id="colors[${colorCount}].code" name="colors[${colorCount}].code" required />

            <label for="colors[${colorCount}].name">Color Name:</label>
            <input type="text" id="colors[${colorCount}].name" name="colors[${colorCount}].name" required />
        </div>
    `;
    colorDivContainer.insertAdjacentHTML("beforeend", colorDiv);
}
