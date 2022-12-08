package com.example.farmerverse.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.farmerverse.R;
import com.example.farmerverse.entities.Seed;
import com.example.farmerverse.viewmodel.FarmerverseViewModel;

import java.util.List;

/**
 * A fragment representing a list of {@link Seed}.
 */
public class SeedFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private FarmerverseViewModel farmerverseViewModel;

    private NavController navController;

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


        //ViewModel
        farmerverseViewModel = new ViewModelProvider(getActivity()).get(FarmerverseViewModel.class);

        farmerverseViewModel.getAllSeeds().observe(getActivity(), new Observer<List<Seed>>() {
            @Override
            public void onChanged(List<Seed> seeds)
            {
                //RecyclerView
                RecyclerView recyclerView = view.findViewById(R.id.list);
                final SeedListAdapter adapter = new SeedListAdapter((new SeedListAdapter.SeedDiff()));
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

                //Swipe to delete
                ItemTouchHelper helper = new ItemTouchHelper(
                        new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                            @Override
                            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                                return false;
                            }

                            @Override
                            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                                int position = viewHolder.getBindingAdapterPosition();
                                Seed seed = adapter.getCurrentList().get(position);
                                farmerverseViewModel.deleteSeed(seed.getId());
                            }
                        }
                );
                helper.attachToRecyclerView(recyclerView);

                adapter.submitList(seeds);
            }


        });
//        farmerverseViewModel.getAllSeeds().observe(getActivity(), seeds -> {
//            adapter.submitList(seeds);
//        });

        //NavController
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();

        //OnClick
        view.findViewById(R.id.fabAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                navController.navigate(R.id.action_seedFragment_to_addSeedFragment);
            }
        });


//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallBack);
//        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }


//    ItemTouchHelper.SimpleCallback swipeCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//        @Override
//        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            int position = viewHolder.getBindingAdapterPosition();
//            Seed seed = farmerverseVi
//        }
//    };


    public Seed getSeedAtPosition(int position){
        return farmerverseViewModel.getSeed(position);
    }




}