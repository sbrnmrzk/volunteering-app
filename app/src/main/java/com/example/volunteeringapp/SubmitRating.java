package com.example.volunteeringapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SubmitRating extends AppCompatActivity {

    RatingBar ratingBar;
    Button submitRating;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_rating);

        ratingBar = (RatingBar) findViewById(R.id.ratingBarSubmit);
        submitRating = (Button) findViewById(R.id.btn_give_rating);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

//                Toast.makeText(SubmitRating.this, "Rating: " + String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();
            }
        });

        submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(SubmitRating.this, "Rating: ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
