package com.example.volunteeringapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ManageEventsActivity extends AppCompatActivity {
    final Fragment eventsF = new fragment_manage_events();
    DBHelper DB;
    Button btn_test, btn_test2;
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter eventAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Event> eventList;
    Fragment active = eventsF;
    final FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_events);

        fm.beginTransaction().add(R.id.frameFragment, eventsF, "1").commit();

        DB = new DBHelper(this);
        btn_test = findViewById(R.id.btn_test);
        btn_test2 = findViewById(R.id.btn_test2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManagement sessionManagement = new SessionManagement(ManageEventsActivity.this);
                String organizer_id = Integer.toString(sessionManagement.getSession());

                Cursor res = DB.manageEventsGet(organizer_id);
                if(res.getCount()==0){
                    Toast.makeText(ManageEventsActivity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Event Title :"+res.getString(0)+"\n");
                    buffer.append("Description :"+res.getString(1)+"\n");
                    buffer.append("Capacity :"+res.getString(2)+"\n\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(ManageEventsActivity.this);
                builder.setCancelable(true);
                builder.setTitle("User Entries");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });

        btn_test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(ManageEventsActivity.this, "Test", Toast.LENGTH_SHORT).show();

                Intent editEvent = new Intent (ManageEventsActivity.this, EditEventActivity.class);
                editEvent.putExtra("event_id", "1");
                startActivityForResult(editEvent, 1);
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public boolean navigateToCreateEvents(View view){
        Intent createEvent = new Intent (this, CreateEventActivity.class);
        startActivityForResult(createEvent, 1);
        return true;
    }
}