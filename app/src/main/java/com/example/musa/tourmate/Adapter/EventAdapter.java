package com.example.musa.tourmate.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.musa.tourmate.Fragments.EventFragment;
import com.example.musa.tourmate.Pojo_Class.Event;
import com.example.musa.tourmate.R;

import java.util.List;

/**
 * Created by Musa on 5/24/2018.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context context;
    private List<Event> eventList;

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event__row, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.tvEventName.setText(event.getEventName());
        holder.tvStartDate.setText(event.getDepartureDate());
        holder.tvCreateDate.setText(event.getCreateDate());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        TextView tvEventName, tvStartDate, tvCreateDate, tvDaysLeft;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEventName = (TextView) itemView.findViewById(R.id.tvEventName);
            tvCreateDate = (TextView) itemView.findViewById(R.id.tvcreatDate);
            tvStartDate = (TextView) itemView.findViewById(R.id.tvStartDate);
            tvDaysLeft = (TextView) itemView.findViewById(R.id.tvDaysLeft);
        }
    }
}