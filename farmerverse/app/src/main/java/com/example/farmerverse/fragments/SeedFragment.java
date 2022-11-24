package com.example.farmerverse.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.farmerverse.NewSeedActivity;
import com.example.farmerverse.R;
import com.example.farmerverse.entities.Seed;
import com.example.farmerverse.viewmodel.FarmerverseViewModel;
import com.google.gson.Gson;

/**
 * A fragment representing a list of Items.
 */
public class SeedFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private FarmerverseViewModel farmerverseViewModel;
    public static final int NEW_SEED_ACTIVITY_REQUEST_CODE = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SeedFragment()
    {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SeedFragment newInstance(int columnCount)
    {
        SeedFragment fragment = new SeedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_seed_item_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        final SeedListAdapter adapter = new SeedListAdapter((new SeedListAdapter.SeedDiff()));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        farmerverseViewModel = new ViewModelProvider(getActivity()).get(FarmerverseViewModel.class);
        farmerverseViewModel.getAllSeeds().observe(getActivity(), seeds -> {
            adapter.submitList(seeds);
        });


        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Gson gson = new Gson();
        if (requestCode == NEW_SEED_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Seed seed = gson.fromJson(data.getStringExtra(NewSeedActivity.EXTRA_REPLY), Seed.class);
            farmerverseViewModel.insertSeed(seed);
        } else {
            Toast.makeText(getContext(), "Something Went Wrong, Seed wasn't added", Toast.LENGTH_LONG).show();
        }
    }
}