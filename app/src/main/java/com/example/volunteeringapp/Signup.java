package com.example.volunteeringapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Signup extends AppCompatActivity {

    EditText name, emailAddress, password, repeatPassword;
    Button Signup;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = (EditText) findViewById(R.id.name);
        emailAddress = (EditText) findViewById(R.id.emailAddress);
        password = (EditText) findViewById(R.id.password);
        repeatPassword = (EditText) findViewById(R.id.repeatPassword);
        Signup = (Button) findViewById(R.id.btnSignup);
        DB = new DBHelper(this);
        TextView textView = (TextView) this.findViewById(R.id.logInRedirect);

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nama = name.getText().toString();
                String user = emailAddress.getText().toString();
                String pass = password.getText().toString();
                String repass = repeatPassword.getText().toString();

                Cursor res = DB.getData(user, pass);

                if(user.equals("")||pass.equals("")||repass.equals(""))
                    Toast.makeText(Signup.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = DB.checkEmailAddress(user);
                        if(checkuser==false){
                            Boolean insert = DB.insertData(nama, user, pass);
                            if(insert==true){
                                Toast.makeText(Signup.this, "Registered successfully.", Toast.LENGTH_SHORT).show();
                                while(res.moveToNext()) {
                                    int id = res.getInt(0);
                                    String name = res.getString(1);
                                    User userSession = new User(id, name);
                                    SessionManagement sessionManagement = new SessionManagement(Signup.this);
                                    sessionManagement.saveSession(userSession);
                                }
                                Intent intent = new Intent(getApplicationContext(),Homepage.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(Signup.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(Signup.this, "User already exists! please sign in.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Signup.this, "Password does not match.", Toast.LENGTH_SHORT).show();
                    }
                } }


        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }

//    public boolean navigateToHome(View view){
//        Intent home = new Intent (this, Homepage.class);
//        startActivityForResult(home, 1);
//        return true;
//    }
}