package com.leo.myapplication14.app;

import android.os.Parcel;
import android.os.Parcelable;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Apex implements Parcelable {
    int id;
    String idNews;
    String title;
    String photo;
    String content;
    String shortContent;
    String url;
    String created_at;
    String featured;
    String imagePath;

    public Apex() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdNews() {
        return idNews;
    }

    public void setIdNews(String idNews) {
        this.idNews = idNews;
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
        return "<html>" + content + "</html>";
    }

    public String getShortContent() {
        StringBuilder result = new StringBuilder("");
        result.append("<html><body><style>body{font-weight: bold;}</style>");
        int count = 0;
        for (int i = 0; i < content.length(); i++) {
            result.append(content.substring(i, i + 1));
            if (isCyrillic(content.charAt(i))) count++;
            if (count == 50) break;
        }
        result.append("...");
        result.append("</p></body></html>");
        return result.toString();
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
        String data = created_at.substring(0, 10);
        Locale russian = new Locale("ru");
        String[] newMonths = {
                "января", "февраля", "марта", "апреля", "мая", "июня",
                "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        DateFormatSymbols dfs = DateFormatSymbols.getInstance(russian);
        dfs.setMonths(newMonths);
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, russian);
        SimpleDateFormat sdf = (SimpleDateFormat) df;
        sdf.setDateFormatSymbols(dfs);

        try {
            Date jud = new SimpleDateFormat("yyyy-MM-dd").parse(data);
            created_at = sdf.format(jud);
        } catch (ParseException e) {
        }
        return created_at;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Apex{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(idNews);
        dest.writeString(title);
        dest.writeString(photo);
        dest.writeString(content);
        dest.writeString(shortContent);
        dest.writeString(url);
        dest.writeString(created_at);
        dest.writeString(featured);
        dest.writeString(imagePath);
    }
}