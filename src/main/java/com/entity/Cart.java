package com.entity;

public class Cart {
    private Integer id;
    private String cname;
    private Double cprice;
    private Integer cnum;
    private Integer user_id;
    private  String image_url;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getId() {
        return id;
    }

    public String getCname() {
        return cname;
    }

    public Double getCprice() {
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

    public void setCprice(Double cprice) {
        this.cprice = cprice;
    }

    public void setCnum(Integer cnum) {
        this.cnum = cnum;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", cname='" + cname + '\'' +
                ", cprice=" + cprice +
                ", cnum=" + cnum +
                ", user_id=" + user_id +
                ", image_url='" + image_url + '\'' +
                '}';
    }


}
