package com.example.musa.tourmate.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musa.tourmate.Authentication.LoginActivity;
import com.example.musa.tourmate.Pojo_Class.Expense;
import com.example.musa.tourmate.Pojo_Class.Moments;
import com.example.musa.tourmate.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ViewAllImageActivity extends AppCompatActivity {

    private RecyclerView imageList;
    FirebaseAuth mAuth;
    DatabaseReference imageRef;

    String eventKey;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_image);

        eventKey = getIntent().getExtras().get("eventKey").toString();
        
        mAuth = FirebaseAuth.getInstance();
        imageRef = FirebaseDatabase.getInstance().getReference().child("Users").child(eventKey).child("Moments");

        imageList = (RecyclerView) findViewById(R.id.recyclerview);
        imageList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        imageList.setLayoutManager(linearLayoutManager);

        DisplayAllImage();
    }

    private void DisplayAllImage() {

        FirebaseRecyclerAdapter<Moments,ImageViewHolder> firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<Moments,ImageViewHolder>
                (
                        Moments.class,
                        R.layout.image_view,
                        ImageViewHolder.class,
                        imageRef
                )

        {
            @Override
            protected void populateViewHolder(ImageViewHolder viewHolder, Moments model, int position) {
                    try {
                        Bitmap bitmap = decodeFromFirebaseBase64(model.getImage());
                        viewHolder.imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        };
        imageList.setAdapter(firebaseRecyclerAdapter);

    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Context context;
        ImageView imageView;


        public ImageViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            imageView = (ImageView) mView.findViewById(R.id.imageView);
        }
       /* public void setImage(String image)
        {
            ImageView imageView = (ImageView) mView.findViewById(R.id.imageView);

           *//* Picasso.with()
                    .load()
                    .resize(MAX_WIDTH, MAX_HEIGHT)
                    .centerCrop()
                    .into(mRestaurantImageView);*//*
           Picasso.with(context)
                   .load()
        }*/


    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
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
            startActivity(new Intent(ViewAllImageActivity.this,MainActivity.class));
        }
        //noinspection SimplifiableIfStatement
        else if (id == R.id.itemLogout) {
            mAuth.signOut();
            SendUserToLoginActivity();
        }


        return super.onOptionsItemSelected(item);
    }
    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(ViewAllImageActivity.this, LoginActivity.class);
        //loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        //finish();
    }
}
