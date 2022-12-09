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
import com.example.farmerverse.viewmodel.FarmerverseViewModel;
import com.google.gson.Gson;

/**
 * Fragment in which a user will be able to edit a {@link Seed} they clicked on
 * It will be auto populated with the data from the seed they clicked on
 */
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
        View view = inflater.inflate(R.layout.fragment_edit_seed, container, false);

        //Get the seedId in order to query the database to get the seeds info
        Bundle args = getArguments();
        seedId = args.getInt("seedId", -1);
        if (seedId == -1)
            Toast.makeText(getContext(), "Something Went Wrong, the Seed was not properly sent in...", Toast.LENGTH_SHORT).show();


        //Get the ViewModel
        farmerverseViewModel = new ViewModelProvider(getActivity()).get(FarmerverseViewModel.class);

        //Get the NavController
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();

        //Get the XML items
        seedName = view.findViewById(R.id.editTxtSeedName);
        spaceBetween = view.findViewById(R.id.editTxtSpaceBetween);
        quantity = view.findViewById(R.id.editTxtQuantity);
        growthTime = view.findViewById(R.id.editTxtGrowthTime);
        weightPerSeed = view.findViewById(R.id.editTxtWeightPerSeed);

        final Button submitBtn = view.findViewById(R.id.btnSubmit);
        final Button cancelBtn = view.findViewById(R.id.btnCancel);
        final Button deleteBtn = view.findViewById(R.id.btnDelete);
        if (seedId >= 1)
            populateForm();

        Gson gson = new Gson();

        //Set even listeners
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

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //are you sure screen
                AlertDialogFragment dialog = new AlertDialogFragment(seedId, true);
                dialog.show(supportFragmentManager, "Alert");
            }
        });

        return view;
    }

    /**
     * Populate the form with the seeds existing data from the database
     */
    private void populateForm()
    {
        Seed seed = farmerverseViewModel.getSeed(seedId);
        seedName.setText(seed.getName());
        spaceBetween.setText(String.format("%.2f", seed.getSpaceBetween()));
        quantity.setText(String.format("%.2f", seed.getQuantity()));
        growthTime.setText(String.format("%s", seed.getGrowthTimeInDays()));
        weightPerSeed.setText(String.format("%.2f", seed.getWeightPerSeed()));
    }

    /**
     * Update the Database with the edited information
     */
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

    /**
     * Checks whether the form is valid or not
     *
     * @return true if valid, false otherwise
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