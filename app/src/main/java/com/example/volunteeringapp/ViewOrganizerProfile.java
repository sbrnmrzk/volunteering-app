package com.example.volunteeringapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class ViewOrganizerProfile extends AppCompatActivity {

    RatingBar ratingBar;
    Button btn_follow, btn_unfollow;
    Button btn_contact;
    Button btn_give_rating;
    DBHelper DB;
    ImageView cover_photo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btn_follow = (Button) findViewById(R.id.btn_follow);
        btn_unfollow = (Button) findViewById(R.id.btn_unfollow);
        btn_contact = (Button) findViewById(R.id.btn_contact);
        btn_give_rating = (Button) findViewById(R.id.btn_give_rating);
        cover_photo = (ImageView) findViewById(R.id.IV_CoverPhoto3);

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
                setButtonVisibility(organizer_id);

                Cursor GetUserByID = DB.getUserById(organizer);
                Cursor GetFollowing = DB.getFollowing(organizer_id);
                Cursor GetFollowers = DB.getFollowers(organizer_id);
                Cursor GetRating = DB.getAvgRating(organizer_id);
                Cursor GetRaters = DB.getRaters(organizer_id);
                Boolean CheckPicture = DB.checkProfilePicture(organizer);

                TextView name = (TextView) findViewById(R.id.ET_name);
                TextView date = (TextView) findViewById(R.id.ET_joined);
                TextView follow = (TextView) findViewById(R.id.btn_follow);
                TextView following = (TextView) findViewById(R.id.ET_following_numbers);
                TextView followers = (TextView) findViewById(R.id.ET_followers_numbers);
                TextView ratingCount = (TextView) findViewById(R.id.ratingCount);
                RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

                if (user_id.equals(organizer_id)){
                    follow.setVisibility(View.GONE);
                }

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
                    followers.setText(followingCount);
                    String check = String.valueOf(GetFollowing.getInt(1));
                } else {
//                    Toast.makeText(ViewOrganizerProfile.this, "No data available for followers!", Toast.LENGTH_SHORT).show();
                }

                 if (GetFollowers !=null && GetFollowers.getCount() > 0) {
                    GetFollowers.moveToFirst();
//                    Toast.makeText(ViewOrganizerProfile.this, "Data available for followers!", Toast.LENGTH_SHORT).show();
                    System.out.println("Number of followers: " + GetFollowers.getCount());
                    String followersCount = String.valueOf(GetFollowers.getCount());
                    following.setText(followersCount);
                 } else {
//                    Toast.makeText(ViewOrganizerProfile.this, "No data available for following!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                Cursor GetRating = DB.getAvgRating(organizer_id);
                Cursor GetRaters = DB.getRaters(organizer_id);
                Cursor CheckRaters = DB.checkRaters(organizer_id, user_id);
//
                float numberOfRater = GetRaters.getCount();
                float currentRating = ratingBar.getRating();

                if (user_id.equals(organizer_id)) {
                    Toast.makeText(ViewOrganizerProfile.this, "Cannot rate yourself!", Toast.LENGTH_SHORT).show();
                } else {
                    if (GetRating != null && GetRating.getCount() > 0) {
                        if (CheckRaters != null && CheckRaters.getCount() > 0) {
                            CheckRaters.moveToFirst();
                            Integer checkRaters = CheckRaters.getInt(1);
                            Toast.makeText(ViewOrganizerProfile.this, "This user has been rated!", Toast.LENGTH_SHORT).show();

                        } else {
                            GetRating.moveToFirst();
                            float currentAvgRating = GetRating.getFloat(5);
                            if (numberOfRater == 0) {
                                Toast.makeText(ViewOrganizerProfile.this, "No one ever rate this user!", Toast.LENGTH_SHORT).show();
                                float numberOfRaterPass = 1.0f;
                                float calculatedAvgRating = (currentAvgRating + currentRating) / numberOfRaterPass;
                                DB.giveRating(organizer_id, user_id, currentRating);
                                DB.updateAvgRating(calculatedAvgRating, organizer_id);
                            } else {
                                Toast.makeText(ViewOrganizerProfile.this, "This user has been rated!", Toast.LENGTH_SHORT).show();
                                float calculatedAvgRating = (currentAvgRating + currentRating) / (numberOfRater + 1);
                                DB.giveRating(organizer_id, user_id, currentRating);
                                DB.updateAvgRating(calculatedAvgRating, organizer_id);
                            }
                        }
                    }
                }
            }
        });

        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String organizer = getIntent().getStringExtra("organizer_id");
                Cursor GetEmail = DB.getUserById(organizer);
                if (GetEmail !=null && GetEmail.getCount() > 0) {
                GetEmail.moveToFirst();
                String email = GetEmail.getString(2);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
                intent.setType("message/rfc822");
                    if (intent.resolveActivity(getPackageManager()) != null){
                        startActivity(intent);
                    } else {
                        Toast.makeText(ViewOrganizerProfile.this, "Unable to contact!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ViewOrganizerProfile.this, "No email available.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setButtonVisibility(Integer organizerId) {
        SessionManagement sessionManagement = new SessionManagement(ViewOrganizerProfile.this);
        String current_user = Integer.toString(sessionManagement.getSession());
        String organizer = getIntent().getStringExtra("organizer_id");
        int userID = sessionManagement.getSession(); // Retrieve the current user id

        Cursor GetFollowers = DB.checkFollowing(userID, organizerId);

        if (GetFollowers !=null && GetFollowers.getCount() > 0) {
            btn_follow.setVisibility(View.INVISIBLE);
            btn_unfollow.setVisibility(View.VISIBLE);
        }
        else {
            btn_follow.setVisibility(View.VISIBLE);
            btn_unfollow.setVisibility(View.INVISIBLE);
        }
    }

    public void onClickFollow(View view){
        SessionManagement sessionManagement = new SessionManagement(ViewOrganizerProfile.this);
        String current_user = Integer.toString(sessionManagement.getSession());
        String organizer = getIntent().getStringExtra("organizer_id");

        Integer user_id = Integer.valueOf(current_user);
        Integer organizer_id = Integer.valueOf(organizer);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm following?");
//        alertDialogBuilder.setMessage("Are you sure you want to volunteer for " +event.getEventTitle() + "?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        DB.followUser(organizer_id, user_id);
                        Toast.makeText(ViewOrganizerProfile.this, "Followed!", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
//                        setButtonVisibility(organizer_id);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void onClickUnfollow(View view){
        SessionManagement sessionManagement = new SessionManagement(ViewOrganizerProfile.this);
        String current_user = Integer.toString(sessionManagement.getSession());
        String organizer = getIntent().getStringExtra("organizer_id");

        Integer user_id = Integer.valueOf(current_user);
        Integer organizer_id = Integer.valueOf(organizer);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm unfollow?");
//        alertDialogBuilder.setMessage("Are you sure you want to cancel volunteering for " +  event.getEventTitle() + "?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        DB.unfollowUser(organizer_id, user_id);
                        Toast.makeText(ViewOrganizerProfile.this, "Unfollowed!", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
//                        setButtonVisibility(organizer_id);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                finish();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
