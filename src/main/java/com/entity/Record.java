package com.entity;

public class Record {
    private Integer id;
    private Integer userId;
    private String time;
    private Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Record() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getTime() {
        return time;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Record(Integer id, Integer userId, String time, Double price) {
        this.id = id;
        this.userId = userId;
        this.time = time;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", userId=" + userId +
                ", time='" + time + '\'' +
                ", price=" + price +
                '}';
    }
}
