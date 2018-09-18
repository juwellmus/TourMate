package com.example.musa.tourmate.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.musa.tourmate.Authentication.LoginActivity;
import com.example.musa.tourmate.Pojo_Class.Expense;
import com.example.musa.tourmate.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExpenseListActivity extends AppCompatActivity {

    private RecyclerView expenseList;
    private TextView tvEventName;
    FirebaseAuth mAuth;
    DatabaseReference expenseRef;
    String eventKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);
        tvEventName = (TextView) findViewById(R.id.tvEventName);

        eventKey = getIntent().getExtras().get("eventKey").toString();
        mAuth = FirebaseAuth.getInstance();
        expenseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(eventKey).child("Expense");

        expenseList = (RecyclerView) findViewById(R.id.expenseList);
        expenseList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        expenseList.setLayoutManager(linearLayoutManager);

        DisplayAllExpense();

    }

    private void DisplayAllExpense() {

        FirebaseRecyclerAdapter<Expense,ExpenseViewHolder> firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<Expense, ExpenseViewHolder>
                (
                        Expense.class,
                        R.layout.expense_list_layout,
                        ExpenseViewHolder.class,
                        expenseRef
                )

        {
            @Override
            protected void populateViewHolder(ExpenseViewHolder viewHolder, Expense model, int position) {
                viewHolder.setCurrentDate(model.getCurrentDate());
                viewHolder.setExpenseComment(model.getExpenseComment());
                viewHolder.setEventExpense(model.getEventExpense());
            }
        };
        expenseList.setAdapter(firebaseRecyclerAdapter);

    }
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        View mView;


        public ExpenseViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setCurrentDate(String currentDate)
        {
            TextView tvDate = (TextView) mView.findViewById(R.id.tvStartDate);
            tvDate.setText(currentDate);
        }
        public void setExpenseComment(String expenseComment)
        {
            TextView tvCostName = (TextView) mView.findViewById(R.id.tvCostName);
            tvCostName.setText(expenseComment);
        }
        public void setEventExpense(String eventExpense)
        {
            TextView tvExpense = (TextView) mView.findViewById(R.id.tvCostAmount);
            tvExpense.setText(eventExpense+" Taka");
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
            startActivity(new Intent(ExpenseListActivity.this,MainActivity.class));
        }
        //noinspection SimplifiableIfStatement
        else if (id == R.id.itemLogout) {
            mAuth.signOut();
            SendUserToLoginActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(ExpenseListActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}
