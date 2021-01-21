package com.example.volunteeringapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewProfile extends AppCompatActivity {

    RatingBar ratingBar;
    Button btn_follow;
    Button btn_contact;
    Button btn_give_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btn_follow = (Button) findViewById(R.id.btn_follow);
        btn_contact = (Button) findViewById(R.id.btn_contact);
        btn_give_rating = (Button) findViewById(R.id.btn_give_rating);

        float currentAvgRating = 5;
        ratingBar.setRating(5);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {


                float currentRating = ratingBar.getRating();
                float calculatedAvgRating = (currentAvgRating + currentRating)/2;

                Toast.makeText(ViewProfile.this, "Rating: " + String.valueOf(calculatedAvgRating), Toast.LENGTH_SHORT).show();
                ratingBar.setRating(calculatedAvgRating);
            }
        });

        btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewProfile.this, "Followed!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewProfile.this, "Contacted!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void SubmitRating(View view) {
        Intent submitRating = new Intent(this, SubmitRating.class);
        startActivity(submitRating);
    }
}
