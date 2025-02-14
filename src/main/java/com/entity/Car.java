package com.entity;

public class Car {
    private Integer id;
    private String cname;
    private Integer cprice;
    private Integer cnum;

    public Integer getId() {
        return id;
    }

    public String getCname() {
        return cname;
    }

    public Integer getCprice() {
        return cprice;
    }

    public Integer getCnum() {
        return cnum;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public void setCprice(Integer cprice) {
        this.cprice = cprice;
    }

    public void setCnum(Integer cnum) {
        this.cnum = cnum;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", cname='" + cname + '\'' +
                ", cprice=" + cprice +
                ", cnum=" + cnum +
                '}';
    }
}
