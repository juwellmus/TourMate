package com.example.musa.tourmate.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musa.tourmate.Activity.EventDetailsActivity;
import com.example.musa.tourmate.Adapter.EventAdapter;
import com.example.musa.tourmate.Pojo_Class.Event;
import com.example.musa.tourmate.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    FloatingActionButton btnAddEvent;


    private EventFragmentInterface fragmentInterface;


    private RecyclerView recyclerView;



    FirebaseAuth mAuth;
    DatabaseReference userRef;


    String current_user_id;
    String newDate;


    public EventFragment() {
        // Required empty public constructor

    }

    public interface EventFragmentInterface{
        void changeEventFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_event, container, false);

       fragmentInterface = (EventFragmentInterface) getActivity();

        btnAddEvent = (FloatingActionButton) v.findViewById(R.id.fab);
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //displayInputDialog();
                fragmentInterface.changeEventFragment();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");


        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        DisplayAllEvents();



        return v;
    }

    private void DisplayAllEvents() {

            FirebaseRecyclerAdapter<Event,EventInfoViewHolder>  firebaseRecyclerAdapter =
                    new FirebaseRecyclerAdapter<Event, EventInfoViewHolder>
                            (
                                    Event.class,
                                    R.layout.event__row,
                                    EventInfoViewHolder.class,
                                    userRef.orderByChild("eid").equalTo(current_user_id)
                            )
                    {
                        @Override
                        protected void populateViewHolder(EventInfoViewHolder viewHolder, Event model, int position) {

                            final String eventKey = getRef(position).getKey();

                            viewHolder.setEventName(model.getEventName());
                            viewHolder.setCreateDate(model.getCreateDate());
                            viewHolder.setDepartureDate(model.getDepartureDate());

                            Calendar calFordDate = Calendar.getInstance();
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                            newDate = currentDate.format(calFordDate.getTime());

                            int date = (int)getDateDiff(new SimpleDateFormat("dd-MM-yyyy"),newDate,model.getDepartureDate());

                            viewHolder.setDaysLeft(String.valueOf(date));


                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent eventDetailsIntent = new Intent(getActivity(), EventDetailsActivity.class);
                                    eventDetailsIntent.putExtra("eventKey",eventKey);
                                    startActivity(eventDetailsIntent);
                                }
                            });

                        }
                    };
            recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    public static class EventInfoViewHolder extends RecyclerView.ViewHolder {
        View mView;
        //TextView eventName,createDate,startDate,daysLeft;

        public EventInfoViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setEventName(String eventName)
        {
            TextView evntName = (TextView) mView.findViewById(R.id.tvLocation);
            evntName.setText(eventName);
        }
        public void setCreateDate(String createDate)
        {
            TextView createdDate = (TextView) mView.findViewById(R.id.tvcreatDate);
            createdDate.setText(createDate);
        }
        public void setDepartureDate(String departureDate)
        {
            TextView startDate = (TextView) mView.findViewById(R.id.tvStartDate);
            startDate.setText(departureDate);
        }
        public void setDaysLeft(String daysLeft)
        {
            TextView dayLeft = (TextView) mView.findViewById(R.id.tvDaysLeft);
            dayLeft.setText(daysLeft);
        }

    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {

                return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
