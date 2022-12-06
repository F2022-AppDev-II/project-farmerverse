package com.example.farmerverse.fragments;

import static java.time.temporal.ChronoUnit.DAYS;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.farmerverse.R;
import com.example.farmerverse.databinding.FragmentHomeBinding;
import com.example.farmerverse.viewmodel.FarmerverseViewModel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;

/**
 * Our Home Page Fragment
 * This is where the user will land after opening the app
 * The MainActivity Activity defaults to this Fragment as its opening fragment
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private NavController navController;
    private NavHostFragment navHostFragment;
    private FarmerverseViewModel farmerverseViewModel;

    public HomeFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2)
    {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method is called when the fragment is navigated to from a different fragment
     * Sets the NavController as well as all the onClick listeners for the fragment
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        navHostFragment = (NavHostFragment) supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();


        Fragment child = new WeatherFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.weather, child).commit();

        view.findViewById(R.id.btnInventory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_homeFragment_to_seedFragment);
            }
        });
        view.findViewById(R.id.btnCalculator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_homeFragment_to_calculator);
            }
        });

        view.findViewById(R.id.btnCamera).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                navController.navigate(R.id.action_homeFragment_to_cameraFragment);
            }
        });
        setDaysUntilHarvest(view);

        view.findViewById(R.id.btnMarket).setOnClickListener(v -> navController.navigate(R.id.action_homeFragment_to_marketFragment));

        return view;
    }

    /**
     * Sets the TextView with the number of days until thanksgiving
     *
     * @param view The Fragment View
     */
    private void setDaysUntilHarvest(View view)
    {
        TextView days = view.findViewById(R.id.txtNumberOfDays);
        LocalDate thanksGiving;
        if (isPastThanksgiving()) {
            thanksGiving = getThanksgivingDate(Year.now().getValue() + 1);
        } else {
            thanksGiving = getThanksgivingDate(Year.now().getValue());
        }
        long daysBetween = DAYS.between(LocalDate.now(), thanksGiving);
        days.setText(String.format("%s Days", daysBetween));
    }

    /**
     * Gets the exact date of thanksgiving (US version) given a specific year
     * 4th Thursday in the month of November
     *
     * @param year The Year that the day is wanted for
     * @return LocalDate of thanksgiving
     */
    private static LocalDate getThanksgivingDate(int year)
    {
        LocalDate thanksGiving = Year.of(year).atMonth(Month.NOVEMBER).atDay(1)
                .with(TemporalAdjusters.dayOfWeekInMonth(4, DayOfWeek.THURSDAY));
        return thanksGiving;
    }


    /**
     * Checks if today is past thanksgiving for todays year
     *
     * @return True if Today is after thanksgiving, false otherwise
     */
    private boolean isPastThanksgiving()
    {
        return java.time.LocalDate.now().isAfter(getThanksgivingDate(Year.now().getValue()));
    }
}