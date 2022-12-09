package com.example.farmerverse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmerverse.R;
import com.example.farmerverse.model.WeatherRvModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRvAdapter extends RecyclerView.Adapter<WeatherRvAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WeatherRvModel> weatherRvModelArrayList;

    public WeatherRvAdapter(Context context, ArrayList<WeatherRvModel> weatherRvModelArrayList)
    {
        this.context = context;
        this.weatherRvModelArrayList = weatherRvModelArrayList;
    }

    @NonNull
    @Override
    public WeatherRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRvAdapter.ViewHolder holder, int position)
    {
        WeatherRvModel model = weatherRvModelArrayList.get(position);
        Picasso.get().load("https:".concat(model.getIcon())).into(holder.ivCondition);
        holder.txtTemperature.setText(model.getTemperature() + "°C");
        holder.txtWindSpeed.setText(model.getWindSpeed() + "Km/h");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        try {
            Date d = input.parse(model.getTime());
            holder.txtTime.setText(output.format(d));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount()
    {
        return weatherRvModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTime, txtTemperature, txtWindSpeed;
        private ImageView ivCondition;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtTemperature = itemView.findViewById(R.id.txtTemperature);
            txtWindSpeed = itemView.findViewById(R.id.txtWindSpeed);
            ivCondition = itemView.findViewById(R.id.ivCondition);
        }
    }
}
