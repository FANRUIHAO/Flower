package com.entity;

import com.fasterxml.jackson.databind.ser.std.StdArraySerializers;

public class Order {
    private int id;
    private String username;
    private String addr;
    private String product;
    private Integer num;
    private String image;
    private String status;
    private Double sum;
    private String ordertime;
    private String phone;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAddr() {
        return addr;
    }

    public String getProduct() {
        return product;
    }

    public Integer getNum() {
        return num;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public Double getSum() {
        return sum;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public String getPhone() {
        return phone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", addr='" + addr + '\'' +
                ", product='" + product + '\'' +
                ", num=" + num +
                ", image='" + image + '\'' +
                ", status='" + status + '\'' +
                ", sum=" + sum +
                ", ordertime='" + ordertime + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

}
