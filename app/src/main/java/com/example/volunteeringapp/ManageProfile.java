package com.example.volunteeringapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ManageProfile extends AppCompatActivity {

    Button changepassword, savechange, discardchange;
    EditText name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);

        Bundle bundle = getIntent().getExtras();

        name = findViewById(R.id.ET_name);
        email = findViewById(R.id.ET_email);

        //Button savechange = findViewById(R.id.BTN_saveProfile);
        // Button discardchange = findViewById(R.id.BTN_discardProfileChange);

    }

}