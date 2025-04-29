package com.entity;

public class Service {
    private Integer id;
    private String phone;

    public Integer getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Service(){}

    public Service(Integer id, String phone) {
        this.id = id;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                '}';
    }
}
