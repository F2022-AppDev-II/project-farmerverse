package com.example.farmerverse.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.farmerverse.entities.Seed;
import com.example.farmerverse.repository.SeedRepository;

import java.util.List;

public class FarmerverseViewModel extends AndroidViewModel {

    private SeedRepository seedRepository;

    private final LiveData<List<Seed>> allSeeds;

    public FarmerverseViewModel(Application application)
    {
        super(application);
        seedRepository = new SeedRepository(application);
        allSeeds = seedRepository.getAllSeeds();
    }

    public LiveData<List<Seed>> getAllSeeds()
    {
        return allSeeds;
    }

    public void insertSeed(Seed seed)
    {
        seedRepository.insert(seed);
    }
}
