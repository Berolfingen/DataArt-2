package com.leo.myapplication14.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class ApexSqlliteHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ApexDB";

    public ApexSqlliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_APEX_TABLE = "CREATE TABLE books ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title VARCHAR, "+
                "photo BLOB "+
                "content TEXT"+
                "shortContent VARCHAR"+
                "featured VARCHAR"+
                "created_at VARCHAR)";

        db.execSQL(CREATE_APEX_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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

    public void addBook(Apex apex){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, apex.getTitle()); // get title
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        apex.getImage().compress(Bitmap.CompressFormat.PNG, 100, bos);
        values.put(KEY_PHOTO, bos.toByteArray());

        // 3. insert
      /*  db.insert(TABLE_BOOKS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();*/
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
