package com.example.volunteeringapp;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private EventDetailsAdapter eventAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button joinedbtn, bookmarkedbtn;
    ArrayList<Event> eventList;
    DBHelper DB;
    Integer userId;
    
    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookmarkedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        setHasOptionsMenu(true);
        recyclerView =  (RecyclerView) view.findViewById(R.id.rv_history);
        bookmarkedbtn = (Button) view.findViewById(R.id.btn_eventsBookmarked);
        joinedbtn = (Button) view.findViewById(R.id.btn_eventsJoined);
        bookmarkedbtn.setOnClickListener(this);
        joinedbtn.setOnClickListener(this);
        SessionManagement sessionManagement = new SessionManagement(getContext());
        userId = sessionManagement.getSession();
        this.getEventHistory();
        joinedbtn.setClickable(false);
        joinedbtn.setAlpha(.5f);
        bookmarkedbtn.setClickable(true);
        bookmarkedbtn.setAlpha(1f);
        return view;
    }

    private void getEventHistory() {
        eventList = new ArrayList<Event>();
        eventList.clear();
        DB = new DBHelper(getContext());
        Cursor res = DB.getEventHistory(userId, "JOINED");
        if(res != null && res.getCount()>0){
            eventList.clear();
            while (res.moveToNext()){
                int eventId = res.getInt(res.getColumnIndex("event_id"));
                Cursor eventhistory = DB.getEventById(eventId);
                if(eventhistory != null && eventhistory.getCount()>0){
                    eventList.clear();
                    while (eventhistory.moveToNext()){
                        Event eventItem = new Event();
                        eventItem.setId(eventhistory.getInt(eventhistory.getColumnIndex("ID")));
                        eventItem.setEventTitle(eventhistory.getString(eventhistory.getColumnIndex("event_title")));
                        eventItem.setDescription(eventhistory.getString(eventhistory.getColumnIndex("description")));
                        eventItem.setCapacity(eventhistory.getInt(eventhistory.getColumnIndex("capacity")));
                        eventItem.setDate(eventhistory.getString(eventhistory.getColumnIndex("start_date")));
                        eventItem.setStartTime(eventhistory.getString(eventhistory.getColumnIndex("start_time")));
                        eventItem.setEndTime(eventhistory.getString(eventhistory.getColumnIndex("end_time")));
                        eventItem.setLocation(eventhistory.getString(eventhistory.getColumnIndex("location")));
                        eventItem.setOrganizerId(eventhistory.getString(eventhistory.getColumnIndex("organizer")));
                        eventItem.setParticipants(eventhistory.getString(eventhistory.getColumnIndex("participants")));
                        eventItem.setCoverPhoto(eventhistory.getBlob(eventhistory.getColumnIndex("cover_photo")));
                        eventList.add(eventItem);
                    }
                }
                res.close();
            }
        }
        layoutManager = new LinearLayoutManager(getContext());
        eventAdapter = new EventDetailsAdapter(eventList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventAdapter);
    }

    private void getBookmarkedEvents() {
        eventList = new ArrayList<Event>();
        eventList.clear();
        DB = new DBHelper(getContext());
        Cursor res = DB.getEventHistory(userId, "BOOKMARK");
        if(res != null && res.getCount()>0){
            eventList.clear();
            while (res.moveToNext()){
                int eventId = res.getInt(res.getColumnIndex("event_id"));
                Cursor eventhistory = DB.getEventById(eventId);
                if(eventhistory != null && eventhistory.getCount()>0){
                    eventList.clear();
                    while (eventhistory.moveToNext()){
                        Event eventItem = new Event();
                        eventItem.setId(eventhistory.getInt(eventhistory.getColumnIndex("ID")));
                        eventItem.setEventTitle(eventhistory.getString(eventhistory.getColumnIndex("event_title")));
                        eventItem.setDescription(eventhistory.getString(eventhistory.getColumnIndex("description")));
                        eventItem.setCapacity(eventhistory.getInt(eventhistory.getColumnIndex("capacity")));
                        eventItem.setDate(eventhistory.getString(eventhistory.getColumnIndex("start_date")));
                        eventItem.setStartTime(eventhistory.getString(eventhistory.getColumnIndex("start_time")));
                        eventItem.setEndTime(eventhistory.getString(eventhistory.getColumnIndex("end_time")));
                        eventItem.setLocation(eventhistory.getString(eventhistory.getColumnIndex("location")));
                        eventItem.setOrganizerId(eventhistory.getString(eventhistory.getColumnIndex("organizer")));
                        eventItem.setParticipants(eventhistory.getString(eventhistory.getColumnIndex("participants")));
                        eventItem.setCoverPhoto(eventhistory.getBlob(eventhistory.getColumnIndex("cover_photo")));
                        eventList.add(eventItem);
                    }
                }
                res.close();
            }
        }
        layoutManager = new LinearLayoutManager(getContext());
        eventAdapter = new EventDetailsAdapter(eventList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_eventsJoined:
                getEventHistory();
                joinedbtn.setClickable(false);
                joinedbtn.setAlpha(.5f);
                bookmarkedbtn.setClickable(true);
                bookmarkedbtn.setAlpha(1f);
                break;
            case R.id.btn_eventsBookmarked:
                getBookmarkedEvents();
                joinedbtn.setClickable(true);
                joinedbtn.setAlpha(1f);
                bookmarkedbtn.setClickable(false);
                bookmarkedbtn.setAlpha(.5f);
                break;
        }
    }
}