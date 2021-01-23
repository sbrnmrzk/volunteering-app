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
        MyDB.execSQL("create Table follow(user_id INTEGER, follower_id INTEGER, PRIMARY KEY(user_id, follower_id), FOREIGN KEY (user_id) REFERENCES users (ID)" +
                ", FOREIGN KEY (follower_id) REFERENCES users (ID))");
        MyDB.execSQL("create Table rating(rating_user INTEGER NOT NULL, rater_user INTEGER NOT NULL, rating_value REAL NOT NULL, PRIMARY KEY (rating_user, rater_user) " +
                ", FOREIGN KEY (rating_user) REFERENCES users (ID), FOREIGN KEY (rater_user) REFERENCES users (ID))");
        MyDB.execSQL("create Table event_history(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER , event_id INTEGER, history_type TEXT)");
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

    public Boolean createEventHistory(String userId, Integer eventId, String type){
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", userId);
        contentValues.put("event_id", eventId);

        //get type == bookmark or type == joined
        contentValues.put("history_type", type);

        long result = MyDB.insert("event_history", null, contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }

    public boolean removeEventFromHistory(String userId, Integer eventId, String type){
        try {
            String strSQL = "DELETE FROM event_history WHERE user_id = '"+ userId + "' AND event_id = " +eventId.toString() + " AND history_type = '" + type + "'";
            MyDB.execSQL(strSQL);
            return true;
        }
        catch (Exception e){
            System.out.println("ERROR!" + e);
            return false;
        }
    }

    public Cursor getEventHistory(Integer userId, String type){
        try{
            Cursor cursor = MyDB.rawQuery("Select * from events where user_id = ? and history_type = ?", new String[] {userId.toString(), type});
            return cursor;
        }catch (Exception e){
            System.out.println("ERROR -> " + e);
            return null;
        }
    }

    public boolean checkIfBookmarked(Integer userId, String eventId){
        try{
            Cursor cursor = MyDB.rawQuery("Select * from event_history where user_id = ?  AND event_id = ?  AND history_type ='BOOKMARK'", new String[] {userId.toString(), eventId});
            if(cursor.getCount()>0){
                return true;
            }
            else {
                return false;
            }
        }catch (Exception e){
            System.out.println("ERROR -> " + e);
            return false;
        }
    }

    public Boolean editEvent(String event_title, String description, String capacity, String location, String start_date, String start_time, String end_time, int organizer_id, Bitmap cover_photo, String lat, String lat2, int event_id){

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

        long result = MyDB.update("events", contentValues, "ID = ?", new String[]{Integer.toString(event_id)});
        if (result==-1)
            return false;
        else
            return true;
    }

    public Boolean deleteEvent(String event_id) {
        long result = MyDB.delete("events", "ID = ?", new String[]{event_id});
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
        Cursor cursor = MyDB.rawQuery("Select ID, event_title,description, capacity, start_date, start_time, end_time, location, organizer, participants, cover_photo from events where organizer = ?", new String[] {organizer_id});
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

    public Boolean followUser(Integer user_id, Integer follower_id){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", user_id);
        contentValues.put("follower_id", follower_id);
        long result = MyDB.insert("follow", null, contentValues);
        if (result==-1)
            return true;
        else
            return false;
    }

    public void unfollowUser(Integer user_id, Integer follower_id){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.execSQL("DELETE FROM follow WHERE follower_id='"+ follower_id +"'");
    }

    public Cursor getFollowing(Integer user_id){
        SQLiteDatabase MyDB = this.getReadableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from follow where user_id = ?", new String[] {String.valueOf(user_id)});
        return cursor;
    }

    public Cursor getFollowers(Integer follower_id){
        SQLiteDatabase MyDB = this.getReadableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from follow where follower_id = ?", new String[] {String.valueOf(follower_id)});
        return cursor;
    }

    public Cursor checkFollowing(Integer follower_id, Integer organizer_id){
        SQLiteDatabase MyDB = this.getReadableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from follow where follower_id = ? and user_id = ?", new String[] {String.valueOf(follower_id), String.valueOf(organizer_id)});
        return cursor;
    }

    public Boolean giveRating(Integer rating_user, Integer rater_user, Float rating_value){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValuesProfile = new ContentValues();
        contentValues.put("rating_user", rating_user);
        contentValues.put("rater_user", rater_user);
        contentValues.put("rating_value", rating_value);
        long result = MyDB.insert("rating", null, contentValues);
        if (result==-1)
            return true;
        else
            return false;
    }

    public Boolean updateAvgRating(Float avgRating, Integer ID){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("avgRating", avgRating);
        long result = MyDB.update("profiles", contentValues, "ID = ?", new String[] {String.valueOf(ID)});
        if (result==-1)
            return true;
        else
            return false;
    }

    public Cursor getAvgRating(Integer ID){
        SQLiteDatabase MyDB = this.getReadableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from profiles where ID = ?", new String[] {String.valueOf(ID)});
        return cursor;
    }

    public Cursor getRaters(Integer rater_user){
        SQLiteDatabase MyDB = this.getReadableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from rating where rater_user = ?", new String[] {String.valueOf(rater_user)});
        return cursor;
    }

    public Cursor getRating(){
        SQLiteDatabase MyDB = this.getReadableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from users where id = ?", null);
        return cursor;
    }
}
