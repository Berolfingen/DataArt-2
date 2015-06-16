package com.leo.myapplication14.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ApexSqlliteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 30;
    private static final String DATABASE_NAME = "ApexDB";

    public ApexSqlliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_APEX_TABLE = "CREATE TABLE apexNews(_id INTEGER PRIMARY KEY, idNews TEXT unique, title TEXT, content TEXT, shortContent Text, featured TEXT, created_at DATETIME, photopath TEXT, url TEXT);";


        db.execSQL(CREATE_APEX_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS apexNews");
        onCreate(db);
    }

    // Books table name
    private static final String TABLE_ApexNews = "apexNews";
    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ID_NEWS = "idNews";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_SHORT_CONTENT = "shortContent";
    private static final String KEY_FEATURED = "featured";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_PHOTOPATH = "photopath";
    private static final String KEY_URL = "url";

    private static final String[] COLUMNS = {KEY_ID, KEY_ID_NEWS, KEY_TITLE, KEY_CONTENT, KEY_SHORT_CONTENT, KEY_FEATURED, KEY_CREATED_AT, KEY_PHOTOPATH, KEY_URL};

    public void addApex(Apex apex) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, apex.getTitle());
        values.put(KEY_ID_NEWS, apex.getIdNews());
        values.put(KEY_CONTENT, apex.getContent());
        values.put(KEY_SHORT_CONTENT, apex.getShortContent());
        values.put(KEY_FEATURED, apex.getFeatured());
        values.put(KEY_CREATED_AT, apex.getCreated_at());
        values.put(KEY_PHOTOPATH, apex.getImagePath());
        values.put(KEY_URL, apex.getUrl());

        db.insert(TABLE_ApexNews, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        db.close();
    }


    public Apex getApex(int id) {
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        // 2. build query
        Cursor cursor =
                db.query(TABLE_ApexNews, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        Apex apex = new Apex();
        apex.setId(Integer.parseInt(cursor.getString(0)));
        apex.setIdNews(cursor.getString(1));
        apex.setTitle(cursor.getString(2));
        apex.setContent(cursor.getString(3));
        apex.setShortContent(cursor.getString(4));
        apex.setFeatured(cursor.getString(5));
        apex.setCreated_at(cursor.getString(6));
        apex.setImagePath(cursor.getString(7));
        apex.setUrl(cursor.getString(8));
        cursor.close();
        db.close();
        return apex;

    }

    public int updateApex(Apex apex) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ID_NEWS, apex.getIdNews());
        values.put(KEY_TITLE, apex.getTitle()); // get title
        values.put(KEY_CONTENT, apex.getContent());
        values.put(KEY_SHORT_CONTENT, apex.getShortContent());
        values.put(KEY_FEATURED, apex.getFeatured());
        values.put(KEY_CREATED_AT, apex.getCreated_at());
        values.put(KEY_PHOTOPATH, apex.getImagePath());
        values.put(KEY_URL, apex.getUrl());
        // 3. updating row
        int i = db.update(TABLE_ApexNews, //table
                values, // column/value
                KEY_ID + " = ?", // selections
                new String[]{String.valueOf(apex.getId())}); //selection args

        // 4. close
        db.close();
        return i;
    }

    public void deleteApex(Apex apex) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_ApexNews, //table name
                KEY_ID + " = ?",  // selections
                new String[]{String.valueOf(apex.getId())}); //selections args
        // 3. close
        db.close();
    }

    public List<Apex> getAllApex() {
        List<Apex> apexes = new ArrayList<Apex>();

        String query = "SELECT  * FROM " + TABLE_ApexNews+"ORDER BY "+KEY_CREATED_AT+" DESC;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build apex and add it to list
        Apex apex = null;
        if (cursor.moveToFirst()) {
            do {
                apex = new Apex();
                apex.setId(Integer.parseInt(cursor.getString(0)));
                apex.setIdNews(cursor.getString(1));
                apex.setTitle(cursor.getString(2));
                apex.setContent(cursor.getString(3));
                apex.setShortContent(cursor.getString(4));
                apex.setFeatured(cursor.getString(5));
                apex.setCreated_at(cursor.getString(6));
                apex.setImagePath(cursor.getString(7));
                apex.setUrl(cursor.getString(8));

                apexes.add(apex);
                Log.d("getAllApex()", apex.toString());
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return apexes;
    }

    public ArrayList<String> getAllNewsId() {
        ArrayList<String> newsId = new ArrayList<>();
        String query = "SELECT idNews FROM " + TABLE_ApexNews;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        String str = "";
        if (cursor.moveToFirst()) {
            do {
                str = (cursor.getString(0));
                newsId.add(str);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return newsId;

    }


    public ArrayList<Apex> getNotFetchedNews() {
        ArrayList<Apex> apexes = new ArrayList<Apex>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_ApexNews + " WHERE featured = 'false' ORDER BY "+KEY_CREATED_AT+" DESC;";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Apex apex = null;
        if (cursor.moveToFirst()) {
            do {
                apex = new Apex();
                apex.setId(Integer.parseInt(cursor.getString(0)));
                apex.setIdNews(cursor.getString(1));
                apex.setTitle(cursor.getString(2));
                apex.setContent(cursor.getString(3));
                apex.setShortContent(cursor.getString(4));
                apex.setFeatured(cursor.getString(5));
                apex.setCreated_at(cursor.getString(6));
                apex.setImagePath(cursor.getString(7));
                apex.setUrl(cursor.getString(8));

                // Add book to books
                apexes.add(apex);
                Log.d("getAllApex()", apex.toString());
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return apexes;
    }

    public ArrayList<Apex> getFetchedNews() {
        ArrayList<Apex> apexes = new ArrayList<Apex>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_ApexNews + " WHERE featured = 'true' ORDER BY "+KEY_CREATED_AT+" DESC;";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Apex apex = null;
        if (cursor.moveToFirst()) {
            do {
                apex = new Apex();
                apex.setId(Integer.parseInt(cursor.getString(0)));
                apex.setIdNews(cursor.getString(1));
                apex.setTitle(cursor.getString(2));
                apex.setContent(cursor.getString(3));
                apex.setShortContent(cursor.getString(4));
                apex.setFeatured(cursor.getString(5));
                apex.setCreated_at(cursor.getString(6));
                apex.setImagePath(cursor.getString(7));
                apex.setUrl(cursor.getString(8));

                // Add book to books
                apexes.add(apex);
                Log.d("getAllApex()", apex.toString());
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return apexes;
    }

    public void checkOnUpdates(ArrayList<String> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        StringBuilder inQuery = new StringBuilder();
        //ArrayList<String> item = new ArrayList<>();
        /*for (int i = 0; i < list.size(); i++) {
            item.add(list.get(i).getIdNews());
        }*/
        inQuery.append("(");
        boolean first = true;
        for (String IdNews : list) {
            if (first) {
                first = false;
                inQuery.append("'").append(IdNews).append("'");
            } else {
                inQuery.append(", '").append(IdNews).append("'");
            }
        }
        inQuery.append(")");

        db.delete(TABLE_ApexNews, KEY_ID_NEWS + " NOT IN " + inQuery.toString(), null);

        db.close();
    }
}

