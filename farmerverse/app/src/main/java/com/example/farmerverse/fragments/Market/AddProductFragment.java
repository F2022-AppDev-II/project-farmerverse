package com.example.farmerverse.fragments.Market;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farmerverse.R;
import com.example.farmerverse.repository.IProductRepository;
import com.example.farmerverse.repository.viewmodel.MyProducts;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProductFragment extends Fragment {

    public static IProductRepository products;

    private NavController navController;
    View view;

    public AddProductFragment() {
        // Required empty public constructor
    }


    public static AddProductFragment newInstance() {
        AddProductFragment fragment = new AddProductFragment();
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
        
        args();
        
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_product, container, false);

        navigation();

        btnListeners();


        return view;
    }

    private void args() {
//        if (!getArguments().isEmpty()) {
//            AddProductFragmentArgs a = AddProductFragmentArgs.fromBundle(getArguments());
//            products = a.getCurrentProductsList();
//            int p;
//        }
    }

    private void btnListeners() {
        view.findViewById(R.id.btn_done).setOnClickListener(view1 -> navController.navigate(R.id.action_marketFrag_to_addProdFrag));

    }

    private void navigation( ) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();

        view.findViewById(R.id.go_home).setOnClickListener(view1 -> navController.navigate(R.id.action_addProd_to_Home));
        view.findViewById(R.id.go_market).setOnClickListener(view1 -> navController.navigate(R.id.action_addProd_to_Market));
    }
}