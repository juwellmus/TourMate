package com.example.musa.tourmate.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musa.tourmate.Forecast_Weather.F_List;
import com.example.musa.tourmate.Forecast_Weather.Weather;
import com.example.musa.tourmate.R;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Musa on 5/11/2018.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {


    Context context;
    private List<F_List> forecasts;
    private List<Weather> weathers;
    boolean convert;
    String imageurl;
    ImageView imgWeather;


    public ForecastAdapter(Context context, List<F_List> forecasts,boolean convert) {
        this.context = context;
        this.forecasts = forecasts;
        this.convert = convert;
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.forecast_row,parent,false);
        return new ForecastViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");

        Date date = new Date(forecasts.get(position).getDt()*1000);
        String currentDate = dateFormat.format(date);
        String currentDay = dayFormat.format(date);

        holder.tvDate.setText(currentDate);
        holder.tvDay.setText(currentDay);

        weathers = forecasts.get(position).getWeather();
        for(Weather w: weathers)
        {
            imageurl= w.getIcon();
            holder.tvDescription.setText(String.valueOf(w.getDescription()));

        }

        String imgURL = String.format("https://openweathermap.org/img/w/%s.png",imageurl);
        Picasso.with(context).load(imgURL).resize(50,50).into(imgWeather);

        if(convert){
            double minTemp = forecasts.get(position).getTemp().getMin();
            holder.tvMin.setText(String.format(" %.2f",minTemp)+"° F ");
            double maxTemp = forecasts.get(position).getTemp().getMax();
            holder.tvMax.setText(String.format(" %.2f",maxTemp)+"° F ");
        }else {
            double minTemp = forecasts.get(position).getTemp().getMin();
            holder.tvMin.setText(String.format(" %.2f",minTemp)+"° C ");
            double maxTemp = forecasts.get(position).getTemp().getMax();
            holder.tvMax.setText(String.format(" %.2f",maxTemp)+"° C ");
        }



        holder.tvHumidity.setText(String.valueOf(forecasts.get(position).getHumidity())+"%");
        holder.tvPressure.setText(String.valueOf(forecasts.get(position).getPressure())+" hPa");
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder{

        TextView tvDate,tvDay,tvDescription,tvMin,tvMax,tvHumidity,tvPressure;
        //ImageView imgWeather;
        public ForecastViewHolder(View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            imgWeather = (ImageView) itemView.findViewById(R.id.imgView);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvMin = (TextView) itemView.findViewById(R.id.tvMin);
            tvMax = (TextView) itemView.findViewById(R.id.tvMax);
            tvHumidity = (TextView) itemView.findViewById(R.id.tvHumidity);
            tvPressure = (TextView) itemView.findViewById(R.id.tvPressure);

        }
    }
    
}
