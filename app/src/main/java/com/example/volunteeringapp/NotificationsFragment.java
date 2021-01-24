package com.example.volunteeringapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<NotificationItem> notificationList;
    DBHelper DB;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
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
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        setHasOptionsMenu(true);
        recyclerView =  view.findViewById(R.id.rv_notifications);
        this.getAllNotifications();

        ImageButton btn_deleteNotifs = (ImageButton) view.findViewById(R.id.btn_deleteNotifications);
        btn_deleteNotifs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Clear notifications");
                alertDialogBuilder.setMessage("Are you sure you want to delete all notifications?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                SessionManagement sessionManagement = new SessionManagement(getContext());
                                Integer userId = sessionManagement.getSession();
                                DB.removeAllNotifications(userId.toString());
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(NotificationsFragment.this).attach(NotificationsFragment.this).commit();
                            }
                        });
                alertDialogBuilder.setNegativeButton("no",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                System.out.println("Closed!");
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        return view;
    }

    private void getAllNotifications() {
        SessionManagement sessionManagement = new SessionManagement(getContext());
        int userId = sessionManagement.getSession();
        notificationList = new ArrayList<NotificationItem>();
        notificationList.clear();
        DB = new DBHelper(getContext());
        Cursor res = DB.getNotifications(userId);
        if(res != null && res.getCount()>0){
            notificationList.clear();
            while (res.moveToNext()){
                NotificationItem notificationItem = new NotificationItem();
                notificationItem.setId(res.getInt(res.getColumnIndex("ID")));
                notificationItem.setNotificationDetails(res.getString(res.getColumnIndex("notification")));
                notificationItem.setNotificationTime(res.getString(res.getColumnIndex("notification_date")));
                notificationItem.setEventId(res.getInt(res.getColumnIndex("event_id")));
                notificationItem.setProfileId(res.getInt(res.getColumnIndex("follower_id")));
                notificationList.add(notificationItem);
            }
        }
        res.close();
        layoutManager = new LinearLayoutManager(getContext());
        notificationAdapter = new NotificationAdapter(notificationList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(notificationAdapter);
    }


}