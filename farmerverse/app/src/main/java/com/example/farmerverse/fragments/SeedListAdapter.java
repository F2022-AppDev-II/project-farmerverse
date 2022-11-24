package com.example.farmerverse.fragments;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.farmerverse.R;
import com.example.farmerverse.entities.Seed;

class SeedViewHolder extends RecyclerView.ViewHolder {
    private final TextView seedName;
    private final TextView daysToGrow;
    private final TextView quantityTextView;

    private SeedViewHolder(View itemView)
    {
        super(itemView);
        seedName = itemView.findViewById(R.id.txtSeedName);
        daysToGrow = itemView.findViewById(R.id.txtDaysToGrowDatabase);
        quantityTextView = itemView.findViewById(R.id.txtDatabaseQuantity);
    }

    public void bind(String text, int growthTime, double quantity)
    {
        seedName.setText(text);
        daysToGrow.setText(String.format("%s", growthTime));
        daysToGrow.setText(String.format("%,.2f", quantity));
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
        holder.bind(current.getName(), current.getGrowthTimeInDays(), current.getQuantity());
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