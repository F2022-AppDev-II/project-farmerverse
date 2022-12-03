package com.example.farmerverse.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmerverse.R;
import com.example.farmerverse.entities.Seed;
import com.example.farmerverse.model.Product;


class ProductViewHolder extends RecyclerView.ViewHolder{

    final TextView name;
    final TextView qty;
    final TextView price;

    public ProductViewHolder( View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.txtItemName);
        price = itemView.findViewById(R.id.txtItemPrice);
        qty = itemView.findViewById(R.id.txtItemQty);
    }
    public void bind(String _name, int _qty, double _price)
    {
        name.setText( _name);
        qty.setText("Quantity: "+_qty);
        price.setText(String.format("Price: $ %,.2f", _price));
    }

    static ProductViewHolder create(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.market_item, parent, false);
        return new ProductViewHolder(view);
    }
}



public class ProductListAdapter extends ListAdapter<Product, ProductViewHolder> {

    protected ProductListAdapter(@NonNull DiffUtil.ItemCallback<Product> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ProductViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product current = getItem(position);
        Bundle bundle = new Bundle();
        bundle.putInt("prodId", current.getId());
        holder.bind(current.getName(), current.getQuantity(), current.getPrice());

    }

    static class ProductDiff extends DiffUtil.ItemCallback<Product>{

        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId() == newItem.getId();
        }
    }
}

