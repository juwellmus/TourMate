package com.example.musa.tourmate.Fragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.musa.tourmate.Activity.MainActivity;
import com.example.musa.tourmate.Authentication.LoginActivity;
import com.example.musa.tourmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventFragment extends Fragment {

    EditText etEventName, etStartLocation, etDestination, etDepartureDate, etBudget;
    Button btnCreateEvent,btnDate;
    private ProgressDialog loadingBar;

    FirebaseAuth mAuth;
    DatabaseReference userRef;

    String current_user_id;
    String saveCurrentDate,saveCurrentTime,postRandomName,newDate;


    public AddEventFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vw =  inflater.inflate(R.layout.fragment_gallary, container, false);


        Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        final int mHour = c.get(Calendar.HOUR);
        final int mMinute = c.get(Calendar.MINUTE);


        loadingBar = new ProgressDialog(getActivity());

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");


        etEventName = (EditText) vw.findViewById(R.id.eventName);
        etStartLocation = (EditText) vw.findViewById(R.id.startLocation);
        etDestination = (EditText) vw.findViewById(R.id.destination);
        etDepartureDate = (EditText) vw.findViewById(R.id.departureDate);
        etBudget = (EditText) vw.findViewById(R.id.budget);

        btnCreateEvent = (Button) vw.findViewById(R.id.btnAddEvent);
        btnDate = (Button) vw.findViewById(R.id.btnPickDate);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        if (dayOfMonth <10 && month <10)
                        {
                            etDepartureDate.setText("0"+dayOfMonth+"-0"+month +"-"+ year);
                        }
                        else if (dayOfMonth>10 && month <10)
                        {
                            etDepartureDate.setText(dayOfMonth+"-0"+month +"-"+ year);
                        }
                        else if (dayOfMonth <10 && month >10)
                        {
                            etDepartureDate.setText("0"+dayOfMonth+"-"+month +"-"+ year);
                        }
                        else {
                            etDepartureDate.setText(dayOfMonth+"-"+month +"-"+ year);
                        }
                    }
                },mYear,mMonth+1,mDay);
                datePickerDialog.show();
            }
        });

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveEventInformation();
            }
        });




        return vw;
    }

    private void SaveEventInformation() {
        String eventName = etEventName.getText().toString();
        String startLocation = etStartLocation.getText().toString();
        String destination = etDestination.getText().toString();
        String departureDate = etDepartureDate.getText().toString();
        String budget = etBudget.getText().toString();


        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());


        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calFordTime.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;


        if(TextUtils.isEmpty(eventName))
        {
            //Toast.makeText(getActivity(), "Please write your Event...", Toast.LENGTH_SHORT).show();
            etEventName.setError("Fill this field !!");
        }
        else if(TextUtils.isEmpty(startLocation))
        {
            //Toast.makeText(getActivity(), "Please write your Start Location...", Toast.LENGTH_SHORT).show();
            etStartLocation.setError("Fill this field !!");
        }
        else if(TextUtils.isEmpty(destination))
        {
            //Toast.makeText(getActivity(), "Please write your destination...", Toast.LENGTH_SHORT).show();
            etDestination.setError("Fill this field !!");
        }
        else if(TextUtils.isEmpty(budget))
        {
            //Toast.makeText(getActivity(), "Please write your destination...", Toast.LENGTH_SHORT).show();
            etBudget.setError("Fill this field !!");
        }
        else if(TextUtils.isEmpty(departureDate))
        {
            etDepartureDate.setError("Fill this field !!");
        }
        else {
            boolean expire = isExpire(departureDate);
            if (expire)
            {
                etDepartureDate.setError("Please Input Valid Date");
                Toast.makeText(getActivity(), "Please Input Valid Date", Toast.LENGTH_SHORT).show();
            }
            else
            {
                loadingBar.setTitle("Saving Information");
                loadingBar.setMessage("Please wait, while we are saving your Information...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                HashMap userMap = new HashMap();

                userMap.put("eid", current_user_id);
                userMap.put("currentDate",saveCurrentDate);
                userMap.put("currentTime",saveCurrentTime);
                userMap.put("eventName", eventName);
                userMap.put("startLocation", startLocation);
                userMap.put("destination", destination);
                userMap.put("budget",budget);
                userMap.put("departureDate", departureDate);
                userMap.put("createDate", saveCurrentDate);

                userRef.child(current_user_id + postRandomName).updateChildren(userMap)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful())
                                {
                                    SendUserToMainActivity();
                                    Toast.makeText(getActivity(), "Event Successfully Add", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();


                                }else {
                                    String message =  task.getException().getMessage();
                                    Toast.makeText(getActivity(), "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            }
                        });
            }
        }

    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        //mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }

    private boolean isExpire(String date){
        if(date.isEmpty() || date.trim().equals("")){
            return false;
        }else{
            SimpleDateFormat sdf =  new SimpleDateFormat("dd-MM-yyyy"); // Jan-20-2015 1:30:55 PM
            Date d=null;
            Date d1=null;
            try {
                //System.out.println("expdate>> "+date);
                //System.out.println("today>> "+today+"\n\n");
                d = sdf.parse(date);
                d1 = new Date(System.currentTimeMillis());

                if(d1.compareTo(d) <0){// not expired
                    return false;
                }else if(d.compareTo(d1)==0){// both date are same
                    if(d.getTime() < d1.getTime()){// not expired
                        return false;
                    }else if(d.getTime() == d1.getTime()){//expired
                        return true;
                    }else{//expired
                        return true;
                    }
                }else{//expired
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }
    }





    public void onResume(){
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle("Add Event");
    }

}
