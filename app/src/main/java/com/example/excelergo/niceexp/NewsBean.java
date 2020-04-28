package com.example.excelergo.niceexp;

public class NewsBean {
    public NewsBean() {
    }

    public NewsBean(String title, String img, String url) {
        this.title = title;
        this.img = img;
        this.url = url;
    }

    private String title;
    private String img;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
