package com.example.volunteeringapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.util.Calendar;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Login.db";
    SQLiteDatabase MyDB = this.getWritableDatabase();

    public DBHelper(@Nullable Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT, emailAddress TEXT, password TEXT)");
        MyDB.execSQL("create Table profiles(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT, emailAddress TEXT, password TEXT, profilePicture BLOB, avgRating REAL, joined_date TEXT)");
        MyDB.execSQL("create Table events(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, event_title TEXT, description TEXT, capacity INTEGER, start_date TEXT, start_time TEXT, end_time TEXT, " +
                "cover_photo BLOB, rewards TEXT, location TEXT, location_lat TEXT, location_long TEXT, organizer TEXT, participants TEXT)");
        MyDB.execSQL("create Table follow(ID INTEGER NOT NULL, user_id INTEGER, follower_id INTEGER,  PRIMARY KEY (ID, user_id, follower_id))");
        MyDB.execSQL("create Table rating(ID INTEGER NOT NULL, user_id INTEGER , rating FLOAT, PRIMARY KEY (ID, rating))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists events");
    }

    public Boolean createEvent(String event_title, String description, String capacity, String location, String start_date, String start_time, String end_time, int organizer_id, Bitmap cover_photo, String lat, String lat2){
        ContentValues contentValues = new ContentValues();
        contentValues.put("event_title", event_title);
        contentValues.put("description", description);
        contentValues.put("capacity", capacity);
        contentValues.put("location", location);
        contentValues.put("start_date", start_date);
        contentValues.put("start_time", start_time);
        contentValues.put("end_time", end_time);
        contentValues.put("organizer", organizer_id);
        contentValues.put("location_lat", lat);
        contentValues.put("location_long", lat2);

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

    public Cursor manageEventsGet(String organizer_id)
    {
        Cursor cursor = MyDB.rawQuery("Select * from events where organizer = ?", new String[] {organizer_id});
        // still retrieve all events, when session is finished, will update so will only get events that are organized by the user.
        return cursor;
    }

    public Cursor getAllEvents(){
        Cursor cursor = MyDB.rawQuery("Select ID, event_title,description, capacity, start_date, start_time, end_time, location, organizer, participants, cover_photo from events", null);
        return cursor;
    }

    public Cursor getEventById(Integer id){
        Cursor cursor = MyDB.rawQuery("Select ID, event_title,description, capacity, start_date, start_time, end_time, location, organizer, participants, location_lat, location_long, cover_photo from events where id = ?", new String[] {id.toString()});
        return cursor;
    }

    public boolean updateParticipantsList(String participants, Integer eventId){
        try {
            String strSQL = "UPDATE events set participants = '"+ participants + "' where id = " +eventId.toString();
            MyDB.execSQL(strSQL);
            return true;
        }
        catch (Exception e){
            System.out.println("ERROR!" + e);
            return false;
        }

    }

    public Boolean insertData(String name, String emailAddress, String password){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("emailAddress", emailAddress);
        contentValues.put("password", password);
        long result = MyDB.insert("users", null, contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }

    public Cursor getUserById(String id){
        Cursor cursor = MyDB.rawQuery("Select * from profiles where id = ?", new String[] {id});
        return cursor;
    }

    public Boolean insertDataProfile(String name, String emailAddress, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("emailAddress", emailAddress);
        contentValues.put("password", password);
        contentValues.put("joined_date", (new Date(System.currentTimeMillis())).toString());
        long result = MyDB.insert("profiles", null, contentValues);
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

    public Cursor getData(String emailAddress, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where emailAddress = ? and password = ?", new String[] {emailAddress, password});
        return cursor;
    }

    public Cursor getRating(){
        SQLiteDatabase MyDB = this.getReadableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from users where id = ?", null);
        return cursor;
    }
}
