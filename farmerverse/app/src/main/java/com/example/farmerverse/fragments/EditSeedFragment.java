package com.example.farmerverse.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
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
import com.example.farmerverse.viewmodel.FarmerverseViewModel;
import com.google.gson.Gson;

public class EditSeedFragment extends Fragment {
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
    private Integer seedId;

    public EditSeedFragment()
    {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_add_seed, container, false);

        Bundle args = getArguments();
        seedId = args.getInt("seedId", -1);
        if (seedId == -1)
            Toast.makeText(getContext(), "Something Went Wrong...", Toast.LENGTH_SHORT).show();


        farmerverseViewModel = new ViewModelProvider(getActivity()).get(FarmerverseViewModel.class);
        //NavController
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();

        seedName = view.findViewById(R.id.editTxtSeedName);
        spaceBetween = view.findViewById(R.id.editTxtSpaceBetween);
        quantity = view.findViewById(R.id.editTxtQuantity);
        growthTime = view.findViewById(R.id.editTxtGrowthTime);
        weightPerSeed = view.findViewById(R.id.editTxtWeightPerSeed);

        final Button submitBtn = view.findViewById(R.id.btnSubmit);
        final Button cancelBtn = view.findViewById(R.id.btnCancel);
        if (seedId >= 1)
            populateForm();

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
                    updateDb();
                    navController.popBackStack();
                } else {
                    Toast.makeText(getContext(), "Make sure the form is filled in correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void populateForm()
    {
        Seed seed = farmerverseViewModel.getSeed(seedId);
        seedName.setText(seed.getName());
        spaceBetween.setText(String.format("%.2f", seed.getSpaceBetween()));
        quantity.setText(String.format("%.2f", seed.getQuantity()));
        growthTime.setText(String.format("%s", seed.getGrowthTimeInDays()));
        weightPerSeed.setText(String.format("%.2f", seed.getWeightPerSeed()));
    }

    private void updateDb()
    {
        Seed s = farmerverseViewModel.getSeed(seedId);
        s.setName(seedName.getText().toString());
        s.setSpaceBetween(spaceBetweenDouble);
        s.setQuantity(quantityDouble);
        s.setGrowthTimeInDays(growthTimeInt);
        s.setWeightPerSeed(weightPerSeedDouble);
        farmerverseViewModel.updateSeed(s);
    }

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