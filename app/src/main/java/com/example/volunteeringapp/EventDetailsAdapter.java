package com.example.volunteeringapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventDetailsAdapter extends RecyclerView.Adapter<EventDetailsAdapter.EventViewHolder> {
    ArrayList<Event> eventList;
    ArrayList<Event> eventListFiltered;
    Context context;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Calendar c;

    public EventDetailsAdapter(ArrayList<Event> eventList) {
        this.eventList = eventList;
        eventListFiltered = new ArrayList<>(eventList);
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
        holder.eventDate.setText(event.getDate() + ", " +  event.getStartTime());
        holder.eventLocation.setText(event.getLocation());
        holder.setEventData(event);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            eventListFiltered = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                eventListFiltered.addAll(eventList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Event event : eventList) {
                    if (event.getEventTitle().toLowerCase().contains(filterPattern)) {
                        eventListFiltered.add(event);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = eventListFiltered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            eventList.clear();
            eventList.addAll((ArrayList<Event>) results.values);
            notifyDataSetChanged();
        }

    };


    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventDate, eventLocation;
        ImageView eventImg;
        Event eventData;
        public EventViewHolder(View itemView) {
            super(itemView);
            eventTitle = (TextView) itemView.findViewById(R.id.eventTitle);
            eventDate = (TextView) itemView.findViewById(R.id.eventDate);
            eventLocation = (TextView) itemView.findViewById(R.id.eventLocation);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), EventDetails.class);
                    System.out.println(eventData.getId());
                    intent.putExtra("eventId", eventData.getId());
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        public void setEventData(Event event){
            eventData = event;
        }

        public Event getEventData(){
            return eventData;
        }

    }
}
