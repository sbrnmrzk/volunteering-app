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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_manage_events#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_manage_events extends Fragment {

    DBHelper DB;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter eventAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<Event> eventList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_manage_events() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_manage_events.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_manage_events newInstance(String param1, String param2) {
        fragment_manage_events fragment = new fragment_manage_events();
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
        View view = inflater.inflate(R.layout.fragment_manage_events, container, false);
        recyclerView =  view.findViewById(R.id.rv_events);
        eventList = new ArrayList<Event>();
        eventList.clear();
        DB = new DBHelper(getContext());
        Cursor res = DB.getAllEvents();
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
        layoutManager = new LinearLayoutManager(getContext());
        eventAdapter = new EventDetailsAdapter(eventList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventAdapter);
        return view;
    }
}