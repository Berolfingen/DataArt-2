package com.leo.myapplication14.app;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Apex implements Serializable {
    int id;
    String title;
    String photo;
    String content;
    String shortContent;
    String url;
    String created_at;
    String featured;
    Bitmap image;

    public Apex() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getContent(){
        return "<html>"+content+"</html>";
    }

    public String getShortContent() {
        StringBuilder result = new StringBuilder("");
        result.append("<html><body><style>body{font-weight: bold;}</style>");
        int count=0;
        for(int i=0;i<content.length();i++){
           result.append(content.substring(i,i+1));
           if(isCyrillic(content.charAt(i))) count++;
           if(count==50) break;
        }
        result.append("...");
        result.append("</p></body></html>");
        return  result.toString();
    }

    public void setShortContent(String shortContent) {
        this.shortContent = shortContent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreated_at() {
        String data=created_at.substring(0,10);

        return data;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    private boolean isCyrillic(char c) {
        return Character.UnicodeBlock.CYRILLIC.equals(Character.UnicodeBlock.of(c));
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
