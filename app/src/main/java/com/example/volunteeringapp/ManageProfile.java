package com.example.volunteeringapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ManageProfile extends AppCompatActivity {

    private Button changepassword, savechange, discardchange;
    private EditText name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);

        Bundle bundle = getIntent().getExtras();

        EditText name = findViewById(R.id.ET_name);
        EditText email = findViewById(R.id.ET_email);

        Button savechange = findViewById(R.id.BTN_saveProfile);
        Button discardchange = findViewById(R.id.BTN_discardProfileChange);

        savechange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String nameinput = name.getText().toString();
                name.setText("Saved");
            }
        });

    }
    public void onBackPressed(){
        super.onBackPressed();
    }
}

        /*private TextWatcher profileTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String nameInput = name.getText().toString().trim();
                String emailInput = email.getText().toString().trim();

                savechange.setEnabled(!nameInput.isEmpty() && !emailInput.isEmpty());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
   // public void onClick(View v){
     //   finish();
    //}


        savechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) ManageProfile.this.findViewById(R.id.ET_name)).setText(name.getText().toString());
                ((EditText) ManageProfile.this.findViewById(R.id.ET_email)).setText(email.getText().toString());
            }
        });

        String nametext = name.getText().toString();
        String emailtext = email.getText().toString();

        if(nametext.length() == 0){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_LONG).show();
        }
        else if (emailtext.length() == 0){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_LONG).show();
        }

        else {
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();
        }

    }*/

