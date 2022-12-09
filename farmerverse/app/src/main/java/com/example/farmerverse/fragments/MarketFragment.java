package com.example.farmerverse.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farmerverse.R;
import com.example.farmerverse.adapters.ProductListAdapter;
import com.example.farmerverse.viewmodel.MyProducts;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MarketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MarketFragment extends Fragment {

    public static MyProducts products;
    private NavController navController;

    public MarketFragment()
    {
        // Required empty public constructor
    }


    public static MarketFragment newInstance()
    {
        MarketFragment fragment = new MarketFragment();
        Bundle args = new Bundle();
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

        View view = inflater.inflate(R.layout.fragment_market_item_list, container, false);
        Context context = view.getContext();

        // Loads default data
        products = new MyProducts();


        // List Adapter
        final ProductListAdapter adapter = new ProductListAdapter((new ProductListAdapter.ProductDiff()));
        adapter.submitList(products.getAll());

        // Recycler View
        RecyclerView recyclerView = view.findViewById(R.id.market_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //NavController
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();

        return view;
    }
}