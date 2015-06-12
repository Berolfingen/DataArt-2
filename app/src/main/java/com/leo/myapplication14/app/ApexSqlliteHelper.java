package com.leo.myapplication14.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class ApexSqlliteHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ApexDB";

    public ApexSqlliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_APEX_TABLE = "CREATE TABLE apexNews(" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "title TEXT,"+
                "photo BLOB,"+
                "content TEXT,"+
                "shortContent TEXt,"+
                "featured TEXT,"+
                "created_at TEXT);";

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
    private static final String KEY_TITLE = "title";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_SHORT_CONTENT = "shortContent";
    private static final String KEY_FEATURED = "featured";
    private static final String KEY_CREATED_AT = "created_at";

    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE, KEY_PHOTO, KEY_CONTENT, KEY_SHORT_CONTENT, KEY_FEATURED, KEY_CREATED_AT};

    public void addApex(Apex apex){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, apex.getTitle()); // get title
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        apex.getImage().compress(Bitmap.CompressFormat.PNG, 100, bos);
        values.put(KEY_PHOTO, bos.toByteArray());
        values.put(KEY_CONTENT,apex.getContent());
        values.put(KEY_SHORT_CONTENT,apex.getShortContent());
        values.put(KEY_FEATURED,apex.getFeatured());
        values.put(KEY_CREATED_AT,apex.getCreated_at());

        db.insert(TABLE_ApexNews, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        db.close();
    }

    public Apex getApex(int id){
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        // 2. build query
        Cursor cursor =
                db.query(TABLE_ApexNews, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
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
        apex.setTitle(cursor.getString(1));
        byte[] byteArray = cursor.getBlob(2);
        apex.setImage(BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length));
        apex.setContent(cursor.getString(3));
        apex.setShortContent(cursor.getString(4));
        apex.setFeatured(cursor.getString(5));
        apex.setCreated_at(cursor.getString(6));
        return apex;
    }

    public int updateApex(Apex apex) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, apex.getTitle()); // get title
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        apex.getImage().compress(Bitmap.CompressFormat.PNG, 100, bos);
        values.put(KEY_PHOTO, bos.toByteArray());
        values.put(KEY_CONTENT,apex.getContent());
        values.put(KEY_SHORT_CONTENT,apex.getShortContent());
        values.put(KEY_FEATURED,apex.getFeatured());
        values.put(KEY_CREATED_AT,apex.getCreated_at());

        // 3. updating row
        int i = db.update(TABLE_ApexNews, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(apex.getId()) }); //selection args

        // 4. close
        db.close();
        return i;
    }

    public void deleteApex(Apex apex) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_ApexNews, //table name
                KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(apex.getId()) }); //selections args
        // 3. close
        db.close();
    }

    public List<Apex> getAllApex() {
        List<Apex> apexes = new LinkedList<Apex>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_ApexNews;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Apex apex = null;
        if (cursor.moveToFirst()) {
            do {
                apex = new Apex();
                apex.setId(Integer.parseInt(cursor.getString(0)));
                apex.setTitle(cursor.getString(1));
                byte[] byteArray = cursor.getBlob(2);
                apex.setImage(BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length));
                apex.setContent(cursor.getString(3));
                apex.setShortContent(cursor.getString(4));
                apex.setFeatured(cursor.getString(5));
                apex.setCreated_at(cursor.getString(6));

                // Add book to books
                apexes.add(apex);
            } while (cursor.moveToNext());
        }

        Log.d("getAllApex()", apex.toString());

        // return books
        return apexes;
    }



}
