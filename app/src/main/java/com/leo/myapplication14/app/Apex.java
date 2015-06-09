package com.leo.myapplication14.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Apex {
    String title;
    String photo;
    String content;
    String url;
    String created_at;
    public Apex() {
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

    private boolean isCyrillic(char c) {
        return Character.UnicodeBlock.CYRILLIC.equals(Character.UnicodeBlock.of(c));
    }

    public void setContent(String content) {
        this.content = content;
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
}
