package com.example.musa.tourmate;


import com.example.musa.tourmate.Current_Weather.CurrentWeatherData;
import com.example.musa.tourmate.Forecast_Weather.ForecastWeatherData;
import com.example.musa.tourmate.GetLocation.GeoCode;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Musa on 5/8/2018.
 */

public interface Api {
    String BASE_URL ="http://api.openweathermap.org/data/2.5/";

    @GET("")
    Call<CurrentWeatherData> getAllWeather(@Url String url);

    @GET("")
    Call<ForecastWeatherData> getAllForecast(@Url String url);

   @GET("")
    Call<GeoCode> getAllLatLon(@Url String url);



}
