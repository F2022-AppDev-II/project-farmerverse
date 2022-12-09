package com.example.farmerverse.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.farmerverse.R;
import com.example.farmerverse.databinding.FragmentAlertDialogBinding;
import com.example.farmerverse.entities.Seed;
import com.example.farmerverse.repository.viewmodel.FarmerverseViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlertDialogFragment#} factory method to
 * create an instance of this fragment.
 */
public class AlertDialogFragment extends DialogFragment {


    private final int seedId;
    private final boolean isFromFocus;
    private FarmerverseViewModel farmerverseViewModel;
    private NavController navController;

    public AlertDialogFragment(int seedId, boolean isFromFocus)
    {
        this.seedId = seedId;
        this.isFromFocus = isFromFocus;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        FragmentAlertDialogBinding binding = FragmentAlertDialogBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        //Get the ViewModel
        farmerverseViewModel = new ViewModelProvider(getActivity()).get(FarmerverseViewModel.class);

        //Get the NavController
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();

        Seed seed = farmerverseViewModel.getSeed(seedId);

        binding.txtAboutToDelete.setText(String.format("You are about to delete the %s seed.\nThere is no going back!", seed.getName()));
        binding.btnCancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });
        binding.btnDeleteNoGoingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                farmerverseViewModel.deleteSeed(seedId);
                dismiss();
                if (isFromFocus)
                    navController.popBackStack();
            }
        });

        return view;
    }
}