package com.example.farmerverse.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.farmerverse.R;
import com.example.farmerverse.adapters.WeatherRvAdapter;
import com.example.farmerverse.model.WeatherRvModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FullWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullWeatherFragment extends Fragment {

    private RelativeLayout rlFullWeatherHome, rlHome;
    private ProgressBar pbLoading;
    private ShapeableImageView ivBackground;
    private TextView txtCityName, txtTemperature, txtCondition;
    private ImageView ivIcon;
    private RecyclerView rvWeather;
    private ArrayList<WeatherRvModel> weatherRvModelArrayList;
    private WeatherRvAdapter weatherRvAdapter;

    public FullWeatherFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FullWeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FullWeatherFragment newInstance(String param1, String param2)
    {
        FullWeatherFragment fragment = new FullWeatherFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_weather, container, false);

        rlFullWeatherHome = view.findViewById(R.id.rlFullWeatherRoot);
        rlHome = view.findViewById(R.id.rlHome);
        pbLoading = view.findViewById(R.id.pbLoading);
        ivBackground = view.findViewById(R.id.ivBackground);
        txtCityName = view.findViewById(R.id.txtCityName);
        txtTemperature = view.findViewById(R.id.txtTemperature);
        txtCondition = view.findViewById(R.id.txtCondition);
        ivIcon = view.findViewById(R.id.ivIcon);
        rvWeather = view.findViewById(R.id.rvWeather);
        weatherRvModelArrayList = new ArrayList<>();
        weatherRvAdapter = new WeatherRvAdapter(getContext(), weatherRvModelArrayList);
        rvWeather.setAdapter(weatherRvAdapter);

        getWeatherInfo("Montreal");

        return view;
    }


    private void getWeatherInfo(String cityName)
    {
        String url = "https://api.weatherapi.com/v1/forecast.json?key=e0b1e813972343d38a9213132220612 &q=Montreal&days=1&aqi=yes&alerts=yes";
        txtCityName.setText(cityName);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                pbLoading.setVisibility(View.GONE);
                rlHome.setVisibility(View.VISIBLE);
                rlFullWeatherHome.setBackground(getResources().getDrawable(R.drawable.transparent_rounded));
                rlFullWeatherHome.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    txtTemperature.setText(temperature + "°C");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String icon = response.getJSONObject("current").getJSONObject("condition").getString("icon");

                    Picasso.get().load("https:".concat(icon)).into(ivIcon);

                    txtCondition.setText(condition);

                    if (isDay == 1) {
                        //morning
                        Picasso.get().load("https://images.unsplash.com/photo-1566228015668-4c45dbc4e2f5?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2574&q=80").into(ivBackground);
                    } else {
                        Picasso.get().load("https://images.unsplash.com/photo-1527190997915-67ce3b53cc58?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=774&q=80").into(ivBackground);
                    }

                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecastDay = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hours = forecastDay.getJSONArray("hour");

                    for (int i = 0; i < hours.length(); i++) {
                        JSONObject hourObj = hours.getJSONObject(i);
                        String time = hourObj.getString("time");
                        String temp = hourObj.getString("temp_c");
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        String wind = hourObj.getString("wind_kph");
                        weatherRvModelArrayList.add(new WeatherRvModel(time, temp, img, wind));
                    }

                    weatherRvAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getContext(), "City Name Not Valid", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}