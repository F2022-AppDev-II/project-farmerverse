package com.example.farmerverse.fragments.Market;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.farmerverse.model.Product;
import com.example.farmerverse.repository.viewmodel.MyProducts;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MarketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MarketFragment extends Fragment {

    public static MyProducts products;
    private NavController navController;

    View view;

    ProductListAdapter adapter;

    public MarketFragment() {
        // Required empty public constructor
    }


    public static MarketFragment newInstance() {
        MarketFragment fragment = new MarketFragment();
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

        view = inflater.inflate(R.layout.fragment_market_item_list, container, false);
        Context context = view.getContext();

        // Loads default data
        products = new MyProducts();


        // List Adapter
        adapter = new ProductListAdapter((new ProductListAdapter.ProductDiff()));
        adapter.submitList(products.getAll());

        // Recycler View
        RecyclerView recyclerView = view.findViewById(R.id.market_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        navigation();

        btnListeners();

        return view;
    }

    private void navigation() {
        //NavController
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();

    }

    private void btnListeners() {
        view.findViewById(R.id.btn_market_add).setOnClickListener(view1 -> goToAddFragment());
    }

    private void goToAddFragment() {
        // Safe Args passing prods repository to add fragment
//        MarketFragmentDirections.ActionMarketFragToAddProdFrag direction = MarketFragmentDirections.actionMarketFragToAddProdFrag(products);
//        navController.navigate(R.id.action_marketFrag_to_addProdFrag);
    }

    private void test() {

        ArrayList<Product> b = new ArrayList<>(products.getAll());

        products.add(new Product( products.getById(products.getAll().size()-1).getId(), "New" ,12,7.7 ));

        adapter.notifyDataSetChanged();
    }

}