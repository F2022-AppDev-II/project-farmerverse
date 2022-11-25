package com.example.farmerverse.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "listing_table")
public class Listing {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private double quantity;
    private double price;

    public Listing(String name, double quantity, double price)
    {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }


    //region Getters and Setters
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

    public double getQuantity()
    {
        return quantity;
    }

    public void setQuantity(double quantity)
    {
        this.quantity = quantity;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }
    //endregion
}
