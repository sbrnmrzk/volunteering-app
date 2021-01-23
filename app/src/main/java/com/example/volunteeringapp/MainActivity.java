package com.example.volunteeringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public boolean navigateToLogin(View view){
        Intent eventDetails = new Intent (this, Login.class);
        startActivityForResult(eventDetails, 1);
        return true;
    }

    public boolean navigateToSignup(View view){
        Intent signup = new Intent (this, Signup.class);
        startActivityForResult(signup, 1);
        return true;
    }

    @Override
    protected void onStart(){
        super.onStart();

        checkSession();
    }

    private void checkSession() {
        //Check if user is logged in
        //If user is logged in --> Return to Homepage

        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        int userID = sessionManagement.getSession();

        if(userID != -1){
            //User ID logged in and return to homepage
            Intent intent  = new Intent(getApplicationContext(), Homepage.class);
            startActivity(intent);
        } else {
            //Do nothing
        }
    }

}