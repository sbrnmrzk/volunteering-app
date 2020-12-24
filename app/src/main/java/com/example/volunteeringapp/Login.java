package com.example.volunteeringapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText emailAddress, password;
    Button btnSignin;
    DBHelper DB;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailAddress = (EditText) findViewById(R.id.emailAddress1);
        password = (EditText) findViewById(R.id.password1);
        btnSignin = (Button) findViewById(R.id.btnSignin1);
        DB = new DBHelper(this);

        btnSignin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                String user = emailAddress.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("")||pass.equals(""))
                    Toast.makeText(Login.this, "Please enter all the fields required.", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass = DB.checkEmailAddressPassword(user, pass);
                    if(checkuserpass==true){
                        Toast.makeText(Login.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                        Intent intent  = new Intent(getApplicationContext(), Homepage.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Login.this, "Invalid credentials.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

//    public boolean navigateToHome(View view){
//        Intent home = new Intent (this, Homepage.class);
//        startActivityForResult(home, 1);
//        return true;
//    }
}