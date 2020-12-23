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

}