package com.example.farmerverse.repository.viewmodel;

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

    public Seed getSeed(int id)
    {
        return seedRepository.getSeed(id);
    }

    public void updateSeed(Seed s)
    {
        seedRepository.updateSeed(s);
    }

    public LiveData<List<Seed>> getAllSeeds()
    {
        return allSeeds;
    }

    public void deleteSeed(int id)
    {
        seedRepository.deleteSeed(id);
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
