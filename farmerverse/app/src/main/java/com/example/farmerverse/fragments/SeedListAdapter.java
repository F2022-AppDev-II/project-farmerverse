package com.example.farmerverse.fragments;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.farmerverse.R;
import com.example.farmerverse.entities.Seed;

class SeedViewHolder extends RecyclerView.ViewHolder {
    private final TextView seedName;
    private final TextView daysToGrow;
    private final TextView quantityTextView;
    private final Button editBtn;

    private SeedViewHolder(View itemView)
    {
        super(itemView);
        seedName = itemView.findViewById(R.id.txtSeedName);
        daysToGrow = itemView.findViewById(R.id.txtDaysToGrowDatabase);
        quantityTextView = itemView.findViewById(R.id.txtDatabaseQuantity);
        editBtn = itemView.findViewById(R.id.btnEdit);
    }

    public void bind(String text, int growthTime, double quantity)
    {
        seedName.setText(text);
        daysToGrow.setText(String.format("%s days", growthTime));
        quantityTextView.setText(String.format("%,.2f Kg", quantity));
    }

    static SeedViewHolder create(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seed_item, parent, false);
        return new SeedViewHolder(view);
    }

}

public class SeedListAdapter extends ListAdapter<Seed, SeedViewHolder> {

    public SeedListAdapter(@NonNull DiffUtil.ItemCallback<Seed> diffCallback)
    {
        super(diffCallback);
    }

    @NonNull
    @Override
    public SeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return SeedViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SeedViewHolder holder, int position)
    {
        Seed current = getItem(position);
        Bundle bundle = new Bundle();
        bundle.putInt("seedId", current.getId());
        holder.bind(current.getName(), current.getGrowthTimeInDays(), current.getQuantity());

        holder.itemView.findViewById(R.id.btnEdit).setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_seedFragment_to_editSeedFragment, bundle)
        );

    }

    static class SeedDiff extends DiffUtil.ItemCallback<Seed> {
        @Override
        public boolean areItemsTheSame(@NonNull Seed oldItem, @NonNull Seed newItem)
        {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Seed oldItem, @NonNull Seed newItem)
        {
            return oldItem.getId() == newItem.getId();
        }
    }
}