package com.entity;

public class Collect {
    Integer id;
    String username;
    String product;
    String image;
    Integer price;

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getProduct() {
        return product;
    }

    public Collect(Integer id, String username, String product, String image, Integer price) {
        this.id = id;
        this.username = username;
        this.product = product;
        this.image = image;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Collect{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", product='" + product + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                '}';
    }
}
