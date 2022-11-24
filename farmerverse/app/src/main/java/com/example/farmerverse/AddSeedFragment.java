package com.example.farmerverse;

import android.content.Intent;
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

import com.example.farmerverse.entities.Seed;
import com.example.farmerverse.viewmodel.FarmerverseViewModel;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddSeedFragment#newInstance} factory method to
 * create an instance of this fragment.
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


    public AddSeedFragment() {
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
    public static AddSeedFragment newInstance(String param1, String param2) {
        AddSeedFragment fragment = new AddSeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_seed, container, false);


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

        Gson gson = new Gson();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFormValid()){
                    addSeedToDb();
                    navController.popBackStack();
                }
                else{
                    Toast.makeText(getContext(), "Make sure the form is filled in correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void addSeedToDb() {
        Seed seed = new Seed(seedName.getText().toString(), spaceBetweenDouble, quantityDouble, growthTimeInt , weightPerSeedDouble);
        farmerverseViewModel.insertSeed(seed);
    }

    private boolean isFormValid(){
        try{
            spaceBetweenDouble = Double.parseDouble(spaceBetween.getText().toString());
            quantityDouble = Double.parseDouble(quantity.getText().toString());
            growthTimeInt = Integer.parseInt(growthTime.getText().toString());
            weightPerSeedDouble = Double.parseDouble(weightPerSeed.getText().toString());
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}