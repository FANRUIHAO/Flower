package com.entity;

public class Complaint {
    private Integer id;
    private String content;
    public Complaint() {}

    public Integer getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Complaint(Integer id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Complaint{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}
