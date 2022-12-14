package com.example.farmerverse.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherWidgetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherWidgetFragment extends Fragment {

    final static String DEFAULT_CITY_NAME = "Montreal";
    private RelativeLayout homeRl, rlWeatherRoot;
    private ProgressBar loadingProgressBar;
    private TextView txtCityName, txtCondition, txtTemperature;
    private RecyclerView rvWeather;
    private ImageView ivIcon, ivBackground;
    private int PERMISSION_CODE = 1;
    private LocationManager locationManager;
    private String cityName;
    private NavHostFragment navHostFragment;
    private NavController navController;

    public WeatherWidgetFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherWidgetFragment newInstance(String param1, String param2)
    {
        WeatherWidgetFragment fragment = new WeatherWidgetFragment();
        return fragment;
    }

    static int getRequstCode()
    {
        return 1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        loadingProgressBar = view.findViewById(R.id.pbLoading);
        txtCondition = view.findViewById(R.id.txtCondition);
        txtTemperature = view.findViewById(R.id.txtTemperature);
        ivIcon = view.findViewById(R.id.ivIcon);
        ivBackground = view.findViewById(R.id.ivBackground);
        txtCityName = view.findViewById(R.id.txtCityName);
        homeRl = view.findViewById(R.id.rlHome);
        rlWeatherRoot = view.findViewById(R.id.rlWeatherRoot);

        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        navHostFragment = (NavHostFragment) supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();

        Location l = getLastKnownLocation();

        // Small Patch for presentation -- Assigning Montreal as default location to retrieve weather from
        cityName = (l != null)? getCityName(l.getLongitude(), l.getLatitude()): DEFAULT_CITY_NAME;

        getWeatherInfo(cityName);

        return view;
    }

    private void requestPermission(String permissionName, int permissionRequestCode)
    {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{permissionName}, permissionRequestCode);
    }

    private Location getLastKnownLocation()
    {
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, PERMISSION_CODE);
            }
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_CODE);
            }
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                Log.d("LOCATION", String.format("Found best last known location: %s ", l));
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private String getCityName(double longitude, double latitude)
    {
        String cityName = "Not Found";
        Geocoder gcd = new Geocoder(getActivity().getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);

            for (Address address : addresses) {
                if (address != null) {
                    String city = address.getLocality();
                    if (city != null && !city.equals("")) {
                        cityName = city;
                    } else {
                        Log.d("TAG", "CITY NOT FOUND");
                        Toast.makeText(getContext(), "City Not Found...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cityName;
    }

    private void getWeatherInfo(String cityname)
    {
        String url = "https://api.weatherapi.com/v1/forecast.json?key=e0b1e813972343d38a9213132220612 &q=Montreal&days=1&aqi=yes&alerts=yes";
        txtCityName.setText(cityname);
        Context context = getContext();
        if (context == null) {
            context = navController.getContext();
        }


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        Context finalContext = context;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                loadingProgressBar.setVisibility(View.GONE);
                homeRl.setVisibility(View.VISIBLE);

                rlWeatherRoot.setBackground(finalContext.getResources().getDrawable(R.drawable.transparent_rounded));
                rlWeatherRoot.setBackgroundColor(finalContext.getResources().getColor(android.R.color.transparent));

                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    txtTemperature.setText(temperature + "??C");
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