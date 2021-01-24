package com.example.volunteeringapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    ArrayList<NotificationItem> notifications;
    Context context;

    public NotificationAdapter(ArrayList<NotificationItem> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View iteView = inflater.inflate(R.layout.notification_details, parent, false);
        NotificationAdapter.NotificationViewHolder viewHolder = new NotificationAdapter.NotificationViewHolder(iteView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationItem notif = notifications.get(position);
        holder.notificationDetails.setText(notif.getNotificationDetails());
        holder.notificationTime.setText(notif.getNotificationTime());
        holder.setNotificationData(notif);
    }

    @Override
    public int getItemCount() {
        return this.notifications.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView notificationDetails, notificationTime;
        NotificationItem notificationItem;
        public NotificationViewHolder(View itemView) {
            super(itemView);
            notificationDetails = (TextView) itemView.findViewById(R.id.tv_notification);
            notificationTime = (TextView) itemView.findViewById(R.id.tv_notificationTime);

            //idk how to implement on click redirect to notification? might have to change db implementation
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(itemView.getContext(), EventDetails.class);
//                    System.out.println(eventData.getId());
//                    intent.putExtra("eventId", eventData.getId());
//                    itemView.getContext().startActivity(intent);
//                }
//            });
        }

        public void setNotificationData(NotificationItem notif) {
            this.notificationItem = notif;
        }

        public NotificationItem getNotificationData() {
            return this.notificationItem;
        }
    }
}
