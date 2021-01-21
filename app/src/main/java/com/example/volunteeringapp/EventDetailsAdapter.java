package com.example.volunteeringapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventDetailsAdapter extends RecyclerView.Adapter<EventDetailsAdapter.EventViewHolder> {
    List<Event> eventList;
    Context context;
    DBHelper dbHelper;
    SQLiteDatabase db;

    public EventDetailsAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View iteView = inflater.inflate(R.layout.event_card, parent, false);
        EventViewHolder viewHolder = new EventViewHolder(iteView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.eventTitle.setText(event.getEventTitle());
        holder.eventDate.setText(event.getDate());
        holder.eventLocation.setText(event.getLocation());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventDate, eventLocation;
        ImageView eventImg;
        public EventViewHolder(View itemView) {
            super(itemView);
            eventTitle = (TextView) itemView.findViewById(R.id.eventTitle);
            eventDate = (TextView) itemView.findViewById(R.id.eventDate);
            eventLocation = (TextView) itemView.findViewById(R.id.eventLocation);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), EventDetails.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

    }
}
