package com.example.farmerverse.fragments.Market;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.farmerverse.R;
import com.example.farmerverse.model.Product;
import com.example.farmerverse.repository.IProductRepository;
import com.example.farmerverse.repository.viewmodel.MyProducts;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProductFragment extends Fragment {

    String name;
    String price;
    String qty;
    public static MyProducts products;

    private NavController navController;
    View view;

    public AddProductFragment() {}


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

    private void inputReferences() {
        name = ((EditText) getActivity().findViewById(R.id.prod_name_hint)).getText().toString();
        qty = ((EditText) getActivity().findViewById(R.id.prod_qty_hint)).getText().toString();
        price = ((EditText) getActivity().findViewById(R.id.prod_price_hint)).getText().toString();
    }

    private void args() {
        // Inject products list passed through args
        if (!getArguments().isEmpty()) {
            products = AddProductFragmentArgs.fromBundle(getArguments()).getArgsProductsViewModel();
        }
    }

    private void btnListeners() {
        view.findViewById(R.id.btn_done).setOnClickListener(view1 -> checkData());
    }

    private void checkData() {
        inputReferences();
        addIfValid();
    }

    private void addIfValid() {
        if(validateInput()){
            products.add(new Product( products.getAll().size(), name, Integer.parseInt(qty), Double.parseDouble(price) ));
            ((EditText) getActivity().findViewById(R.id.prod_name_hint)).setText("");
            ((EditText) getActivity().findViewById(R.id.prod_qty_hint)).setText("");
            ((EditText) getActivity().findViewById(R.id.prod_price_hint)).setText("");
            Snackbar.make(getView(), "Product added successfully!", Snackbar.LENGTH_SHORT).show();
        }

    }

    // Check for empty fields, if any prompt the user, otherwise return true
    private boolean validateInput() {

        StringBuilder stringBuilder = new StringBuilder();
        String error = "The following fields are missing:\n";

        if (name.equals(""))
            stringBuilder.append("Name\n");
        if (qty.equals(""))
            stringBuilder.append("Quantity\n");
        if (price.equals(""))
            stringBuilder.append("Price\n");

        if (!stringBuilder.toString().equals("")){
            Snackbar snackbar = Snackbar.make(getView(), error+ stringBuilder, Snackbar.LENGTH_LONG);
            View snackView = snackbar.getView();
            TextView snackTextView =  snackView.findViewById(com.google.android.material.R.id.snackbar_text);

            snackTextView.setMaxLines(8);
            snackbar.show();
            return false;
        }
            return true;
    }

    private void navigation( ) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();
        view.findViewById(R.id.go_market).setOnClickListener(view1 -> goToMarket());
    }

    private void goToMarket() {
        AddProductFragmentDirections.ActionAddProdToMarket dir = AddProductFragmentDirections.actionAddProdToMarket(products);

        navController.navigate(dir);
    }
}