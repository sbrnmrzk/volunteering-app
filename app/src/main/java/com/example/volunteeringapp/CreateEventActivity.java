package com.example.volunteeringapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {

    EditText ET_EventTitle, ET_Description, ET_Location, ET_Capacity, ET_StartDate, ET_StartTime, ET_EndTime;
    Button btn_createEvent;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        ET_EventTitle = (EditText) findViewById(R.id.ET_EventTitle);
        ET_Description = (EditText) findViewById(R.id.ET_Description);
        ET_Location = (EditText) findViewById(R.id.ET_Location);
        ET_Capacity = (EditText) findViewById(R.id.ET_Capacity);
        ET_StartDate = (EditText) findViewById(R.id.ET_StartDate);
        ET_StartTime = (EditText) findViewById(R.id.ET_StartTime);
        ET_EndTime = (EditText) findViewById(R.id.ET_EndTime);
        // photo not available yet
        // rewards not available yet
        btn_createEvent = (Button) findViewById(R.id.btn_createEvent);
        DB = new DBHelper(this);


        Toolbar createEvent_toolbar = (Toolbar) findViewById(R.id.createEvent_toolbar);
        setSupportActionBar(createEvent_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btn_createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateEventActivity.this, ET_StartDate.getText().toString(), Toast.LENGTH_SHORT).show();

//                String event_title = ET_EventTitle.getText().toString();
//                String description = ET_Description.getText().toString();
//                String capacity = ET_Capacity.getText().toString();
//                String location = ET_Location.getText().toString();
//                String start_date = ET_StartDate.getText().toString();
//                String start_time = ET_StartTime.getText().toString();
//                String end_time = ET_EndTime.getText().toString();
//                String organizer_id = "1"; // session is not yet implemented, so will hardcode this first
//                // String location_lat
//                // String location_long
//                // String cover_photo?
//                // String rewards?
//
//                if(event_title.equals("")||description.equals(""))
//                    Toast.makeText(CreateEventActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
//                else {
//                    Boolean createEvent = DB.createEvent(event_title, description, capacity, location, start_date, start_time, end_time, organizer_id);
//                    if (createEvent == true) {
//                        Toast.makeText(CreateEventActivity.this, "Registered successfully.", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), ManageEventsActivity.class);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(CreateEventActivity.this, "Event creation failed.", Toast.LENGTH_SHORT).show();
//                    }
//                }
            }
        });

        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "dd MMM yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                ET_StartDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        ET_StartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateEventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean createEvent(View view){
        return true;
    }
}