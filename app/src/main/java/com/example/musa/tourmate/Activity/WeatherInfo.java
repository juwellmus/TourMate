package com.example.musa.tourmate.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.musa.tourmate.Adapter.PageAdapter;
import com.example.musa.tourmate.Authentication.LoginActivity;
import com.example.musa.tourmate.R;
import com.example.musa.tourmate.SharedPreference.UserPreference;
import com.google.firebase.auth.FirebaseAuth;

public class WeatherInfo extends AppCompatActivity {

    String city="";
    boolean convert = false;

    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapter adapter;

    UserPreference userPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);

        /*Intent intent = getIntent();
        if(intent.getAction().equals(Intent.ACTION_SEARCH)){
            city = intent.getStringExtra(SearchManager.QUERY);
        }*/

        userPreference = new UserPreference(this);


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Current"));
        tabLayout.addTab(tabLayout.newTab().setText("10 DAYS FORECAST"));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),city,userPreference.getTempUnit());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Do you want to Exit ?");
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel",null);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weather_menu,menu);

       /* SearchManager manager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.itemCelsius:
                userPreference.setTempUnit(true);
                adapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),city,userPreference.getTempUnit());
                viewPager.setAdapter(adapter);
                break;
            case R.id.itemFahrenheit:
                userPreference.setTempUnit(false);
                adapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),city,userPreference.getTempUnit());
                viewPager.setAdapter(adapter);
                break;
            case R.id.itemHome:
                startActivity(new Intent(WeatherInfo.this,MainActivity.class));
                break;
            case R.id.itemLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                userPreference.setLogInStatus(false);
                startActivity(new Intent(WeatherInfo.this,LoginActivity.class));

            case R.id.itemSearch:

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = LayoutInflater.from(this);
                View vw = inflater.inflate(R.layout.search_city,null);

                final EditText etCity = (EditText) vw.findViewById(R.id.etCity);
                Button btnSearchCity = (Button) vw.findViewById(R.id.btnSearchCity);
                builder.setView(vw);
                final AlertDialog alertDialog = builder.create();

                btnSearchCity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        city = etCity.getText().toString();

                        adapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),city,userPreference.getTempUnit());
                        viewPager.setAdapter(adapter);
                        alertDialog.dismiss();

                    }
                });

                alertDialog.show();

                break;


        }

        return super.onOptionsItemSelected(item);
    }

}
