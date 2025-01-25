package com.entity;

public class Product {
    private Integer id;
    private  String proname;
    private Integer num;

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
                '}';
    }
}
