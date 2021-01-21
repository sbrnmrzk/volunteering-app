package com.example.volunteeringapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

public class EditEventActivity extends AppCompatActivity {

    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DB = new DBHelper(this);
        String event_id = getIntent().getStringExtra("event_id");
        Cursor res = DB.getEventById(Integer.valueOf(event_id));
        if(res.getCount()==0){
            Toast.makeText(EditEventActivity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            Toast.makeText(EditEventActivity.this, res.getString(4), Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(EditEventActivity.this, event_id, Toast.LENGTH_SHORT).show();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
    }
}