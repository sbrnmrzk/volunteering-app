package com.example.volunteeringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EventDetails extends AppCompatActivity {
    Event event;
    int eventId;
    User organizer;
    DBHelper DB;
    TextView eventTitle, eventDateStart, eventDateEnd, eventDescription, eventLocation, startTime, endTime, organizerName,
        organizerDate;
    Button btnVolunteer, btnCancelVolunteer, btn_follow, btn_unfollow, btnEditEvent;
    MenuItem btn_bookmark, btn_unbookmark;
    List<Event> eventList;
    List<User> userList;
    String participants, userId;
    List<String> participantList;
    ImageView eventCover;
    Menu menu;
    ImageView cover_photo;
    final Fragment mapF = new MapsFragment();
    Fragment active = mapF;
    final FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

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
        btnVolunteer = (Button) findViewById(R.id.btn_volunteer);
        btnCancelVolunteer = (Button) findViewById(R.id.btn_unvolunteer);
        btnEditEvent = (Button) findViewById(R.id.btn_editEvent);
        eventCover = (ImageView) findViewById(R.id.iv_EventCover);
        btn_follow = (Button) findViewById(R.id.btn_followOrganizer);
        btn_unfollow = (Button) findViewById(R.id.btn_unfollowOrganizer);
        cover_photo = (ImageView) findViewById(R.id.eventImg);
        //get Event by ID
        eventId = getIntent().getIntExtra("eventId", 0);
        System.out.println("Event id = " + eventId);
        eventList = new ArrayList<Event>();
        userList = new ArrayList<User>();
        userList.clear();
        eventList.clear();

        //GET EVENT BY ID
        event = getEvent(eventId);
        if (event == null) {
            return;
        }
        //GET ORGANIZER DETAILS BY ORGANIZER ID
        organizer = getOrganizerDetailsById(event.getOrganizerId());

        //INITIALIZE FRAGMENT
        fm.beginTransaction().add(R.id.frameFragment, mapF, "1").commit();

        SessionManagement sessionManagement = new SessionManagement(EventDetails.this);
        userId = Integer.toString(sessionManagement.getSession());
        participants = event.getParticipants();
        if(participants == null){
            participants = "";
        }
        participantList = Arrays.asList(participants.split(","));

        setButtonVisibility(userId);

        eventTitle.setText(event.getEventTitle());
        eventDescription.setText(event.getDescription());
        eventDateStart.setText(event.getDate());
        eventDateEnd.setText(event.getDate());
        startTime.setText(event.getStartTime());
        endTime.setText(event.getEndTime());
        eventLocation.setText(event.getLocation());
        organizerName.setText(organizer.getName());

        ByteArrayInputStream inputStream = new ByteArrayInputStream(event.getCoverPhoto());
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        eventCover.setImageBitmap(bitmap);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MapsFragment fragment = (MapsFragment) getSupportFragmentManager().findFragmentById(R.id.frameFragment);
                String address = (event.getLocation()).toString();
                fragment.locationSearch(address);
            }
        }, 500);

        try {
            Date joinedDate = new SimpleDateFormat("yyyy-MM-dd").parse(organizer.getJoinedDate());
            SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy");
            organizerDate.setText("Joined " + formatter.format(joinedDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setButtonVisibility(String userId) {
        Cursor GetFollowers = DB.checkFollowing(Integer.valueOf(userId), Integer.valueOf(event.getOrganizerId()));
        Cursor GetUserByID = DB.getUserById(event.getOrganizerId());
        Boolean CheckPicture = DB.checkProfilePicture(event.getOrganizerId());

        if(!CheckPicture){
            if (GetUserByID != null && GetUserByID.getCount() > 0) {
                GetUserByID.moveToFirst();
                cover_photo.setImageResource(R.drawable.defaulticon);
            }
        } else {
            if (GetUserByID != null && GetUserByID.getCount() > 0) {
                GetUserByID.moveToFirst();
                byte[] blob = GetUserByID.getBlob(GetUserByID.getColumnIndex("profilePicture"));
                ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                cover_photo.setImageBitmap(bitmap);
            }
        }

        if (GetFollowers !=null && GetFollowers.getCount() > 0) {
            btn_follow.setVisibility(View.INVISIBLE);
            btn_unfollow.setVisibility(View.VISIBLE);
        }
        else {
            btn_follow.setVisibility(View.VISIBLE);
            btn_unfollow.setVisibility(View.INVISIBLE);
        }

        if ((event.getOrganizerId()).equals(userId)) {
            btnVolunteer.setVisibility(View.INVISIBLE);
            btnCancelVolunteer.setVisibility(View.INVISIBLE);
            btnEditEvent.setVisibility(View.VISIBLE);
        } else {
            btnEditEvent.setVisibility(View.INVISIBLE);
            if(event.getCapacity()>participantList.size()){
                btnVolunteer.setText("VOLUNTEER");
                btnVolunteer.setAlpha(1f);
                if(participantList.contains(userId)){
                    btnCancelVolunteer.setVisibility(View.VISIBLE);
                    btnVolunteer.setVisibility(View.INVISIBLE);
                }
                else {
                    btnVolunteer.setVisibility(View.VISIBLE);
                    btnCancelVolunteer.setVisibility(View.INVISIBLE);
                }
            }
            else{
                btnCancelVolunteer.setVisibility(View.INVISIBLE);
                btnVolunteer.setVisibility(View.VISIBLE);
                btnVolunteer.setClickable(false);
                btnVolunteer.setAlpha(0.5f);
                btnVolunteer.setText("CAPACITY FULL");
            }

        }

        if (userId.equals(event.getOrganizerId())){
            btn_follow.setVisibility(View.GONE);
            btn_unfollow.setVisibility(View.GONE);
        }
    }
    public void onClickEditEvent (View view) {
        Intent editEvent = new Intent (EventDetails.this, EditEventActivity.class);
        editEvent.putExtra("event_id", Integer.toString(event.getId()));
        startActivityForResult(editEvent, 1);
    }

    public void onClickVolunteer(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm volunteering");
        alertDialogBuilder.setMessage("Are you sure you want to volunteer for " +event.getEventTitle() + "?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                    if(participants.length()>0){
                                        participants = participants + "," + userId;
                                    }
                                    else{
                                        participants = userId;
                                    }
                                    DB = new DBHelper(getApplicationContext());
                                    event.setParticipants(participants);
                                    boolean result = DB.updateParticipantsList(participants, event.getId());
                                    if(result){
                                        DB.createEventHistory(userId, eventId, "JOINED");
                                        participantList = Arrays.asList(participants.split(","));
                                        Toast.makeText(EventDetails.this,"Successfully added." + participants,Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(EventDetails.this,"Adding participant failed " + participants,Toast.LENGTH_LONG).show();
                                    }

                                    //rechecks button visibility
                                    setButtonVisibility(userId);
                                }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                finish();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void onCancelVolunteer(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Cancel volunteering");
        alertDialogBuilder.setMessage("Are you sure you want to cancel volunteering for " +  event.getEventTitle() + "?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String strNew;
                        if(participants.length()>1){
                            strNew = participants.replaceFirst("," + userId, "");
                            participants = strNew;

                        }
                        else if(participants.length()==1){
                            strNew = participants.replaceFirst(userId, "");
                            participants = strNew;
                        }
                        else{
                            Toast.makeText(EventDetails.this, "NO USERS IN LIST", Toast.LENGTH_LONG).show();
                        }
                        DB = new DBHelper(getApplicationContext());
                        event.setParticipants(participants);
                        boolean result = DB.updateParticipantsList(participants, event.getId());
                        boolean eventHistory = DB.removeEventFromHistory(userId, eventId, "JOINED");
                        if(result && eventHistory){
                            participantList = Arrays.asList(participants.split(","));
                            Toast.makeText(EventDetails.this,"Successfully removed." + participants,Toast.LENGTH_LONG).show();
                        }
                        else if (!result){
                            Toast.makeText(EventDetails.this,"Removing participant failed " + participants,Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(EventDetails.this,"Removing event failed " + participants,Toast.LENGTH_LONG).show();
                        }

                        //rechecks button visibility
                        setButtonVisibility(userId);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                finish();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public boolean navigateToProfile(View view){

        Intent profileDetails = new Intent(this, ViewOrganizerProfile.class);
        profileDetails.putExtra("organizer_id", event.getOrganizerId());
        startActivityForResult(profileDetails, 1);
        return true;

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

    private boolean checkIfBookmarked() {
        return DB.checkIfBookmarked(Integer.valueOf(userId), String.valueOf(eventId));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_details_menu, menu);
        this.menu = menu;
        MenuItem bookmark = menu.findItem(R.id.btn_bookmark);
        MenuItem unbookmark = menu.findItem(R.id.btn_unbookmark);

        if((event.getOrganizerId()).equals(userId)){
            bookmark.setVisible(false);
            unbookmark.setVisible(false);
        }
        else {
            if (checkIfBookmarked()) {
                bookmark.setVisible(false);
                unbookmark.setVisible(true);
            } else {
                bookmark.setVisible(true);
                unbookmark.setVisible(false);
            }
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MenuItem bookmarkItem = menu.findItem(R.id.btn_bookmark);
        MenuItem unbookmarkItem = menu.findItem(R.id.btn_unbookmark);
        DB = new DBHelper(this);
        switch (item.getItemId()) {
            case R.id.btn_bookmark:
                DB.createEventHistory(userId, eventId, "BOOKMARK");
                bookmarkItem.setVisible(false);
                unbookmarkItem.setVisible(true);
                Toast.makeText(getApplicationContext(), "Bookmarked", Toast.LENGTH_LONG).show();
                return true;
            case R.id.btn_unbookmark:
                DB.removeEventFromHistory(userId, eventId, "BOOKMARK");
                bookmarkItem.setVisible(true);
                unbookmarkItem.setVisible(false);
                Toast.makeText(getApplicationContext(), "Removed from bookmarks", Toast.LENGTH_LONG).show();
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
                    eventItem.setParticipants(res.getString(res.getColumnIndex("participants")));
                    eventItem.setCoverPhoto(res.getBlob(res.getColumnIndex("cover_photo")));
                    eventList.add(eventItem);
                }
            } else {
                finish();
            }
            res.close();
            return eventList.get(0);
        } catch (Exception e){
            System.out.println("ERROR: " + e);
            return null;
        }
    }
}