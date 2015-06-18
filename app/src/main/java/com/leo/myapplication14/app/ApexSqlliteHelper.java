package com.leo.myapplication14.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;

public class ApexSqlliteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 35;
    private static final String DATABASE_NAME = "ApexDB";

    public ApexSqlliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_APEX_TABLE = "CREATE TABLE apexNews(_id INTEGER PRIMARY KEY, idNews TEXT unique," +
                " title TEXT, content TEXT, shortContent Text, featured TEXT, " +
                "created_at DATETIME, updated_at DATETIME, photopath TEXT, url TEXT, main TEXT);";

        db.execSQL(CREATE_APEX_TABLE);

        String CREATE_OPTIONS_TABLE = "CREATE TABLE options(_id INTEGER PRIMARY KEY, dbQuan INTEGER DEFAULT 50);";
        db.execSQL(CREATE_OPTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS apexNews");
        db.execSQL("DROP TABLE IF EXISTS options");
        onCreate(db);
    }

    private static final String TABLE_OPTIONS = "options";
    private static final String KEY_ID1 = "_id";
    private static final String KEY_QUAN = "dbQuan";
    private static final String[] COLUMNS1 = {KEY_ID1, KEY_QUAN};

    public int getQuan() {
        int result= 0;

        String query = "SELECT  dbQuan FROM " + TABLE_OPTIONS + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {

                result = Integer.parseInt(cursor.getString(0));

        }
        cursor.close();
        db.close();
        return result;
    }

    public int updateQuan(int quan) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUAN, quan);
        int i = db.update(TABLE_OPTIONS, values, KEY_ID1 + " =1",null);
        db.close();
        Log.d("q","From update method "+i);
        return i;
    }

    public ArrayList<String> getQ() {
        SQLiteDatabase mDataBase;
        mDataBase = getReadableDatabase();
        ArrayList<String> list = new ArrayList<>();
        Cursor  cursor = mDataBase.rawQuery("select * from options",null);

        if(cursor.moveToFirst()){            do{

                String column1 = cursor.getString(0);
                list.add(column1);
                String column2 = cursor.getString(1);
                list.add(column2);

            }while(cursor.moveToNext());
        }
        cursor.close();
        mDataBase.close();
        Log.d("q","What is in "+list.toString());
        Log.d("q","SIZE "+list.size());
        return list;
    }

    public void addQ(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_QUAN,50);
        Log.d("q","add");
        db.insert(TABLE_OPTIONS,null,contentValues);
        db.close();
    }

    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_OPTIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


    private static final String TABLE_ApexNews = "apexNews";
    private static final String KEY_ID = "_id";
    private static final String KEY_ID_NEWS = "idNews";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_SHORT_CONTENT = "shortContent";
    private static final String KEY_FEATURED = "featured";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";
    private static final String KEY_PHOTOPATH = "photopath";
    private static final String KEY_URL = "url";
    private static final String KEY_MAIN = "main";

    private static final String[] COLUMNS = {KEY_ID, KEY_ID_NEWS, KEY_TITLE, KEY_CONTENT, KEY_SHORT_CONTENT,
            KEY_FEATURED, KEY_CREATED_AT, KEY_UPDATED_AT, KEY_PHOTOPATH, KEY_URL, KEY_MAIN};

    public void addApex(Apex apex) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, apex.getTitle());
        values.put(KEY_ID_NEWS, apex.getIdNews());
        values.put(KEY_CONTENT, apex.getContent());
        values.put(KEY_SHORT_CONTENT, apex.getShortContent());
        values.put(KEY_FEATURED, apex.getFeatured());
        values.put(KEY_CREATED_AT, apex.getCreated_at());
        values.put(KEY_UPDATED_AT, apex.getUpdated_at());
        values.put(KEY_PHOTOPATH, apex.getImagePath());
        values.put(KEY_URL, apex.getUrl());
        values.put(KEY_MAIN, apex.getMain());

        Log.d("add", "add");
        db.insert(TABLE_ApexNews,
                null,
                values);
        db.close();
    }


    public Apex getApex(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ApexNews, COLUMNS, " id = ?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Apex apex = new Apex();
        apex.setId(Integer.parseInt(cursor.getString(0)));
        apex.setIdNews(cursor.getString(1));
        apex.setTitle(cursor.getString(2));
        apex.setContent(cursor.getString(3));
        apex.setShortContent(cursor.getString(4));
        apex.setFeatured(cursor.getString(5));
        apex.setCreated_at(cursor.getString(6));
        apex.setUpdated_at(cursor.getString(7));
        apex.setImagePath(cursor.getString(8));
        apex.setUrl(cursor.getString(9));
        apex.setMain(cursor.getString(10));
        cursor.close();
        db.close();
        return apex;
    }

    public int updateApex(Apex apex) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, apex.getTitle());
        values.put(KEY_CONTENT, apex.getContent());
        values.put(KEY_SHORT_CONTENT, apex.getShortContent());
        values.put(KEY_FEATURED, apex.getFeatured());
        values.put(KEY_CREATED_AT, apex.getCreated_at());
        values.put(KEY_UPDATED_AT, apex.getUpdated_at());
        values.put(KEY_PHOTOPATH, apex.getImagePath());
        values.put(KEY_URL, apex.getUrl());
        values.put(KEY_MAIN, apex.getMain());

        int i = db.update(TABLE_ApexNews, values, KEY_ID_NEWS + " = ?", new String[]{String.valueOf(apex.getIdNews())});

        db.close();
        return i;
    }

    public void deleteApex(Apex apex) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ApexNews, KEY_ID + " = ?", new String[]{String.valueOf(apex.getId())});
        db.close();
    }

    //Universal method to get an ApexList
    public ArrayList<Apex> getApexList(String query) {
        ArrayList<Apex> apexes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

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
                apex.setUpdated_at(cursor.getString(7));
                apex.setImagePath(cursor.getString(8));
                apex.setUrl(cursor.getString(9));
                apex.setMain(cursor.getString(10));
                apexes.add(apex);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return apexes;
    }

    public ArrayList<Apex> getAllApex() {
        String query = "SELECT  * FROM " + TABLE_ApexNews + " ORDER BY " + KEY_CREATED_AT + " DESC;";
        return getApexList(query);
    }

    public ArrayList<Apex> getArchiveNews() {
        String query = "SELECT  * FROM " + TABLE_ApexNews + " WHERE main = 'false' ORDER BY " + KEY_CREATED_AT + " DESC;";
        return getApexList(query);
    }

    public ArrayList<Apex> getFetchedNews() {
        String query = "SELECT  * FROM " + TABLE_ApexNews + " WHERE featured = 'true' ORDER BY " + KEY_CREATED_AT + " DESC;";
        return getApexList(query);
    }

    public ArrayList<Apex> getMainMotFetchedNews() {
        String query = "SELECT  * FROM " + TABLE_ApexNews + " WHERE featured = 'false' AND main = 'true'  ORDER BY "
                + KEY_CREATED_AT + " DESC;";
        return getApexList(query);
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

    public HashMap<String, String> getUpdatedDatesToId(ArrayList<String> idNews) {
        HashMap<String, String> result = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        for (int i = 0; i < idNews.size(); i++) {
            String id = idNews.get(i);
            String query = "SELECT updated_at FROM " + TABLE_ApexNews + " WHERE " + KEY_ID_NEWS + "=" + id;
            Cursor cursor = db.rawQuery(query, null);
            String updated = "";
            if (cursor.moveToFirst()) {
                do {
                    updated = (cursor.getString(0));
                    result.put(id, updated);
                } while (cursor.moveToNext());
            }
        }
        db.close();
        Log.d("map", result.toString());
        return result;
    }

    public void checkOnUpdates(ArrayList<String> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        StringBuilder inQuery = new StringBuilder();

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
        Log.d("delete",inQuery.toString());
        inQuery.append(")");
        db.delete(TABLE_ApexNews, KEY_ID_NEWS + " NOT IN " + inQuery.toString(), null);
        db.close();
    }
}
