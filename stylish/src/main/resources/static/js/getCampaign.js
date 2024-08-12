campaignContainer = document.getElementById("campaign-container");

fetch("/api/1.0/marketing/campaigns")
    .then((response) => response.json())
    .then((data) => {
        const campaigns = data.data;
        if (campaigns && campaigns.length > 0) {
            const firstCampaign = campaigns[0];

            const imgElement = document.createElement("img");
            imgElement.src = firstCampaign.picture;
            imgElement.alt = "Campaign Image";
            imgElement.classList.add("campaign-image");
            imgElement.dataset.productId = firstCampaign.product_id;

            const storyElement = document.createElement("div");
            storyElement.classList.add("campaign-story");
            storyElement.innerText = firstCampaign.story;

            campaignContainer.appendChild(imgElement);
            campaignContainer.appendChild(storyElement);
        }
    });

campaignContainer.addEventListener("click", (event) => {
    const target = event.target;
    if (target.classList.contains("campaign-image")) {
        const productId = target.dataset.productId;
        window.location.href = `/product.html?id=${productId}`;
    }
});
