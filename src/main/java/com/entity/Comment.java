package com.entity;

public class Comment {
    Integer cid;
    String cproduct;
    String comment;
    String username;

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public void setCproduct(String cproduct) {
        this.cproduct = cproduct;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getCid() {
        return cid;
    }

    public String getCproduct() {
        return cproduct;
    }

    public String getComment() {
        return comment;
    }

    public String getUsername() {
        return username;
    }

    public Comment(Integer cid, String cproduct, String comment, String username) {
        this.cid = cid;
        this.cproduct = cproduct;
        this.comment = comment;
        this.username = username;
    }
}
