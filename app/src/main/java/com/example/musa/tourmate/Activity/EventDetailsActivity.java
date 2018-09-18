package com.example.musa.tourmate.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musa.tourmate.Authentication.LoginActivity;
import com.example.musa.tourmate.Pojo_Class.Expense;
import com.example.musa.tourmate.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.http.Url;

public class EventDetailsActivity extends AppCompatActivity {

    private String eventKey;
    private EditText etEventExpense,etEventComment;
    private ProgressBar progressBar;
    private TextView tvExpenseStatus,tvExpenseAlart;


    TextView tvEventName,tvBudgetStatus,tvAddNewExpense,tvViewAllExpense,tvAddMoreBudget,tvTakePhoto,tvViewGallary,tvViewAllMoments,tvEditEvent,tvDeleteEvent;

    private FirebaseAuth mAuth;
    private DatabaseReference eventDetailsRef,expenseRef,imageRef;


    String current_user_id,eventName,postRandomName,saveCurrentDate,saveCurrentTime;
    int expense=0,expenseStatus=0,budgeTotal=0;

    private static final int CAMERA_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        tvExpenseStatus = (TextView)findViewById(R.id.tvExpenseStatus);
        tvExpenseAlart = (TextView)findViewById(R.id.tvExpenseAlart);

        eventKey = getIntent().getExtras().get("eventKey").toString();
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        eventDetailsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(eventKey);
        expenseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(eventKey).child("Expense");
        imageRef = FirebaseDatabase.getInstance().getReference().child("Users").child(eventKey).child("Moments");

        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    expense = expense + Integer.parseInt(snapshot.child("eventExpense").getValue().toString());
                    Log.d("item id ",String.valueOf(expense));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        tvEventName = (TextView) findViewById(R.id.tvEventName);
        tvBudgetStatus = (TextView) findViewById(R.id.tvBidgetStatus);
        tvAddNewExpense = (TextView) findViewById(R.id.addNewExpense);
        tvViewAllExpense = (TextView) findViewById(R.id.viewAllExpense);
        tvAddMoreBudget = (TextView) findViewById(R.id.addMoreBudget);
        tvTakePhoto = (TextView) findViewById(R.id.takePhoto);
        tvViewAllMoments = (TextView) findViewById(R.id.viewAllMoments);

        tvEditEvent = (TextView) findViewById(R.id.editEvent);
        tvDeleteEvent = (TextView) findViewById(R.id.deleteEvent);



        eventDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists())
               {
                   final String event_name = dataSnapshot.child("eventName").getValue().toString();
                   final String budget = dataSnapshot.child("budget").getValue().toString();

                   budgeTotal = Integer.parseInt(budget);

                   expenseStatus = ((100*expense)/budgeTotal);
                   progressBar.setProgress(expenseStatus);
                   if (expenseStatus > 100)
                   {
                       tvExpenseStatus.setText("100%");
                       tvExpenseStatus.setTextColor(Color.RED);
                   }else {
                       tvExpenseStatus.setText(String.valueOf(expenseStatus)+"%");
                       tvExpenseStatus.setTextColor(Color.BLACK);
                   }
                   if (expense>budgeTotal)
                   {
                       tvExpenseAlart.setText("Your Budget Limit Over !!");
                   }else{
                       tvExpenseAlart.setText("");
                   }

                   final String startLocatuon = dataSnapshot.child("startLocation").getValue().toString();
                   final String destination = dataSnapshot.child("destination").getValue().toString();
                   final String departureDate = dataSnapshot.child("departureDate").getValue().toString();
                   //final String eventExpense = dataSnapshot.child("Expense").child("7avhT1FeGWMaSIKvxP8Y49C3Ker226-May-201814:55:57").child("eventExpense").getValue().toString();

                   tvEventName.setText(event_name);
                   tvBudgetStatus.setText("Budget Status("+expense+"/"+budget+")");
                   //tvBudgetStatus.setText(eventExpense);



                   tvEditEvent.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           EditEvent(event_name,startLocatuon,departureDate,destination,budget);
                       }
                   });

                   tvAddNewExpense.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           expense=0;
                           displayInputDialog(event_name);
                       }
                   });
                   tvAddMoreBudget.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           AddMoreBudget(budget);
                       }
                   });

               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        tvDeleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteEvent();
            }
        });

        tvViewAllExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendEventListActivity();

            }
        });
        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });

        tvViewAllMoments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendAllImageActivity();
            }
        });

    }

    private void SendAllImageActivity() {
        Intent imageIntent = new Intent(EventDetailsActivity.this, ViewAllImageActivity.class);
        //mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        imageIntent.putExtra("eventKey",eventKey);
        startActivity(imageIntent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == CAMERA_REQUEST_CODE && resultCode == this.RESULT_OK) {
             Bundle extras = data.getExtras();
             Bitmap imageBitmap = (Bitmap) extras.get("data");
             encodeBitmapAndSaveToFirebase(imageBitmap);
         }

    }
    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calFordTime.getTime());

        postRandomName = current_user_id+saveCurrentDate + saveCurrentTime;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        /*DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_RESTAURANTS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(mRestaurant.getPushId())
                .child("imageUrl");*/

        HashMap imageMap = new HashMap();
        imageMap.put("uid",current_user_id);
        imageMap.put("currentDate",saveCurrentDate);
        imageMap.put("currentTime",saveCurrentTime);
        imageMap.put("image",imageEncoded);


        imageRef.child(postRandomName).updateChildren(imageMap)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(EventDetailsActivity.this, "Image Successfully Added", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(EventDetailsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void AddMoreBudget(final String budget) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View vw = inflater.inflate(R.layout.add_more_budget,null);

        final EditText etExtraBudget = (EditText) vw.findViewById(R.id.etExtraBudget);
        Button btnExtraBudget = (Button) vw.findViewById(R.id.btnAddExtraBudget);
        builder.setView(vw);
        final AlertDialog alertDialog = builder.create();

        btnExtraBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String extraBudget = etExtraBudget.getText().toString();
                int newBudget = Integer.parseInt(budget)+Integer.parseInt(extraBudget);

                eventDetailsRef.child("budget").setValue(String.valueOf(newBudget));
                alertDialog.dismiss();

            }
        });

        alertDialog.show();

    }

    private void SendEventListActivity() {
        Intent mainIntent = new Intent(EventDetailsActivity.this, ExpenseListActivity.class);
        //mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainIntent.putExtra("eventKey",eventKey);
        startActivity(mainIntent);

    }

    private void EditEvent(String event_name, String startLocatuon, String departureDate, String destination, String budget) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View vw = inflater.inflate(R.layout.edit_event_layout,null);

        final EditText etEventName = (EditText) vw.findViewById(R.id.eventName);
        final EditText etStartLocation = (EditText) vw.findViewById(R.id.startLocation);
        final EditText etDestination = (EditText) vw.findViewById(R.id.destination);
        final EditText etDepartureDate = (EditText) vw.findViewById(R.id.departureDate);
        final EditText etBudget = (EditText) vw.findViewById(R.id.budget);

        etEventName.setText(event_name);
        etStartLocation.setText(startLocatuon);
        etDepartureDate.setText(departureDate);
        etDestination.setText(destination);
        etBudget.setText(budget);

        Button btnEditEvent = (Button) vw.findViewById(R.id.btnEidtEvent);

        builder.setView(vw);

        final AlertDialog alertDialog = builder.create();

        btnEditEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventDetailsRef.child("eventName").setValue(etEventName.getText().toString());
                eventDetailsRef.child("startLocation").setValue(etStartLocation.getText().toString());
                eventDetailsRef.child("departureDate").setValue(etDepartureDate.getText().toString());
                eventDetailsRef.child("destination").setValue(etDestination.getText().toString());
                eventDetailsRef.child("budget").setValue(etBudget.getText().toString());
                Toast.makeText(EventDetailsActivity.this, "Successfully Update", Toast.LENGTH_SHORT).show();
                
                alertDialog.dismiss();
            }


        });

        alertDialog.show();

    }


    private void DeleteEvent() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setIcon(R.drawable.ic_action_delete);
        builder.setMessage("Want to delete this event");
        builder.setCancelable(false);
        builder.setNegativeButton("No",null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                eventDetailsRef.removeValue();
                SendUserToMainActivity();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();

    }
    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(EventDetailsActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void displayInputDialog(String event_name) {

         eventName = event_name;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View vw = inflater.inflate(R.layout.add_expense_layout,null);
        builder.setTitle("Add new Expense");

        etEventExpense = (EditText) vw.findViewById(R.id.etExpenseAmount);
        etEventComment = (EditText) vw.findViewById(R.id.etComment);

        builder.setView(vw);
        //final AlertDialog alertDialog = builder.create();

        builder.setNegativeButton("Cancel",null);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Toast.makeText(EventDetailsActivity.this, "Ooook", Toast.LENGTH_SHORT).show();
               ValidateExpense(eventName);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void ValidateExpense(String event_name) {

        String eventExpense = etEventExpense.getText().toString();
        String eventComment = etEventComment.getText().toString();

        if(TextUtils.isEmpty(eventExpense))
        {
           // Toast.makeText(getActivity(), "Please write your Event...", Toast.LENGTH_SHORT).show();
            etEventExpense.setError("Fill this field !!");
        }
        else if(TextUtils.isEmpty(eventComment))
        {
            // Toast.makeText(getActivity(), "Please write your Event...", Toast.LENGTH_SHORT).show();
            etEventExpense.setError("Fill this field !!");
        }else {

            Calendar calFordDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            final String saveCurrentDate = currentDate.format(calFordDate.getTime());

            Calendar calFordTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            final String saveCurrentTime = currentTime.format(calFordTime.getTime());

            final String RandomKey = current_user_id + saveCurrentDate + saveCurrentTime;

            HashMap expenseMap = new HashMap();
            expenseMap.put("uid",current_user_id);
            expenseMap.put("currentDate",saveCurrentDate);
            expenseMap.put("currentTime",saveCurrentTime);
            expenseMap.put("eventName",event_name);
            expenseMap.put("eventExpense",eventExpense);
            expenseMap.put("expenseComment",eventComment);

            expenseRef.child(RandomKey).updateChildren(expenseMap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(EventDetailsActivity.this, "Expense Successfully Added", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(EventDetailsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.itemHome)
        {
            startActivity(new Intent(EventDetailsActivity.this,MainActivity.class));
        }
        //noinspection SimplifiableIfStatement
        else if (id == R.id.itemLogout) {
            mAuth.signOut();
            SendUserToLoginActivity();
        }


        return super.onOptionsItemSelected(item);
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(EventDetailsActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}
