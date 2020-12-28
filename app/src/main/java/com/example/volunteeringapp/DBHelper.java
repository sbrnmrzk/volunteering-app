package com.example.volunteeringapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Login.db";

    public DBHelper(@Nullable Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(emailAddress TEXT primary key, password TEXT)");
        MyDB.execSQL("create Table events(event_title TEXT, description TEXT, capacity INTEGER, start_date TEXT, start_time TEXT, end_time TEXT, " +
                "cover_photo BLOB, rewards TEXT, location TEXT, location_lat TEXT, location_long TEXT, organizer ID, participants TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists events");
    }

    public Boolean createEvent(String event_title, String description, String capacity, String location, String start_date, String start_time, String end_time, String organizer_id, Bitmap cover_photo){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("event_title", event_title);
        contentValues.put("description", description);
        contentValues.put("capacity", capacity);
        contentValues.put("location", location);
        contentValues.put("start_date", start_date);
        contentValues.put("start_time", start_time);
        contentValues.put("end_time", end_time);
        contentValues.put("organizer", organizer_id);

        byte[] data = getBitmapAsByteArray(cover_photo);
        contentValues.put("cover_photo", data);

        long result = MyDB.insert("events", null, contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public Cursor manageEventsGet()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from events", null);
        return cursor;
    }

    public Boolean insertData(String emailAddress, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("emailAddress", emailAddress);
        contentValues.put("password", password);
        long result = MyDB.insert("users", null, contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }

    public Boolean checkEmailAddress(String emailAddress){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where emailAddress = ?", new String[] {emailAddress});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkEmailAddressPassword(String emailAddress, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where emailAddress = ? and password = ?", new String[] {emailAddress, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
}
