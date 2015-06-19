package com.leo.myapplication14.app.gallery;

import android.os.Parcel;
import android.os.Parcelable;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GalleryImage implements Parcelable {
    int id;
    String imagePath;
    String title;
    String content;
    String created_at;
    String url;
    String updated_at;
    String main;

    public GalleryImage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public String getShortTitle() {
        String result = "";
        if (title.length() < 20) {
            result = title;
            return result;
        } else {
            result = title.substring(0, 20);
            return result;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_atFormatted() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(imagePath);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(created_at);
        dest.writeString(url);
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }
}