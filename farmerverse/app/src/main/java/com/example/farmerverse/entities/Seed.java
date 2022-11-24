package com.example.farmerverse.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "seed_table")
public class Seed {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String name;
    private double spaceBetween;
    private double quantity;
    private int growthTimeInDays;
    private double weightPerSeed;

    public Seed(@NonNull String name, double spaceBetween, double quantity, int growthTimeInDays, double weightPerSeed)
    {
        this.name = name;
        this.spaceBetween = spaceBetween;
        this.quantity = quantity;
        this.growthTimeInDays = growthTimeInDays;
        this.weightPerSeed = weightPerSeed;
    }


    //region Getters And Setters
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getSpaceBetween()
    {
        return spaceBetween;
    }

    public void setSpaceBetween(double spaceBetween)
    {
        this.spaceBetween = spaceBetween;
    }

    public double getQuantity()
    {
        return quantity;
    }

    public void setQuantity(double quantity)
    {
        this.quantity = quantity;
    }

    public int getGrowthTimeInDays()
    {
        return growthTimeInDays;
    }

    public void setGrowthTimeInDays(int growthTimeInDays)
    {
        this.growthTimeInDays = growthTimeInDays;
    }

    public double getWeightPerSeed()
    {
        return weightPerSeed;
    }

    public void setWeightPerSeed(double weightPerSeed)
    {
        this.weightPerSeed = weightPerSeed;
    }
    //endregion
}
