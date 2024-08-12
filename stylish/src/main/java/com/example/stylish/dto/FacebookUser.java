package com.example.stylish.dto;

public class FacebookUser {
    private String id;
    private String name;
    private String email;
    private Picture picture;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    // Picture nested class
    public static class Picture {
        private Data data;

        // Getters and Setters
        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        // Data nested class
        public static class Data {
            private String url;
            private int height;
            private boolean isSilhouette;
            private int width;

            // Getters and Setters
            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public boolean isSilhouette() {
                return isSilhouette;
            }

            public void setSilhouette(boolean isSilhouette) {
                this.isSilhouette = isSilhouette;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }
        }
    }
}
