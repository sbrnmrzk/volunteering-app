package com.example.volunteeringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventDetails extends AppCompatActivity {
    Event event;
    User organizer;
    DBHelper DB;
    TextView eventTitle, eventDateStart, eventDateEnd, eventDescription, eventLocation, startTime, endTime, organizerName,
        organizerDate;

    List<Event> eventList;
    List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.eventChild);
        setSupportActionBar(myChildToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        eventTitle = (TextView) findViewById(R.id.tv_eventTitle);
        eventDescription = (TextView) findViewById(R.id.tv_eventDesc);
        eventDateStart = (TextView) findViewById(R.id.tv_eventDate);
        eventDateEnd = (TextView) findViewById(R.id.tv_eventDate2);
        eventLocation = (TextView) findViewById(R.id.tv_eventLocation);
        startTime = (TextView) findViewById(R.id.tv_startTime);
        endTime = (TextView) findViewById(R.id.tv_endTime);
        organizerName = (TextView) findViewById(R.id.tv_eventOrganizer);
        organizerDate = (TextView) findViewById(R.id.tv_eventOrganizerJoined);

        //get Event by ID
        int eventId = getIntent().getIntExtra("eventId", 0);
        System.out.println("Event id = " + eventId);
        eventList = new ArrayList<Event>();
        userList = new ArrayList<User>();
        userList.clear();
        eventList.clear();

        //GET EVENT BY ID
        event = getEvent(eventId);

        //GET ORGANIZER DETAILS BY ORGANIZER ID
        organizer = getOrganizerDetailsById(event.getOrganizerId());

        eventTitle.setText(event.getEventTitle());
        eventDescription.setText(event.getDescription());
        eventDateStart.setText(event.getDate());
        eventDateEnd.setText(event.getDate());
        startTime.setText(event.getStartTime());
        endTime.setText(event.getEndTime());
        eventLocation.setText(event.getLocation());
        organizerName.setText(organizer.getName());
        try {
            Date joinedDate = new SimpleDateFormat("yyyy-MM-dd").parse(organizer.getJoinedDate());
            SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy");
            organizerDate.setText("Joined " + formatter.format(joinedDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public User getOrganizerDetailsById(String organizerId) {
        try{
            DB = new DBHelper(this);
            Cursor res = DB.getUserById(organizerId);
            if(res != null && res.getCount()>0){
                userList.clear();
                while (res.moveToNext()){
                    User userResult = new User();
                    userResult.setId(res.getInt(res.getColumnIndex("ID")));
                    userResult.setName(res.getString(res.getColumnIndex("name")));
                    userResult.setJoinedDate(res.getString(res.getColumnIndex("joined_date")));
                    userList.add(userResult);
                }
            }
            res.close();
            return userList.get(0);
        } catch (Exception e){
            System.out.println("ERROR: " + e);
            return null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(getApplicationContext(), "Bookmarked", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Event getEvent(int eventId){
        try{
            DB = new DBHelper(this);
            Cursor res = DB.getEventById(eventId);
            if(res != null && res.getCount()>0){
                eventList.clear();
                while (res.moveToNext()){
                    Event eventItem = new Event();
                    eventItem.setId(res.getInt(res.getColumnIndex("ID")));
                    eventItem.setEventTitle(res.getString(res.getColumnIndex("event_title")));
                    eventItem.setDescription(res.getString(res.getColumnIndex("description")));
                    eventItem.setCapacity(res.getInt(res.getColumnIndex("capacity")));
                    eventItem.setDate(res.getString(res.getColumnIndex("start_date")));
                    eventItem.setStartTime(res.getString(res.getColumnIndex("start_time")));
                    eventItem.setEndTime(res.getString(res.getColumnIndex("end_time")));
                    eventItem.setLocation(res.getString(res.getColumnIndex("location")));
                    eventItem.setOrganizerId(res.getString(res.getColumnIndex("organizer")));
                    eventList.add(eventItem);
                }
            }
            res.close();
            return eventList.get(0);
        } catch (Exception e){
            System.out.println("ERROR: " + e);
            return null;
        }
    }
}