package com.entity;

public class Product {
    private Integer id;
    private  String proname;
    private Integer num;
    private Double price;
    private String category;
    private String description;
    private String pro_image;
    private Integer star;

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getPro_image() {
        return pro_image;
    }

    public void setPro_image(String pro_image) {
        this.pro_image = pro_image;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    public String getProname() {
        return proname;
    }

    public Integer getNum() {
        return num;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }

    public void setNum(Integer num) {
        this.num = num;
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", proname='" + proname + '\'' +
                ", num=" + num +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", pro_image='" + pro_image + '\'' +
                ", star=" + star +
                '}';
    }
}
