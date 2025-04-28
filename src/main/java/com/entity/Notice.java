package com.entity;

public class Notice {
    Integer id;
    String content;

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

    public Notice(Integer id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}
