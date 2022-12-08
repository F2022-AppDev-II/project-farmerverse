package com.example.farmerverse.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.farmerverse.R;
import com.example.farmerverse.entities.Seed;
import com.example.farmerverse.helpers.StringWithId;
import com.example.farmerverse.viewmodel.FarmerverseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Calculator#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Calculator extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText editTextSquareMeters;
    private Spinner seedSpinner;
    private EditText editTextDistanceBetweenTwoSeeds;
    private TextView txtSeedsNeeded;
    private TextView txtPerSquareMeter;
    private RadioButton rbManuallyAddSeed;
    private FarmerverseViewModel farmerverseViewModel;
    private NavController navController;
    private double squareMeters;
    private double spacing;
    private Seed seedToCalculateOn;
    private int currentSelectedSeedId = -1;
    private double AVERAGE_ROW_SPACING_CM = 20 * 2.54;

    public Calculator()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment calculator.
     */
    // TODO: Rename and change types and number of parameters
    public static Calculator newInstance(String param1, String param2)
    {
        Calculator fragment = new Calculator();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);


        //Get the ViewModel
        farmerverseViewModel = new ViewModelProvider(getActivity()).get(FarmerverseViewModel.class);

        //Get the NavController
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();

        //Get XML items
        editTextSquareMeters = view.findViewById(R.id.editTxtSquareMeters);
        seedSpinner = view.findViewById(R.id.spinner);
        editTextDistanceBetweenTwoSeeds = view.findViewById(R.id.editTextDistanceBetweenTwoSeeds);
        txtSeedsNeeded = view.findViewById(R.id.txtSeedsNeeded);
        txtPerSquareMeter = view.findViewById(R.id.txtSeedsPerSquareMeter);

        //add items to spinner from DB

        LiveData<List<Seed>> seeds;
        try {
            seeds = farmerverseViewModel.getAllSeeds();
            Context context = getContext();
            //Have to observe because it is done async and might be null sp onChanged gets called when it is no longer null
            seeds.observe(getActivity(), new Observer<List<Seed>>() {
                @Override
                public void onChanged(List<Seed> seeds)
                {
                    //Now seeds is not null, if the db was changed somehow during lifetime of this fragment,
                    //the spinner will also update
                    List<StringWithId> spinnerSeedList = new ArrayList<>();
                    for (Seed seed : seeds
                    ) {
                        spinnerSeedList.add(new StringWithId(seed.getName(), seed.getId()));
                    }
                    //set spinner adapter
                    ArrayAdapter<StringWithId> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerSeedList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    seedSpinner.setAdapter(adapter);
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong populating the spinner", Toast.LENGTH_SHORT).show();
        }


        //SET LISTENERS
        seedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                StringWithId s = (StringWithId) parent.getItemAtPosition(position);
                currentSelectedSeedId = s.getId();
                //Now we can set the other data with the Id of the Seed

                //set the space between the seeds based on spinner selection
                Seed seed = farmerverseViewModel.getSeed(currentSelectedSeedId);
                editTextDistanceBetweenTwoSeeds.setText(String.format("%s", seed.getSpaceBetween()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //don't need
            }
        });

        view.findViewById(R.id.btnCalculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (isFormFilledIn()) {
                    ArrayList<Double> calculated = calculate();
                    txtSeedsNeeded.setText(String.format("~ %d", calculated.get(0).intValue()));
                    txtPerSquareMeter.setText(String.format("~ %d", calculated.get(1).intValue()));
                } else {
                    Toast.makeText(getContext(), "Make sure form is fully filled in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private ArrayList<Double> calculate()
    {
        //assuming square for 1m^2
        Seed currentSeed = farmerverseViewModel.getSeed(currentSelectedSeedId);
        double distance = currentSeed.getSpaceBetween();
        double plantsPerRow = 100 / distance;
        double plantsPerColumn = 100 / distance;
        double plantsPerSquareMeter = plantsPerColumn * plantsPerRow;

        double numberOfPlants = plantsPerSquareMeter * squareMeters;


        ArrayList<Double> calculated = new ArrayList<>();
        calculated.add(numberOfPlants);
        calculated.add(plantsPerSquareMeter);


        return calculated;
    }

    private boolean isFormFilledIn()
    {
        try {
            squareMeters = Double.parseDouble(editTextSquareMeters.getText().toString());
            spacing = Double.parseDouble(editTextDistanceBetweenTwoSeeds.getText().toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    //TODO: Add Spinner on select Item to update the distance between 2 plants by querying database
}