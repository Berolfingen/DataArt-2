package com.leo.myapplication14.app;

public class Apex {
    String title;
    String photo;
    String content;
    String url;

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
        result.append("<html>");
        int count=0;
        for(int i=0;i<content.length();i++){
           result.append(content.substring(i,i+1));
           if(isCyrillic(content.charAt(i))) count++;
           if(count==50) break;
        }
        result.append("...");
        result.append("</html>");
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

}
