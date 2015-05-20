package com.leo.myapplication14.app;

public class Apex {
    String id;
    String title;
    String photo;
    String content;

    public Apex() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getContent() {
        return "<html>"+content+"</html>";
    }

    public void setContent(String content) {
        this.content = content;
    }
}
