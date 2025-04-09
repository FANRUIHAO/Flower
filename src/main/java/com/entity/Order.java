package com.entity;

import com.fasterxml.jackson.databind.ser.std.StdArraySerializers;

public class Order {
    private int id;
    private Integer user_id;
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

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", addr='" + addr + '\'' +
                ", product='" + product + '\'' +
                ", num=" + num +
                ", image='" + image + '\'' +
                ", status='" + status + '\'' +
                ", sum=" + sum +
                ", ordertime='" + ordertime + '\'' +
                ", phone=" + phone +
                '}';
    }
}
