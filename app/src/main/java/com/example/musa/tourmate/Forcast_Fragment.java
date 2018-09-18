package com.example.musa.tourmate;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musa.tourmate.Adapter.ForecastAdapter;
import com.example.musa.tourmate.Forecast_Weather.F_List;
import com.example.musa.tourmate.Forecast_Weather.ForecastWeatherData;
import com.example.musa.tourmate.GetLocation.GeoCode;
import com.example.musa.tourmate.GetLocation.Result;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class Forcast_Fragment extends Fragment {

   // double latitude = 22.33,longitude=91.84;
    String city = "";
    String url;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    private FusedLocationProviderClient client;
    private double latitude=0, longitude=0;
    private  double lat,lon;
    boolean convert;


    public Forcast_Fragment() {
        // Required empty public constructor
    }
    public static Forcast_Fragment newInstance(String city, boolean convert) {
        Forcast_Fragment result = new Forcast_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("CITY", city);
        bundle.putBoolean("CONVERT",convert);
        result.setArguments(bundle);
        return result;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forcast, container, false);
        city = getArguments().getString("CITY");
        convert = getArguments().getBoolean("CONVERT");

        recyclerView = (RecyclerView) v.findViewById(R.id.forecastList);

        client = LocationServices.getFusedLocationProviderClient(getActivity());
        getDeviceCurrentLocation();
        return v;
    }
    private boolean checkLocationPermission(){
        if(ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},111);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getDeviceCurrentLocation();
        }
    }

    public void getDeviceCurrentLocation() {

        if(checkLocationPermission()){
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location == null){
                        return;
                    }
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    Log.e("lat : ",latitude+"",null);
                    Log.e("lon : ",longitude+"",null);


                    if (city == ""){
                        callAPI(latitude,longitude);
                    }else {
                        searchLocation();
                    }

                }
            });
        }
    }

    public void callAPI(double lat,double lon)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Api api = retrofit.create(Api.class);
        //String url = String.format("weather?lat=22.33&lon=91.84&appid=9f51149abc3d8adac7a9103e8da92927",latitude,longitude);
        /*String url = String.format("forecast/daily?lat=%f&lon=%f&cnt=10&appid=9d1d5f43fe2b7a4a75bbc54e376f6b97",lat,lon);*/

        if(convert){
            url = String.format("forecast/daily?lat=%f&lon=%f&cnt=10&units=imperial&appid=9d1d5f43fe2b7a4a75bbc54e376f6b97",lat,lon);
        }else {
            url = String.format("forecast/daily?lat=%f&lon=%f&cnt=10&units=metric&appid=9d1d5f43fe2b7a4a75bbc54e376f6b97",lat,lon);
        }

        Call<ForecastWeatherData> call = api.getAllForecast(url);
        call.enqueue(new Callback<ForecastWeatherData>() {
            @Override
            public void onResponse(Call<ForecastWeatherData> call, Response<ForecastWeatherData> response) {

                ForecastWeatherData forecast_data = response.body();
                List<F_List> f_lists = forecast_data.getList();



                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                ForecastAdapter recyclerViewAdapter = new ForecastAdapter(getActivity(),forecast_data.getList(),convert);

                recyclerView.setAdapter(recyclerViewAdapter);

            }

            @Override
            public void onFailure(Call<ForecastWeatherData> call, Throwable t) {

            }
        });

    }

    public void searchLocation()
    {
        Retrofit retroCity = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/geocode/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Api apiCity = retroCity.create(Api.class);
        //String url ="json?address=Nurjahan+Road,+Mohammadpur,+Dhaka&key=AIzaSyCy4IOHbNB0he6ASe5XqixIF4Fr0ezM8aI";
        String url = String.format("json?address=%s&key=AIzaSyCy4IOHbNB0he6ASe5XqixIF4Fr0ezM8aI", city);
        Call<GeoCode> call = apiCity.getAllLatLon(url);

        call.enqueue(new Callback<GeoCode>() {
            @Override
            public void onResponse(Call<GeoCode> call, Response<GeoCode> response) {
                GeoCode geoCode = response.body();

                List<Result> results = geoCode.getResults();
                for(Result r:  results){
                    lat = r.getGeometry().getLocation().getLat();
                    lon = r.getGeometry().getLocation().getLng();
                }

                callAPI(lat,lon);
            }

            @Override
            public void onFailure(Call<GeoCode> call, Throwable t) {

            }
        });
    }

}
