package com.example.stylish.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductDto {
    private int id;
    private String category;
    private String title;
    private String description;
    private double price;
    private String texture;
    private String wash;
    private String place;
    private String note;
    private String story;
    private List<ColorDto> colors;
    private List<String> sizes;
    private List<VariantDto> variants;
    @JsonProperty("main_image")
    private String mainImage;
    private List<String> images;

    // Default constructor
    public ProductDto() {}

    // Parameterized constructor
    public ProductDto(int id, String category, String title, String description, double price, String texture, String wash, String place, String note, String story, List<ColorDto> colors, List<String> sizes, List<VariantDto> variants, String mainImage, List<String> images) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.description = description;
        this.price = price;
        this.texture = texture;
        this.wash = wash;
        this.place = place;
        this.note = note;
        this.story = story;
        this.colors = colors;
        this.sizes = sizes;
        this.variants = variants;
        this.mainImage = mainImage;
        this.images = images;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getWash() {
        return wash;
    }

    public void setWash(String wash) {
        this.wash = wash;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public List<ColorDto> getColors() {
        return colors;
    }

    public void setColors(List<ColorDto> colors) {
        this.colors = colors;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public List<VariantDto> getVariants() {
        return variants;
    }

    public void setVariants(List<VariantDto> variants) {
        this.variants = variants;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}