package com.example.farmerverse.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.farmerverse.daos.ListingDao;
import com.example.farmerverse.database.FarmerverseRoomDatabase;
import com.example.farmerverse.entities.Listing;

import java.util.List;

public class ListingRepository {
    private ListingDao listingDao;
    private LiveData<List<Listing>> allListings;

    public ListingRepository(Application application)
    {
        FarmerverseRoomDatabase db = FarmerverseRoomDatabase.getDatabase(application);
        listingDao = db.listingDao();
        allListings = listingDao.getAllListings();
    }

    public LiveData<List<Listing>> getAllListings()
    {
        return allListings;
    }

    public void insert(Listing listing)
    {
        FarmerverseRoomDatabase.databaseWriteExecutor.execute(() -> {
            listingDao.insert(listing);
        });
    }
}
