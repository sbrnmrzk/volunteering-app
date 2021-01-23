package com.example.volunteeringapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewOrganizerProfile extends AppCompatActivity {

    RatingBar ratingBar;
    Button btn_follow;
    Button btn_contact;
    Button btn_give_rating;
    DBHelper DB;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btn_follow = (Button) findViewById(R.id.btn_follow);
        btn_contact = (Button) findViewById(R.id.btn_contact);
        btn_give_rating = (Button) findViewById(R.id.btn_give_rating);

        SessionManagement sessionManagement = new SessionManagement(ViewOrganizerProfile.this);
        String current_user = Integer.toString(sessionManagement.getSession());
        int userID = sessionManagement.getSession(); // Retrieve the current user id
        String organizer = getIntent().getStringExtra("organizer_id"); // Retrieve the organizer_id

        Integer user_id = Integer.valueOf(current_user);
        Integer organizer_id = Integer.valueOf(organizer);

        DB = new DBHelper(this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Cursor GetUserByID = DB.getUserById(organizer);
                Cursor GetFollowing = DB.getFollowing(organizer_id);
                Cursor GetFollowers = DB.getFollowers(organizer_id);

                TextView name = (TextView) findViewById(R.id.ET_name);
                TextView date = (TextView) findViewById(R.id.ET_joined);
                TextView follow = (TextView) findViewById(R.id.btn_follow);
                TextView following = (TextView) findViewById(R.id.ET_following_numbers);
                TextView followers = (TextView) findViewById(R.id.ET_followers_numbers);

                if (GetUserByID != null && GetUserByID.getCount() > 0) {
                    GetUserByID.moveToFirst();
                    name.setText(GetUserByID.getString(1));
                    date.setText("Joined " + GetUserByID.getString(6));
                }

                if (GetFollowing !=null && GetFollowing.getCount() > 0) {
                    GetFollowing.moveToFirst();
//                    Toast.makeText(ViewOrganizerProfile.this, "Data available for following!", Toast.LENGTH_SHORT).show();
                    System.out.println("Number of following: " + GetFollowing.getCount());
                    String followingCount = String.valueOf(GetFollowing.getCount());
                    following.setText(followingCount);
                    String check = String.valueOf(GetFollowing.getInt(1));
                    if (current_user.equals(check)){
                        follow.setText("F O L L O W E D");
                    }
                } else {
                    Toast.makeText(ViewOrganizerProfile.this, "No data available for following!", Toast.LENGTH_SHORT).show();
                }

                 if (GetFollowers !=null && GetFollowers.getCount() > 0) {
                    GetFollowers.moveToFirst();
//                    Toast.makeText(ViewOrganizerProfile.this, "Data available for followers!", Toast.LENGTH_SHORT).show();
                    System.out.println("Number of followers: " + GetFollowers.getCount());
                    String followersCount = String.valueOf(GetFollowers.getCount());
                    followers.setText(followersCount);
                 } else {
                    Toast.makeText(ViewOrganizerProfile.this, "No data available for followers!", Toast.LENGTH_SHORT).show();
                }


//                while(GetFollowers.moveToNext() && GetFollowing.moveToNext()){
//                    following.setText(GetFollowing.getInt(0));
//                    followers.setText(GetFollowers.getInt(1));
//                }
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                Cursor GetRating = DB.getAvgRating(userID);
                Cursor GetRaters = DB.getRaters(userID);

                //DISPLAY CURRENT RATING

//                float currentAvgRating = GetRating.getFloat(5);
                float currentAvgRating = 1;
//                if (GetRating.getFloat(5) == 0.0f){
//                    Toast.makeText(ViewProfile.this, "No rating received before.", Toast.LENGTH_SHORT).show();
//                }
//                Toast.makeText(ViewProfile.this, "Rating: " + String.valueOf(currentAvgRating), Toast.LENGTH_SHORT).show();

                while(GetRating.moveToNext()){
                    ratingBar.setRating(GetRating.getFloat(5));
                }

                //UPDATE CURRENT RATING
                float numberOfRater = GetRaters.getCount();
                float currentRating = ratingBar.getRating();
//                float currentRating = 1;
                float calculatedAvgRating = (currentAvgRating + currentRating)/numberOfRater;
//                float calculatedAvgRating = 2;

                if (user_id.equals(organizer_id)){
                    Toast.makeText(ViewOrganizerProfile.this, "Cannot rate yourself!", Toast.LENGTH_SHORT).show();
                } else {
                    boolean GiveRating = DB.giveRating(user_id, organizer_id, currentRating);
                    if(!GiveRating){
                        Toast.makeText(ViewOrganizerProfile.this, "Rating added successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ViewOrganizerProfile.this, "Only allow to rate once only!", Toast.LENGTH_SHORT).show();
                    }
                    DB.updateAvgRating(calculatedAvgRating, user_id);
                    Toast.makeText(ViewOrganizerProfile.this, "Rating: " + String.valueOf(calculatedAvgRating), Toast.LENGTH_SHORT).show();

                }

                while(GetRating.moveToNext()){
                    ratingBar.setRating(GetRating.getFloat(2));
                }
            }
        });

        btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(user_id.equals(organizer_id)){
                    Toast.makeText(ViewOrganizerProfile.this, "Cannot follow yourself!", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean FollowUser = DB.followUser(organizer_id, user_id);
                    if (!FollowUser){
                        Toast.makeText(ViewOrganizerProfile.this, "Followed!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ViewOrganizerProfile.this, "Followed already!", Toast.LENGTH_SHORT).show();
                    }
                    Cursor res = DB.getFollowers(userID);
                    TextView tv = (TextView) findViewById(R.id.ET_followers_numbers);
                    while(res.moveToNext()){
                        String followersCount = String.valueOf(res.getCount());
                        tv.setText(followersCount);
                    }
                }
            }
        });
    }
}
