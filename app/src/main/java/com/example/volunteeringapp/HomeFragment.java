package com.example.volunteeringapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

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
    ArrayList<Event> eventList;
    DBHelper DB;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        recyclerView =  view.findViewById(R.id.rv_events);
        this.getAllEvents();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.homepage_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search for events");
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getAllEvents();
                eventAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                getAllEvents();
                eventAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void navigateToEventDetails(View view){
        Intent eventDetails = new Intent (getContext(), EventDetails.class);
        startActivity(eventDetails);
    }

    public void getAllEvents(){
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
                eventItem.setCoverPhoto(res.getBlob(res.getColumnIndex("cover_photo")));
                eventList.add(eventItem);
            }
        }
        res.close();
        layoutManager = new LinearLayoutManager(getContext());
        eventAdapter = new EventDetailsAdapter(eventList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventAdapter);
    }

}