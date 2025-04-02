package com.entity;

public class Collect {
    Integer id;
    String username;
    String product;

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

    public Collect(Integer id, String username, String product) {
        this.id = id;
        this.username = username;
        this.product = product;
    }
}
