package com.example.farmerverse.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.farmerverse.R;
import com.example.farmerverse.entities.Seed;
import com.example.farmerverse.repository.viewmodel.FarmerverseViewModel;
import com.google.gson.Gson;

/**
 * This is the fragment where a user will be able to add a {@link Seed} to the {@link com.example.farmerverse.database.FarmerverseRoomDatabase}
 */
public class AddSeedFragment extends Fragment {
    public static final String EXTRA_REPLY = "com.example.android.seedlistsql.REPLY";
    private EditText seedName;
    private EditText spaceBetween;
    private EditText quantity;
    private EditText growthTime;
    private EditText weightPerSeed;
    private double spaceBetweenDouble;
    private double quantityDouble;
    private int growthTimeInt;
    private double weightPerSeedDouble;
    private FarmerverseViewModel farmerverseViewModel;
    private NavController navController;


    public AddSeedFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddSeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddSeedFragment newInstance(String param1, String param2)
    {
        AddSeedFragment fragment = new AddSeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called when the fragment gets created
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return Fragment View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_seed, container, false);


        //populate the ViewModel
        farmerverseViewModel = new ViewModelProvider(getActivity()).get(FarmerverseViewModel.class);

        //Get the NavController
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();

        //Get all XML the elements on the page
        seedName = view.findViewById(R.id.editTxtSeedName);
        spaceBetween = view.findViewById(R.id.editTxtSpaceBetween);
        quantity = view.findViewById(R.id.editTxtQuantity);
        growthTime = view.findViewById(R.id.editTxtGrowthTime);
        weightPerSeed = view.findViewById(R.id.editTxtWeightPerSeed);

        final Button submitBtn = view.findViewById(R.id.btnSubmit);
        final Button cancelBtn = view.findViewById(R.id.btnCancel);

        //Used to Jsonify objects into strings so that they can get passed around if needed
        Gson gson = new Gson();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                navController.popBackStack();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (isFormValid()) {
                    addSeedToDb();
                    navController.popBackStack();
                } else {
                    Toast.makeText(getContext(), "Make sure the form is filled in correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    /**
     * Adds a seed to the database by using the viewmodel created in onCreateView
     */
    private void addSeedToDb()
    {
        Seed seed = new Seed(seedName.getText().toString(), spaceBetweenDouble, quantityDouble, growthTimeInt, weightPerSeedDouble);
        farmerverseViewModel.insertSeed(seed);
    }

    /**
     * Checks to see if the form is filled in properly
     *
     * @return True if filled in right, false otherwise
     */
    private boolean isFormValid()
    {
        try {
            spaceBetweenDouble = Double.parseDouble(spaceBetween.getText().toString());
            quantityDouble = Double.parseDouble(quantity.getText().toString());
            growthTimeInt = Integer.parseInt(growthTime.getText().toString());
            weightPerSeedDouble = Double.parseDouble(weightPerSeed.getText().toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}