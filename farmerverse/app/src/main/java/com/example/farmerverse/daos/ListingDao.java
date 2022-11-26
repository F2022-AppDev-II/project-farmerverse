package com.example.farmerverse.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.farmerverse.entities.Listing;

import java.util.List;

@Dao
public interface ListingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Listing listing);

    @Query("DELETE FROM listing_table")
    void deleteAll();

    @Query("SELECT * FROM listing_table")
    LiveData<List<Listing>> getAllListings();

    @Query("SELECT * FROM listing_table WHERE id = :id")
    LiveData<Listing> getListing(int id);

    @Query("DELETE FROM listing_table WHERE id = :id")
    void deleteListing(int id);

    @Update
    void updateListing(Listing listing);

    @Query("UPDATE listing_table SET quantity = quantity-:amount WHERE id=:id")
    void removeQuantity(double amount, int id);

    @Query("UPDATE listing_table SET quantity = quantity+:amount WHERE id=:id")
    void addQuantity(double amount, int id);
}
