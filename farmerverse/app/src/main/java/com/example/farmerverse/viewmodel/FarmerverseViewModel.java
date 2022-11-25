package com.example.farmerverse.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.farmerverse.entities.Listing;
import com.example.farmerverse.entities.Seed;
import com.example.farmerverse.repository.ListingRepository;
import com.example.farmerverse.repository.SeedRepository;

import java.util.List;

public class FarmerverseViewModel extends AndroidViewModel {

    private SeedRepository seedRepository;
    private ListingRepository listingRepository;

    private final LiveData<List<Seed>> allSeeds;
    private final LiveData<List<Listing>> allListings;

    public FarmerverseViewModel(Application application)
    {
        super(application);
        seedRepository = new SeedRepository(application);
        listingRepository = new ListingRepository(application);
        allSeeds = seedRepository.getAllSeeds();
        allListings = listingRepository.getAllListings();
    }

    public LiveData<List<Seed>> getAllSeeds()
    {
        return allSeeds;
    }

    public LiveData<List<Listing>> getAllListings()
    {
        return allListings;
    }

    public void insertSeed(Seed seed)
    {
        seedRepository.insert(seed);
    }

    public void insertListing(Listing listing)
    {
        listingRepository.insert(listing);
    }
}
