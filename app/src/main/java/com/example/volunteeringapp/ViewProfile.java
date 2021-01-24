package com.example.volunteeringapp;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayInputStream;

public class ViewProfile extends AppCompatActivity {

    RatingBar ratingBar;
    ImageView cover_photo;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ratingBar = (RatingBar) findViewById(R.id.ratingBarUP);
        cover_photo = (ImageView) findViewById(R.id.IV_CoverPhoto3UP);

        SessionManagement sessionManagement = new SessionManagement(ViewProfile.this);
        String current_user = Integer.toString(sessionManagement.getSession());
        int userID = sessionManagement.getSession();

        Integer user_id = Integer.valueOf(current_user); // Retrieve the current user id
        Integer follower_id = Integer.valueOf(current_user);; // Retrieve the selected user's id

        DB = new DBHelper(this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Cursor GetUserByID = DB.getUserById(current_user);
                Cursor GetFollowing = DB.getFollowing(userID);
                Cursor GetFollowers = DB.getFollowers(userID);
                Boolean CheckPicture = DB.checkProfilePicture(current_user);
                Cursor GetRating = DB.getAvgRating(userID);
                Cursor GetRaters = DB.getRaters(userID);

                TextView name = (TextView) findViewById(R.id.ET_nameUP);
                TextView date = (TextView) findViewById(R.id.ET_joinedUP);
                TextView following = (TextView) findViewById(R.id.ET_following_numbersUP);
                TextView followers = (TextView) findViewById(R.id.ET_followers_numbersUP);
                TextView ratingCount = (TextView) findViewById(R.id.ratingCountUP);
                RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBarUP);

                if(!CheckPicture){
                    if (GetUserByID != null && GetUserByID.getCount() > 0) {
                        GetUserByID.moveToFirst();
                        cover_photo.setImageResource(R.drawable.defaulticon);
                    }
                } else {
                    if (GetUserByID != null && GetUserByID.getCount() > 0) {
                        GetUserByID.moveToFirst();
                        byte[] blob = GetUserByID.getBlob(GetUserByID.getColumnIndex("profilePicture"));
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        cover_photo.setImageBitmap(bitmap);
                    }
                }

                if (GetUserByID != null && GetUserByID.getCount() > 0) {
                    GetUserByID.moveToFirst();
                    name.setText(GetUserByID.getString(1));
                    date.setText("Joined " + GetUserByID.getString(6));
                }

                if (GetFollowing !=null && GetFollowing.getCount() > 0) {
                    GetFollowing.moveToFirst();
                    System.out.println("Number of following: " + GetFollowing.getCount());
                    String followingCount = String.valueOf(GetFollowing.getCount());
                    followers.setText(followingCount);
                } else {
                    Toast.makeText(ViewProfile.this, "No data available for followers!", Toast.LENGTH_SHORT).show();
                }

                if (GetFollowers !=null && GetFollowers.getCount() > 0) {
                    GetFollowers.moveToFirst();
                    System.out.println("Number of followers: " + GetFollowers.getCount());
                    String followersCount = String.valueOf(GetFollowers.getCount());
                    following.setText(followersCount);
                } else {
                    Toast.makeText(ViewProfile.this, "No data available for following!", Toast.LENGTH_SHORT).show();
                }

                if (GetRating !=null && GetRating.getCount() > 0) {
                    GetRating.moveToFirst();
                    Float RatingStar = GetRating.getFloat(5);
                    System.out.println("Number of rating count: " + RatingStar);
                    ratingBar.setRating(RatingStar);
                } else {
                    Float RatingStar = 0.0f;
                    ratingBar.setRating(RatingStar);
                }

                if (GetRaters !=null && GetRaters.getCount() > 0) {
                    GetRaters.moveToFirst();
                    String RatingCount = String.valueOf(GetRaters.getCount());
                    System.out.println("Number of rating count: " + GetRaters.getCount());
                    ratingCount.setText("(" + RatingCount + ")");
                } else {
                    ratingCount.setText("(0)");
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
